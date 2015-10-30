package com.bhsc.mobile.disclose;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.pc.ioc.event.EventBus;
import com.android.pc.ioc.inject.InjectAll;
import com.android.pc.ioc.inject.InjectBinder;
import com.android.pc.ioc.inject.InjectInit;
import com.android.pc.ioc.inject.InjectLayer;
import com.android.pc.ioc.view.listener.OnClick;
import com.android.pc.ioc.view.listener.OnItemClick;
import com.bhsc.mobile.R;
import com.bhsc.mobile.common.ImageBrowserActivity;
import com.bhsc.mobile.datalcass.Data_DB_Disclose;
import com.bhsc.mobile.disclose.event.ActionEvent;
import com.bhsc.mobile.main.BHApplication;
import com.bhsc.mobile.manager.UserManager;
import com.bhsc.mobile.media.ImageUtil;
import com.bhsc.mobile.utils.L;
import com.bhsc.mobile.utils.Method;
import com.bhsc.mobile.view.dialog.DefaultDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by lynn on 15-9-29.
 */
@InjectLayer(R.layout.activity_disclose)
public class DiscloseActivity extends Activity {
    public static final int PHOTO_CHOOSE_WITH_DATA = 0x10;
    public static final int PHOTO_MAKE_WITH_DATA = 0x11;
    public static final int PHOTO_CUT = 0x12;

    class Views {
        @InjectBinder(method = "menu", listeners = {OnClick.class})
        View fragment_disclose_title_menu;
        @InjectBinder(method = "goBack", listeners = {OnClick.class})
        View fragment_disclose_title_back;
        EditText activity_disclose_edit_title;
        EditText activity_disclose_edit_content;
        @InjectBinder(method = "confirm", listeners = {OnClick.class})
        View activity_disclose_edit_confirm;
        @InjectBinder(method = "cancel", listeners = {OnClick.class})
        View activity_disclose_edit_cancel;
        GridView activity_disclose_edit_images;
    }

    @InjectAll
    Views views;

    private PictureAdapter mPictureAdapter;
    private ArrayList<String> mPictures;

    private PopupWindow mPopupWindow;
    private DisclosePresenter mDisclosePresenter;

    private DefaultDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @InjectInit
    private void init() {
        mDisclosePresenter = DisclosePresenter.getInstance(this);

        mPictures = new ArrayList<>();
        mPictures.add(PictureAdapter.DEFAULT_IMAGE);
        mPictureAdapter = new PictureAdapter(this, R.layout.item_disclose_picture, mPictures);
        mPictureAdapter.setOnAddPictureListener(mAddPictureListener);

        views.activity_disclose_edit_images.setAdapter(mPictureAdapter);
        views.activity_disclose_edit_images.setOnItemClickListener(mItemClickListener);
    }

    private void menu() {
    }

    private void goBack() {
        finish();
    }

    private void confirm() {
        String title = views.activity_disclose_edit_title.getText().toString();
        String content = views.activity_disclose_edit_content.getText().toString();
        mPictures.remove(mPictures.size() - 1);//删除添加图片按钮
        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(content)) {
            Data_DB_Disclose disclose = new Data_DB_Disclose();
            disclose.setContent(content);
            disclose.setTitle(title);
            disclose.setCreateTime(Method.getTS());
            disclose.setUserName(UserManager.getInstance(this).getCurrentUser().getUserName());
            mDisclosePresenter.publish(disclose, mPictures);
        } else {
            if (mDialog != null && mDialog.isShowing()) {
                mDialog = new DefaultDialog(DiscloseActivity.this);
                mDialog.setMessage("爆料内容不能为空!");
                mDialog.setOnPositiveClickListener(new DefaultDialog.OnButtonClickListener() {
                    @Override
                    public void onClick() {
                        mDialog.dismiss();
                    }
                });
            }
        }
    }

    private void cancel() {
        finish();
    }

    private void picturesItemClicked() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case PHOTO_MAKE_WITH_DATA:
                Bitmap photo = data.getParcelableExtra("data");
                Uri photoPath = data.getData();
                if (photoPath != null) {
                    picturesChanged(mDisclosePresenter.copyPicture(DiscloseActivity.this, photoPath, ImageUtil.TempPath + Method.getTS() + ".png"));
                } else if (photo != null) {
                    picturesChanged(mDisclosePresenter.savePicture(photo, ImageUtil.TempPath + Method.getTS() + ".png"));
                }
                break;
            case PHOTO_CHOOSE_WITH_DATA:
                picturesChanged(mDisclosePresenter.copyPicture(DiscloseActivity.this, data.getData(), ImageUtil.TempPath + Method.getTS() + ".png"));
                break;
            case PHOTO_CUT:
                Bundle extras = data.getExtras();
                Bitmap image = null;
                if (extras != null) {
                    image = extras.getParcelable("data");
                }
                if (image != null) {
                    String path = mDisclosePresenter.savePicture(image, ImageUtil.TempPath + Method.getTS() + ".png");
                    picturesChanged(path);
                }
                break;
            default:
                break;
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
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);// 调用android的图库
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

    private PictureAdapter.OnAddPictureListener mAddPictureListener = new PictureAdapter.OnAddPictureListener() {
        @Override
        public void addPicture() {
            showDialag();
        }
    };

    /**
     * when picture quantity > 5,hide the add button
     */
    private void hideAddImageButton() {
        if (mPictures.size() > Data_DB_Disclose.IMAGE_MAX_COUNT) {
            mPictures.remove(PictureAdapter.DEFAULT_IMAGE);
        }
    }

    private void picturesChanged(String path) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        int length = mPictures.size();
        int position = length > 0 ? length - 1 : 0;
        mPictures.add(position, path);
        hideAddImageButton();
        mPictureAdapter = new PictureAdapter(this, R.layout.item_disclose_picture, mPictures);
        views.activity_disclose_edit_images.setAdapter(mPictureAdapter);
        views.activity_disclose_edit_images.setOnItemClickListener(mItemClickListener);
    }

    private void hideSoftInputMethod() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(views.activity_disclose_edit_title.getWindowToken(), 0);
    }

    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            hideSoftInputMethod();
            String imagePath = mPictures.get(position);
            if (PictureAdapter.DEFAULT_IMAGE.equals(imagePath)) {
                showDialag();
            } else {
                Intent intent = new Intent();
                intent.putExtra(ImageBrowserActivity.INTENT_PATH, mPictures.get(position));
                intent.setClass(DiscloseActivity.this, ImageBrowserActivity.class);
                startActivity(intent);
            }
        }
    };

    public void onEventMainThread(ActionEvent event) {
        if (event.getAction() == ActionEvent.ACTION_ADD_DISCLOSE_FINISH) {
            finish();
        }
    }
}
