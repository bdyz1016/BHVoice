package com.bhsc.mobile.disclose;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.bhsc.mobile.MyApplication;
import com.bhsc.mobile.disclose.model.DiscloseResponse;
import com.bhsc.mobile.R;
import com.bhsc.mobile.net.UploadFile;
import com.bhsc.mobile.userpages.UserManager;
import com.bhsc.mobile.userpages.dialog.DefaultDialog;
import com.bhsc.mobile.utils.FileUtil;
import com.bhsc.mobile.utils.ImageUtil;
import com.bhsc.mobile.utils.L;
import com.bhsc.mobile.views.ImageViewer;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaeger.ninegridimageview.NineGridImageView;
import com.jaeger.ninegridimageview.NineGridImageViewAdapter;
import com.orm.dsl.NotNull;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vn.tungdx.mediapicker.MediaItem;
import vn.tungdx.mediapicker.MediaOptions;
import vn.tungdx.mediapicker.activities.MediaPickerActivity;
import vn.tungdx.mediapicker.utils.Utils;


/**
 * Created by lynn on 15-9-29.
 */
public class DiscloseActivity extends Activity {
    private final String TAG = DiscloseActivity.class.getSimpleName();

    public final static int PICTURE_MAX_WIDTH = 800;
    public final static int PICTURE_MAX_HEIGHT = 800;

    public static final int REQUEST_IMAGE = 0x1;
    public static final int PHOTO_CHOOSE_WITH_DATA = 0x10;
    public static final int PHOTO_MAKE_WITH_DATA = 0x11;
    public static final int PHOTO_CUT = 0x12;

    private final int MAX_IMG = 9;

    private final String IMAGES = "images";

    private final String ADD_IMAGE = "add_image";

    private View mBack;
    private EditText Edit_Title;
    private EditText Edit_Content;
    private NineGridImageView mNineGridImageView;
    private Button Btn_Confirm, Btn_Cancel;

    private ArrayList<String> mPictures;

    private PopupWindow mPopupWindow;

    private DefaultDialog mDialog;
    private ProgressDialog mProgressDialog;
    private int mPictureCount;

