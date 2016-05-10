package com.bhsc.mobile.userpages;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.bhsc.mobile.R;
import com.bhsc.mobile.net.RequestError;
import com.bhsc.mobile.userpages.dialog.EditNicknameDialog;
import com.bhsc.mobile.userpages.dialog.EditStatusDialog;
import com.bhsc.mobile.userpages.model.Data_DB_User;
import com.bhsc.mobile.utils.FileUtil;
import com.bhsc.mobile.utils.L;
import com.bhsc.mobile.views.ImageViewer;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import vn.tungdx.mediapicker.MediaItem;
import vn.tungdx.mediapicker.MediaOptions;
import vn.tungdx.mediapicker.activities.MediaPickerActivity;
import vn.tungdx.mediapicker.utils.Utils;

/**
 * Created by lynn on 15-10-7.
 */
public class UserInfoActivity extends Activity implements View.OnClickListener {
    private final String TAG = UserInfoActivity.class.getSimpleName();

    private final int REQUEST_IMAGE = 1;

    private SimpleDraweeView mPhoto;
    private View mTakePhoto;
    private View mEditNickname;
    private View mEditStatus;
    private View mChangePasswd;
    private View mLogout;
    private View mGoBack;
    private TextView Tv_Nickname;
    private TextView Tv_Status;

    private EditNicknameDialog mEditNicknameDialog;
    private EditStatusDialog mEditStatusDialog;

    private UserManager mUserManager;

    private ProgressDialog mProgressDialog;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        L.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        initWidget();
        initData();
        initView();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        L.i(TAG, "onPostResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initWidget(){
        L.i(TAG, "initWidget");
        mPhoto = (SimpleDraweeView) findViewById(R.id.photo);
        mPhoto.setOnClickListener(this);
        Tv_Nickname = (TextView) findViewById(R.id.nickname);
        Tv_Status = (TextView) findViewById(R.id.status);
        mTakePhoto = findViewById(R.id.take_photo);
        mTakePhoto.setOnClickListener(this);
        mEditNickname = findViewById(R.id.nickname_edit);
        mEditNickname.setOnClickListener(this);
        mEditStatus = findViewById(R.id.status_edit);
        mEditStatus.setOnClickListener(this);
        mChangePasswd = findViewById(R.id.change_password);
        mChangePasswd.setOnClickListener(this);
        mLogout = findViewById(R.id.logout);
        mLogout.setOnClickListener(this);
        mGoBack = findViewById(R.id.back);
        mGoBack.setOnClickListener(this);
    }

    private void initData(){
        mContext = this;
        mUserManager = new UserManager(this);
    }

    private void initView(){
        L.i(TAG, "initView");
        Data_DB_User user = UserManager.getUser();
        if(UserManager.isLogin() && user != null){
            if(!TextUtils.isEmpty(user.getHeadurl())){
                mPhoto.setImageURI(Uri.parse(user.getHeadurl()));
            }
            if(!TextUtils.isEmpty(user.getUsername())){
                Tv_Nickname.setText(user.getUsername());
            } else if(!TextUtils.isEmpty(user.getEmail())){
                Tv_Nickname.setText(user.getEmail());
            }
            if(!TextUtils.isEmpty(user.getStatus())){
                Tv_Status.setText(user.getStatus());
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        L.i(TAG, "onActivityResult");
        switch (requestCode){
            case REQUEST_IMAGE:
                if(resultCode != RESULT_OK){
                    return;
                }
                List<MediaItem> mediaSelectedList = MediaPickerActivity
                        .getMediaItemSelected(data);
                for (MediaItem mediaItem : mediaSelectedList) {
                    if (mediaItem.isPhoto()) {
                        String filePath = mediaItem.getPathCropped(this);
                        setUserPhoto(filePath);
                    }
                }
                break;
        }
    }
//
//    private void complete() {
//        UserPresenter.getInstance(this).saveUserInfo(mCurrentUser);
//    }

    private void goBack() {
        mUserManager.cancel();
        finish();
    }

    private void editNickname(){
        if(mEditNicknameDialog != null && mEditNicknameDialog.isShowing()){
            mEditNicknameDialog.dismiss();
        }
        mEditNicknameDialog = new EditNicknameDialog(this);
        mEditNicknameDialog.setOnPositiveButtonClickListener(new EditNicknameDialog.OnPositiveButtonClickListener() {
            @Override
            public void onConfirm(final String nickname) {
                displayProgressDialog("");
                mUserManager.setUserName(nickname, new UserManager.OnEditUserInfoListener() {
                    @Override
                    public void success() {
                        Tv_Nickname.setText(nickname);
                        dismissProgressDialog();
                    }

                    @Override
                    public void failed(RequestError error) {
                        dismissProgressDialog();
                        Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
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
            public void onConfirm(final String status) {
                displayProgressDialog(null);
                mUserManager.setStatus(status, new UserManager.OnEditUserInfoListener() {
                    @Override
                    public void success() {
                        Tv_Status.setText(status);
                        dismissProgressDialog();
                    }

                    @Override
                    public void failed(RequestError error) {
                        dismissProgressDialog();
                        Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        mEditStatusDialog.show();
    }

    private void changePassword(){
        Intent intent = new Intent();
        intent.setClass(UserInfoActivity.this, ResetPasswordActivity.class);
        startActivity(intent);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.take_photo:
                takePhoto();
                break;
            case R.id.status_edit:
                editStatus();
                break;
            case R.id.nickname_edit:
                editNickname();
                break;
            case R.id.change_password:
                changePassword();
                break;
            case R.id.logout:
                UserManager.logout();
                finish();
                break;
            case R.id.back:
                goBack();
                break;
            case R.id.photo:
                ImageViewer imageViewer = new ImageViewer(mContext);
                List<String> list = new ArrayList<>();
                list.add(UserManager.getUser().getHeadurl());
                imageViewer.setImageSource(list, 0);
                imageViewer.show();
                break;
        }
    }

    private void takePhoto(){
        File filePath;
        if (!Utils.hasExternalStorage() || (filePath = getExternalCacheDir()) == null) {
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
            MediaPickerActivity.open(this, REQUEST_IMAGE, options);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setUserPhoto(final String filePath){
        L.i(TAG, "image path:" + filePath);
        displayProgressDialog(null);
        mUserManager.setUserPhoto(new File(filePath), new UserManager.OnEditUserInfoListener() {
            @Override
            public void success() {
                Uri uri = Uri.parse("file://" + filePath);
                mPhoto.setImageURI(uri);
                dismissProgressDialog();
            }

            @Override
            public void failed(RequestError error) {
                dismissProgressDialog();
                Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
            }});
    }

    private void displayProgressDialog(String message){
        dismissProgressDialog();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        if(!TextUtils.isEmpty(message)){
            mProgressDialog.setMessage(message);
        }
        mProgressDialog.show();
    }

    private void dismissProgressDialog(){
        if(mProgressDialog != null && mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
    }

}
