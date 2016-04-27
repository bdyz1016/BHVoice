package com.bhsc.mobile.userpages;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bhsc.mobile.R;
import com.bhsc.mobile.userpages.dialog.EditNicknameDialog;
import com.bhsc.mobile.userpages.dialog.EditStatusDialog;
import com.bhsc.mobile.userpages.model.Data_DB_User;
import com.bhsc.mobile.utils.L;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by lynn on 15-10-7.
 */
public class UserInfoActivity extends Activity implements View.OnClickListener {

    public static final int PHOTO_CHOOSE_WITH_DATA = 0x10;
    public static final int PHOTO_MAKE_WITH_DATA = 0x11;
    public static final int PHOTO_CUT = 0x12;

    private SimpleDraweeView mPhoto;
    private View mTakePhoto;
    private View mEditNickname;
    private View mEditStatus;
    private View mChangePasswd;
    private View mLogout;
    private TextView Tv_Nickname;
    private TextView Tv_Status;

    private PopupWindow mPopupWindow;

    private EditNicknameDialog mEditNicknameDialog;
    private EditStatusDialog mEditStatusDialog;

    private Data_DB_User mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        initWidget();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initWidget(){
        mPhoto = (SimpleDraweeView) findViewById(R.id.photo);
        Tv_Nickname = (TextView) findViewById(R.id.nickname);
        Tv_Status = (TextView) findViewById(R.id.status);
        mTakePhoto = findViewById(R.id.take_photo);
        mEditNickname = findViewById(R.id.nickname_edit);
        mEditStatus = findViewById(R.id.status_edit);
        mChangePasswd = findViewById(R.id.change_password);
        mLogout = findViewById(R.id.logout);
    }