    private String mUserId;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mPictures = savedInstanceState.getStringArrayList(IMAGES);
        } else {
            mPictures = new ArrayList<>();
            mPictures.add(ADD_IMAGE);
        }
        setContentView(R.layout.activity_disclose);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        initWidget();
        initData();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putStringArrayList(IMAGES, mPictures);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if(keyCode == KeyEvent.KEYCODE_BACK){
//            if(mPictureAdapter.isEditable()){
//                L.i(TAG, "Picture is Editable");
//                mPictureAdapter.setEdit(PictureAdapter.UN_EDITABLE);
//                return true;
//            }
//        }
        return super.onKeyDown(keyCode, event);
    }

    private void initWidget() {
        mBack = findViewById(R.id.back);
        Edit_Title = (EditText) findViewById(R.id.title);
        Edit_Content = (EditText) findViewById(R.id.content);
        mNineGridImageView = (NineGridImageView) findViewById(R.id.images);
        Btn_Confirm = (Button) findViewById(R.id.confirm);
        Btn_Cancel = (Button) findViewById(R.id.cancel);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
        Btn_Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm();
            }
        });
        Btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }

    private void initData() {
        mNineGridImageView.setAdapter(mAdapter);
        mNineGridImageView.setImagesData(new ArrayList<>(mPictures));
        if (UserManager.isLogin()) {
            mUserId = String.valueOf(UserManager.getUser().getId());
        } else {
            Toast.makeText(this, "请登录", Toast.LENGTH_SHORT).show();
            finish();
        }
        mContext = this;
    }

    private void menu() {
    }

    private void goBack() {
        finish();
    }

    private void confirm() {
        displayProgressDialog("正在上传");
        String title = Edit_Title.getText().toString();
        String content = Edit_Content.getText().toString();
        new SubmitAsync().execute(title, content, mUserId);
    }

    private void cancel() {
        finish();
    }

    private void picturesItemClicked() {
    }

    private void displayProgressDialog(@NotNull String message) {
        dismissPrigressDialog();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    private void dismissPrigressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private void showDialag() {
        backgroundAlpha(0.4f);

        View view = LayoutInflater.from(this).inflate(R.layout.select_image, null);

        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        view.setBackgroundColor(Color.BLACK);
        view.getBackground().setAlpha(0);
        mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        mPopupWindow.setAnimationStyle(R.style.pop_anim_style);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());

        mPopupWindow.setOutsideTouchable(true);

        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });

        //取消
        View cancel = view.findViewById(R.id.select_image_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });

        //拍照
        View camera = view.findViewById(R.id.select_image_camera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
                pickPhotoFromCamera();
            }
        });

        //相册
        View gallery = view.findViewById(R.id.select_image_gallery);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
                pickPhotoFromGallery();
            }
        });

        mPopupWindow.showAtLocation(findViewById(R.id.activity_disclose_container),
                Gravity.BOTTOM, 0, 0);
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    /**
     * 打开摄像头
     */
    private void pickPhotoFromCamera() {
        // 屏蔽第三方拍照软件，设置系统相机为默认
        PackageInfo pi = null;
        int i = 0;// 判断能不能读取包名
        // 三星
        try {
            // 获得相机包名信息
            pi = getPackageManager().getPackageInfo("com.sec.android.app.camera", 0);
            L.i("pi============", pi + "");
            if (pi.packageName != null) {
                // 获得当前应用程序的包管理器
                PackageManager pm = this.getPackageManager();
                Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
                // 设置点击程序时最先运行
                resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                resolveIntent.setPackage(pi.packageName);
                List<ResolveInfo> apps = pm.queryIntentActivities(
                        resolveIntent, 0);
                ResolveInfo ri = apps.iterator().next();
                if (ri != null) {
                    String packageName = ri.activityInfo.packageName;
                    String className = ri.activityInfo.name;
                    // 此处将Intent的Action设置为捕获照片
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // 设置期望被打开的App包名和类名
                    ComponentName cn = new ComponentName(packageName, className);
                    L.i("packageName+++++++++++++++++", packageName);
                    L.i("className+++++++++++++++++", className);
                    intent.setComponent(cn);
                    startActivityForResult(intent, PHOTO_MAKE_WITH_DATA);
                    i = 1;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        // 不能读取包名（暂时不能屏蔽第三方）
        if (i == 0) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, PHOTO_MAKE_WITH_DATA);
        }
    }

    /**
     * 读取相册图片
     */
    private void pickPhotoFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);// 调用android的图库
        startActivityForResult(intent, PHOTO_CHOOSE_WITH_DATA);
    }

    public void photoCut(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 256);
        intent.putExtra("outputY", 256);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_CUT);
    }


    private void hideSoftInputMethod() {
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(views.activity_disclose_edit_title.getWindowToken(), 0);
    }


    private void shake(View v) {
        v.clearAnimation();
        Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        v.startAnimation(shake);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_IMAGE:
                if (resultCode != RESULT_OK) {
                    Toast.makeText(this, "照片选择失败", Toast.LENGTH_SHORT).show();
                } else {
                    List<MediaItem> mediaSelectedList = MediaPickerActivity
                            .getMediaItemSelected(data);
                    for (MediaItem mediaItem : mediaSelectedList) {
                        String filePath = mediaItem.getPathCropped(mContext);
                        L.i(TAG, "select file path:" + filePath);
                        if (mediaItem.isPhoto()) {
                            mPictures.add(mPictures.size() - 1, filePath);
                            if(mPictures.size() > MAX_IMG){
                                mPictures.remove(ADD_IMAGE);
                            }
                            mNineGridImageView.setImagesData(new ArrayList<>(mPictures));
                        }
                    }
                }
                break;
        }
    }

    private NineGridImageViewAdapter<String> mAdapter = new NineGridImageViewAdapter<String>() {
        @Override
        protected void onDisplayImage(Context context, ImageView imageView, String s) {
            L.i(TAG, "image path:" + s);
//            if (!TextUtils.isEmpty(s) && imageView != null && imageView instanceof SimpleDraweeView) {
//                SimpleDraweeView simpleDraweeView = (SimpleDraweeView) imageView;
//                if (ADD_IMAGE.equals(s)) {
//                    simpleDraweeView.setImageURI(Uri.parse("res://com.bhsc.mobile/" + R.mipmap.btn_add_image));
//                } else {
//                    simpleDraweeView.setImageURI(Uri.parse("file://" + s));
//                }
//            }
            if (ADD_IMAGE.equals(s)) {
                Picasso.with(context)
                        .load(R.mipmap.btn_add_image).resize(MyApplication.DISCLOSE_IMAGE_RESIZE, MyApplication.DISCLOSE_IMAGE_RESIZE).centerCrop()
                        .into(imageView);
            } else {
                Uri uri = Uri.parse("file://" + s);
                Picasso.with(context)
                        .load(uri).resize(MyApplication.DISCLOSE_IMAGE_RESIZE, MyApplication.DISCLOSE_IMAGE_RESIZE)
                        .centerCrop()
                        .placeholder(R.color.background_color)
                        .into(imageView);
            }
        }

        @Override
        protected ImageView generateImageView(Context context) {
//            SimpleDraweeView simpleDraweeView = new SimpleDraweeView(context);
//            GenericDraweeHierarchy hierarchy = simpleDraweeView.getHierarchy();
//            hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP);
//            return simpleDraweeView;
            return new ImageView(context);
        }


        @Override
        protected void onItemImageClick(Context context, final int index, List<String> list) {
            String str = list.get(index);
            if (!ADD_IMAGE.equals(str)) {
                final DefaultDialog defaultDialog = new DefaultDialog(mContext);
                defaultDialog.setMessage("移除图片？");
                defaultDialog.setOnNegativeClickListener(new DefaultDialog.OnButtonClickListener() {
                    @Override
                    public void onClick() {
                        defaultDialog.dismiss();
                    }
                });
                defaultDialog.setOnPositiveClickListener(new DefaultDialog.OnButtonClickListener() {
                    @Override
                    public void onClick() {
                        mPictures.remove(index);
                        mNineGridImageView.setImagesData(new ArrayList<>(mPictures));
                        defaultDialog.dismiss();
                    }
                });
                defaultDialog.show();
            } else {
                File filePath;
                if (!Utils.hasExternalStorage() || (filePath = mContext.getExternalCacheDir()) == null) {
                    return;
                }
                try {
                    File file = FileUtil.createFile(filePath.getAbsolutePath(), String.valueOf(System.currentTimeMillis()) + ".jpg");
                    MediaOptions.Builder builder = new MediaOptions.Builder();
                    MediaOptions options = builder
                            .canSelectMultiPhoto(false)
                            .setIsCropped(true)
                            .setFixAspectRatio(false)
                            .setCroppedFile(file)
                            .build();
                    MediaPickerActivity.open(DiscloseActivity.this, REQUEST_IMAGE, options);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private class SubmitAsync extends AsyncTask<String, Void, Integer> {

        private Gson mGson;

        public SubmitAsync() {
            mGson = new Gson();
        }

        @Override
        protected Integer doInBackground(String... params) {
            List<File> files = new ArrayList<>();
            mPictures.remove(ADD_IMAGE);
            for (String path : mPictures) {
                File file = new File(path);
                if (file.exists()) {
                    Bitmap bitmap = ImageUtil.decodeSampledBitmapFromSource(path, PICTURE_MAX_WIDTH, PICTURE_MAX_HEIGHT);
                    try {
                        FileOutputStream fileOutputStream = new FileOutputStream(file, false);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 50, fileOutputStream);
                        fileOutputStream.flush();
                        fileOutputStream.close();
                        files.add(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//                    files.add(file);
                }
            }
            String title = params[0];
            String content = params[1];
            String userId = params[2];
            String response = null;
            try {
                String url = MyApplication.Address + "/mood/addMood";
                L.i(TAG, "add mood url:" + url);
                Map<String, String> requestParams = new HashMap<>();
                requestParams.put("title", title);
                requestParams.put("content", content);
                requestParams.put("userId", userId);
                response = UploadFile.uploadMultiFileSync(url, null, files, requestParams);
                L.i(TAG, "add mood:" + response);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!TextUtils.isEmpty(response)) {
                DiscloseResponse discloseResponse = mGson.fromJson(response, new TypeToken<DiscloseResponse>() {
                }.getType());
                int responseCode = discloseResponse.getCode();
                if(responseCode == DiscloseResponse.SUCESS_CODE){
                    for(File file:files){
                        file.delete();
                    }
                }
                return responseCode;
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            dismissPrigressDialog();
            if (integer != null && integer == DiscloseResponse.SUCESS_CODE) {
                finish();
            } else {
                Toast.makeText(DiscloseActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
