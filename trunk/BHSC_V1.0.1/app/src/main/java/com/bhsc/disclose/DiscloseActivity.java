package com.bhsc.disclose;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
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

import com.bhsc.MyApplication;
import com.bhsc.mobile.R;
import com.bhsc.net.UploadFile;
import com.bhsc.userpages.dialog.DefaultDialog;
import com.bhsc.utils.L;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jaeger.ninegridimageview.NineGridImageView;
import com.jaeger.ninegridimageview.NineGridImageViewAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;


/**
 * Created by lynn on 15-9-29.
 */
public class DiscloseActivity extends Activity {
    private final String TAG = DiscloseActivity.class.getSimpleName();

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

    private int mPictureCount;

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
    }

    private void menu() {
    }

    private void goBack() {
        finish();
    }

    private void confirm() {
        new SubmitAsync().execute("");
    }

    private void cancel() {
        finish();
    }

    private void picturesItemClicked() {
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
                    List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                    if (path != null && !path.isEmpty()) {
                        if (path.size() < MAX_IMG) {
                            path.add(ADD_IMAGE);
                        }
                        mPictures.remove(ADD_IMAGE);
                        mPictures.addAll(path);
                        mNineGridImageView.setImagesData(new ArrayList<>(mPictures));
                    }
                }
                break;
        }
    }

    private NineGridImageViewAdapter<String> mAdapter = new NineGridImageViewAdapter<String>() {
        @Override
        protected void onDisplayImage(Context context, ImageView imageView, String s) {
            L.i(TAG, "image path:" + s);
            if (!TextUtils.isEmpty(s) && imageView != null && imageView instanceof SimpleDraweeView) {
                SimpleDraweeView simpleDraweeView = (SimpleDraweeView) imageView;
                if (ADD_IMAGE.equals(s)) {
                    simpleDraweeView.setImageURI(Uri.parse("res://com.bhsc.mobile/" + R.mipmap.btn_add_image));
                } else {
                    simpleDraweeView.setImageURI(Uri.parse("file://" + s));
                }
            }
        }

        @Override
        protected ImageView generateImageView(Context context) {
            SimpleDraweeView simpleDraweeView = new SimpleDraweeView(context);
            GenericDraweeHierarchy hierarchy = simpleDraweeView.getHierarchy();
            hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP);
            return simpleDraweeView;
        }

        @Override
        protected void onItemImageClick(Context context, int index, List<String> list) {
            Toast.makeText(context, "image position is " + index, Toast.LENGTH_SHORT).show();
            String str = list.get(index);
            if (!ADD_IMAGE.equals(str)) {
                showDialag();
            } else {
                int imageNumber = MAX_IMG - mPictures.size() + 1;
                Intent intent = new Intent(DiscloseActivity.this, MultiImageSelectorActivity.class);
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, imageNumber);
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
                intent.putStringArrayListExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, new ArrayList<String>());
                startActivityForResult(intent, REQUEST_IMAGE);
            }
        }
    };

    private class SubmitAsync extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            List<File> files = new ArrayList<>();
            mPictures.remove(ADD_IMAGE);
            for (String path : mPictures) {
                File file = new File(path);
                if (file.exists()) {
                    files.add(file);
                }
            }
            String title = params[0];
            String content = params[1];
            String userId = params[2];
            try {
                String response = UploadFile.uploadMultiFileSync(MyApplication.Address + "/mood/addMood?title="+ title +"&content="+ content +"&userId=" + userId, null, files, null);
                L.i(TAG, "add mood:" + response);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
