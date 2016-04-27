package com.bhsc.mobile.userpages;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bhsc.mobile.R;
import com.bhsc.mobile.userpages.model.Data_DB_User;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by lynn on 15-9-17.
 */
public class UserFragment extends Fragment implements View.OnClickListener{
    private final String TAG = UserFragment.class.getSimpleName();

    private SimpleDraweeView mPhoto;
    private TextView Tv_Username;
    private View mDiscuss, mDisclose, mAbout, mFeedback;

    private View mContentView;
    private Context mContext;
    private Data_DB_User mUserInfo;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_user, container, false);
        initWidget();
        return mContentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void initWidget(){
        mPhoto = (SimpleDraweeView) mContentView.findViewById(R.id.photo);
        Tv_Username = (TextView) mContentView.findViewById(R.id.nick_name);
        mDiscuss = mContentView.findViewById(R.id.discuss);
        mDisclose = mContentView.findViewById(R.id.disclose);
        mAbout = mContentView.findViewById(R.id.about);
        mFeedback = mContentView.findViewById(R.id.feedback);
        mPhoto.setOnClickListener(this);
    }

    private void initView(){
        if(UserManager.isLogin() && (mUserInfo = UserManager.getUser()) != null) {
            if (!TextUtils.isEmpty(mUserInfo.getHeadurl())) {
                mPhoto.setImageURI(Uri.parse(mUserInfo.getHeadurl()));
            }
            if (!TextUtils.isEmpty(mUserInfo.getUsername())){
                Tv_Username.setText(mUserInfo.getUsername());
            } else if(!TextUtils.isEmpty(mUserInfo.getEmail())){
                Tv_Username.setText(mUserInfo.getEmail());
            }
        }
    }

    private void version(){}

    private void settings(){
//        Intent intent = new Intent();
//        intent.setClass(mContext, SettingsActivity.class);
//        startActivity(intent);
    }

    private void disclose(){
        if(UserManager.isLogin()){
//            Intent intent = new Intent();
//            intent.setClass(mContext, DiscloseActivity.class);
//            startActivity(intent);
        } else {
            Toast.makeText(mContext, "请先登录!", Toast.LENGTH_SHORT).show();
        }
    }

    private void feedback(){
//        Intent intent = new Intent();
//        intent.setClass(mContext, FeedbackActivity.class);
//        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.about:
                break;
            case R.id.feedback:
                feedback();
                break;
            case R.id.disclose:
                disclose();
                break;
            case R.id.discuss:
                break;
            case R.id.photo:
                if(UserManager.isLogin()){
                    startActivity(new Intent(mContext, UserInfoActivity.class));
                } else {
                    startActivity(new Intent(mContext, LoginAndRegisterActivity.class));
                }
                break;
            default:
                break;
        }
    }
}