    private void initView(){
        if(UserManager.isLogin() && (mCurrentUser = UserManager.getUser()) != null){
            if(!TextUtils.isEmpty(mCurrentUser.getHeadurl())){
                mPhoto.setImageURI(Uri.parse(mCurrentUser.getHeadurl()));
            }
            if(!TextUtils.isEmpty(mCurrentUser.getUsername())){
                Tv_Nickname.setText(mCurrentUser.getUsername());
            } else if(!TextUtils.isEmpty(mCurrentUser.getEmail())){
                Tv_Nickname.setText(mCurrentUser.getEmail());
            }
            if(!TextUtils.isEmpty(mCurrentUser.getStatus())){
                Tv_Status.setText(mCurrentUser.getStatus());
            }
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode != RESULT_OK) {
//            return;
//        }
//        switch (requestCode) {
//            case PHOTO_MAKE_WITH_DATA:
//                Bitmap photo = data.getParcelableExtra("data");
//                if (photo != null) {
//                    if (data.getData() == null) {
//                        try {
//                            String imageName = Method.getTS() + "";
//                            if (Method.isHaveSdcard()) {
//                                // Bitmap b = Bitmap.createScaledBitmap(photo, 100,
//                                // 100, true);
//                                if((BHApplication.screenWidth == 540 && BHApplication.screenHeight == 888)
//                                        ||(BHApplication.screenWidth == 720 && BHApplication.screenHeight == 1184)){
//                                    Method.saveMyBitmap_low(imageName, Bitmap.createBitmap(photo, photo.getWidth()/2-40, photo.getHeight()/2-40, 80, 80));
//                                    mViews.activity_userinfo_photo.setImageBitmap(Bitmap.createBitmap(photo, photo.getWidth()/2-40, photo.getHeight()/2-40, 80, 80));
//                                }else{
//                                    Method.saveMyBitmap_low(imageName, Bitmap.createBitmap(photo,photo.getWidth() / 2 - 50,photo.getHeight() / 2 - 50, 100,100));
//                                    mViews.activity_userinfo_photo.setImageBitmap(Bitmap.createBitmap(photo,photo.getWidth() / 2 - 50,photo.getHeight() / 2 - 50, 100, 100));
//                                }
//                            } else
//                                Toast.makeText(this,
//                                        getResources().getString(
//                                                R.string.user_userinfo_nosdcard),
//                                        Toast.LENGTH_LONG).show();
//                        } catch (IOException ex) {
//                            // 专为红米无SD卡无权限
//                            L.d("LL", "error IOException(红米无SD卡无权限)");
//                            ex.printStackTrace();
//                            Toast.makeText(this, getResources().getString(
//                                            R.string.user_userinfo_nosdcard),
//                                    Toast.LENGTH_SHORT).show();
//                            return;
//                        } catch (Exception ex) {
//                            L.d("LL", "error 4");
//                            Toast.makeText(this, getResources().getString(R.string.user_userinfo_takephoneError_take_failed),
//                                    Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//
//                    } else {
//                        // 拍照截图防止上传图片过大--licheng
//                        photoCut(data.getData());
//                    }
//                    System.out.println("开始点我截图片啦2！！！！！！！！！！！！");
//                } else {
//                    L.d("LL", "error 3");
//                    Toast.makeText(this,
//                            getResources()
//                                    .getString(
//                                            R.string.user_userinfo_takephoneError_take_failed),
//                            Toast.LENGTH_SHORT).show();
//                }
//                break;
//
//            case PHOTO_CHOOSE_WITH_DATA:
//                photoCut(data.getData());
//                break;
//
//            case PHOTO_CUT:
//                Bundle extras = data.getExtras();
//                Bitmap photo1 = null;
//                if (extras != null)
//                    photo1 = extras.getParcelable("data");
//                if (photo1 != null) {
//                    try {
//                        String imageName = Method.getTS() + "";
//                        if (Method.isHaveSdcard()) {
//                            Method.saveMyBitmap_low(imageName, photo1);
//                            mViews.activity_userinfo_photo.setImageBitmap(photo1);
//                        } else
//                            Toast.makeText(this,
//                                    getResources().getString(
//                                            R.string.user_userinfo_nosdcard),
//                                    Toast.LENGTH_LONG).show();
//                    } catch (IOException ex) {
//                        // 专为红米无SD卡无权限
//                        L.d("LL", "error IOException(红米无SD卡无权限)");
//                        ex.printStackTrace();
//                        Toast.makeText(this,getResources().getString(R.string.user_userinfo_nosdcard),
//                                Toast.LENGTH_SHORT).show();
//                        return;
//                    } catch (Exception ex) {
//                        L.d("LL", "error 4");
//                        ex.printStackTrace();
//                        Toast.makeText(this, getResources().getString(R.string.user_userinfo_takephoneError_cut_failed),
//                                Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//
//                } else {
//                    L.d("LL", "error 5");
//                    Toast.makeText(this, getResources().getString(R.string.user_userinfo_takephoneError_cut_failed),
//                            Toast.LENGTH_SHORT).show();
//                }
//                break;
//
//            default:
//                break;
//        }
//    }
//
//    private void complete() {
//        UserPresenter.getInstance(this).saveUserInfo(mCurrentUser);
//    }

    private void goBack() {
        finish();
    }

    private void editNickname(){
        if(mEditNicknameDialog != null && mEditNicknameDialog.isShowing()){
            mEditNicknameDialog.dismiss();
        }
        mEditNicknameDialog = new EditNicknameDialog(this);
        mEditNicknameDialog.setOnPositiveButtonClickListener(new EditNicknameDialog.OnPositiveButtonClickListener() {
            @Override
            public void onConfirm(String nickname) {
                Tv_Nickname.setText(nickname);
            }
        });
        mEditNicknameDialog.show();
    }

    private void editStatus(){
        if(mEditStatusDialog != null && mEditStatusDialog.isShowing()){
            mEditStatusDialog.dismiss();
        }
        mEditStatusDialog = new EditStatusDialog(this);
        mEditStatusDialog.setOnPositiveButtonClickListener(new EditStatusDialog.OnPositiveButtonClickListener() {
            @Override
            public void onConfirm(String status) {
                Tv_Status.setText(status);
            }
        });
        mEditStatusDialog.show();
    }

    private void logout(){
    }

    private void changePassword(){
        Intent intent = new Intent();
        intent.setClass(UserInfoActivity.this, ResetPasswordActivity.class);
        startActivity(intent);
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

        mPopupWindow.showAtLocation(findViewById(R.id.activity_userinfo_container),
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

//    private void setUserPhoto(String path){
//        if(!TextUtils.isEmpty(path)){
//            ImageUtil.getInstance().loadBitmap(path, mViews.activity_userinfo_photo, 100, 100);
//        }
//    }

//    private void bigImage(String path){
//        if(TextUtils.isEmpty(path)){
//            return;
//        }
//        Intent intent = new Intent();
//        intent.putExtra(ImageBrowserActivity.INTENT_PATH, path);
//        intent.setClass(UserInfoActivity.this, ImageBrowserActivity.class);
//        startActivity(intent);
//    }

//    public void onEventMainThread(UserEvent event){
//        if(event.getAction() == UserEvent.ACTION_UPDATE_USERINFO_SUCCESS){
//            finish();
//        } else if(event.getAction() == UserEvent.ACTION_DELETE_USERINFO_COMPLETE){
//            finish();
//        } else if(event.getAction() == UserEvent.ACTION_GET_USERINFO){
//            mCurrentUser = (Data_DB_User) event.getExtra();
//            mViews.activity_userinfo_nickname.setText(mCurrentUser.getNickName());
//            mViews.activity_userinfo_status.setText(mCurrentUser.getStatus());
////            ImageUtil.getInstance().loadBitmap(mCurrentUser.getPhotoPath(), mViews.activity_userinfo_photo, mViews.activity_userinfo_photo.getWidth(), mViews.activity_userinfo_photo.getHeight());
//        }
//    }

    private void tackPicture(){

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.take_photo:
                break;
            case R.id.status_edit:
                break;
            case R.id.nickname_edit:
                break;
            case R.id.change_password:
                break;
            case R.id.logout:
                break;
        }
    }
}
