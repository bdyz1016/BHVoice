//package com.bhsc.userpages;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.text.TextUtils;
//
//import com.bhsc.mobile.R;
//import com.bhsc.utils.L;
//import com.bhsc.utils.Method;
//import com.google.gson.Gson;
//
//import java.util.ArrayList;
//import java.util.LinkedHashMap;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
///**
// * Created by lynn on 15-10-7.
// */
//public class UserPresenter {
//    private final String TAG = UserPresenter.class.getSimpleName();
//
//    private static UserPresenter sUserPresenter;
//
//    public static UserPresenter getInstance(Context context) {
//        if (sUserPresenter == null) {
//            synchronized (UserPresenter.class) {
//                if (sUserPresenter == null) {
//                    sUserPresenter = new UserPresenter(context);
//                }
//            }
//        }
//        return sUserPresenter;
//    }
//
//    private ExecutorService mExecutorService;
//    private Context mContext;
//    private Gson mGson;
//
//    private UserPresenter(Context context) {
//        mExecutorService = Executors.newCachedThreadPool();
//        mContext = context;
//        mGson = new Gson();
//    }
//
//    public void initUserData() {
//        L.i(TAG, "initUserData");
//        mExecutorService.submit(new Runnable() {
//            @Override
//            public void run() {
//                L.i(TAG, "create discusses");
//                ArrayList<Data_DB_Discuss> discusses = new ArrayList<>();
//                for (int i = 0; i < 10; i++) {
//                    Data_DB_Discuss data = new Data_DB_Discuss();
//                    data.setContent("屠呦呦宁波旧居要卖1.5亿 吐槽：现在满世界都是我");
//                    data.setCreateTime(Method.getTS());
//                    discusses.add(data);
//                }
//                DiscussEvent event = new DiscussEvent();
//                event.setDiscusses(discusses);
//                EventBus.getDefault().post(event);
//
//                if (UserManager.getInstance(mContext).isLogined()) {
//                    UserEvent userEvent = new UserEvent();
//                    userEvent.setAction(UserEvent.ACTION_GET_USERINFO);
//                    userEvent.setExtra(UserManager.getInstance(mContext).getCurrentUser());
//                    EventBus.getDefault().post(userEvent);
//                }
//            }
//        });
//    }
//
//    public void register(final String email, final String password,final String confirmPassword) {
//        mExecutorService.submit(new Runnable() {
//            @Override
//            public void run() {
//                //检验两次密码是否一致
//                if(TextUtils.isEmpty(password)){
//                    UserEvent event = new UserEvent();
//                    event.setAction(UserEvent.ACTION_REGISTER_FAILED);
//                    event.setExtra(mContext.getString(R.string.login_error_7));
//                    event.setErrorCode(UserEvent.ERROR_PASSWORD_EMPTY);
//                    EventBus.getDefault().post(event);
//                    return;
//                } else if(TextUtils.isEmpty(confirmPassword) ||!password.equals(confirmPassword)){
//                    UserEvent event = new UserEvent();
//                    event.setAction(UserEvent.ACTION_REGISTER_FAILED);
//                    event.setExtra(mContext.getString(R.string.register_error_0));
//                    event.setErrorCode(UserEvent.ERROR_PASSWORD_INCONSISTENT);
//                    EventBus.getDefault().post(event);
//                    return;
//                }
//
//                int checkFormatResult = Method.checkUserNameAndPswd(email, password);
//                //检验用户名密码格式是否正确
//                if(checkFormatResult != 0) {
//                    String message;
//                    switch (checkFormatResult) {
//                        case 1:
//                            message = mContext.getString(R.string.login_error_5);
//                            break;
//                        case 2:
//                            message = mContext.getString(R.string.login_error_6);
//                            break;
//                        case 3:
//                            message = mContext.getString(R.string.login_error_7);
//                            break;
//                        case 4:
//                            message = mContext.getString(R.string.login_error_8);
//                            break;
//                        default:
//                            message = mContext.getString(R.string.login_error_2);
//                            break;
//                    }
//                    UserEvent event = new UserEvent();
//                    event.setAction(UserEvent.ACTION_REGISTER_FAILED);
//                    event.setExtra(message);
//                    event.setErrorCode(checkFormatResult);
//                    EventBus.getDefault().post(event);
//                    return;
//                }
//
//                LinkedHashMap<String, String> params = new LinkedHashMap<>();
//                params.put("loginId", email);
//                params.put("email", email);
//                params.put("password", password);
//                httpPost httpPost = new httpPost();
//                try {
//                    String response = httpPost.requireClass(BHApplication.Address + "/user/register", params, "UTF-8");
//                    L.i(TAG, response);
//                    ObjectResponse<Data_DB_User> userResponse = mGson.fromJson(response, new TypeToken<ObjectResponse<Data_DB_User>>(){}.getType());
//                    if(userResponse.getCode() == Response.SUCESS_CODE){
//                        UserManager.getInstance(mContext).setCurrentUser(userResponse.getObject());
//                        UserEvent event = new UserEvent();
//                        event.setAction(UserEvent.ACTION_REGISTER_SUCCESS);
//                        EventBus.getDefault().post(event);
//                    } else if(userResponse.getCode() == 201){
//                        UserEvent event = new UserEvent();
//                        event.setAction(UserEvent.ACTION_REGISTER_FAILED);
//                        event.setExtra(mContext.getString(R.string.login_error_4));
//                        event.setErrorCode(UserEvent.ERROR_USERNAME_EXIST);
//                        EventBus.getDefault().post(event);
//                    } else if(userResponse.getCode() == 202){
//                        UserEvent event = new UserEvent();
//                        event.setAction(UserEvent.ACTION_REGISTER_FAILED);
//                        event.setExtra(mContext.getString(R.string.login_error_3));
//                        event.setErrorCode(UserEvent.ERROR_USERNAME_EXIST);
//                        EventBus.getDefault().post(event);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
//
//    public void login(final String username, final String password) {
//        mExecutorService.submit(new Runnable() {
//            @Override
//            public void run() {
//                int checkFormatResult = Method.checkUserNameAndPswd(username, password);
//
//                //检验用户名密码格式是否正确
//                if(checkFormatResult != 0) {
//                    String message;
//                    switch (checkFormatResult){
//                        case 1:
//                            message = mContext.getString(R.string.login_error_5);
//                            break;
//                        case 2:
//                            message = mContext.getString(R.string.login_error_6);
//                            break;
//                        case 3:
//                            message = mContext.getString(R.string.login_error_7);
//                            break;
//                        case 4:
//                            message = mContext.getString(R.string.login_error_8);
//                            break;
//                        default:
//                            message = mContext.getString(R.string.login_error_2);
//                            break;
//                    }
//                    UserEvent event = new UserEvent();
//                    event.setAction(UserEvent.ACTION_LOGIN_FAILED);
//                    event.setExtra(message);
//                    event.setErrorCode(checkFormatResult);
//                    EventBus.getDefault().post(event);
//                    return;
//                }
//
//                LinkedHashMap<String, String> params = new LinkedHashMap<>();
//                params.put("loginId", username);
//                params.put("password", password);
//                httpPost httpPost = new httpPost();
//                try {
//                    String response = httpPost.requireClass(BHApplication.Address + "/user/login", params, "UTF-8");
//                    L.i(TAG, response);
//                    UserResponse userResponse = mGson.fromJson(response, UserResponse.class);
//                    if (userResponse.getCode() == Response.SUCESS_CODE) {
//                        UserManager.getInstance(mContext).login(userResponse.getUser());
//                        UserEvent event = new UserEvent();
//                        event.setAction(UserEvent.ACTION_LOGIN_SUCCESS);
//                        EventBus.getDefault().post(event);
//                    } else if (userResponse.getCode() == 201){
//                        UserEvent event = new UserEvent();
//                        event.setAction(UserEvent.ACTION_LOGIN_FAILED);
//                        event.setExtra(mContext.getResources().getString(R.string.login_error_0));
//                        event.setErrorCode(UserEvent.ERROR_USERNAME_WRONG);
//                        EventBus.getDefault().post(event);
//                    } else if (userResponse.getCode() == 202){
//                        UserEvent event = new UserEvent();
//                        event.setAction(UserEvent.ACTION_LOGIN_FAILED);
//                        event.setExtra(mContext.getResources().getString(R.string.login_error_1));
//                        event.setErrorCode(UserEvent.ERROR_PASSWORD_WRONG);
//                        EventBus.getDefault().post(event);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
//
//    public void saveUserInfo(final Data_DB_User user) {
//        mExecutorService.submit(new Runnable() {
//            @Override
//            public void run() {
//                if(UserManager.getInstance(mContext).updateCurrentUser(user)){
//                    UserEvent event = new UserEvent();
//                    event.setAction(UserEvent.ACTION_UPDATE_USERINFO_SUCCESS);
//                    EventBus.getDefault().post(event);
//                } else {
//                    UserEvent event = new UserEvent();
//                    event.setAction(UserEvent.ACTION_UPDATE_USERINFO_FAILED);
//                    EventBus.getDefault().post(event);
//                }
//            }
//        });
//    }
//
//    public void deleteUserInfo() {
//        mExecutorService.submit(new Runnable() {
//            @Override
//            public void run() {
//                UserManager.getInstance(mContext).logout();
//                UserEvent event = new UserEvent();
//                event.setAction(UserEvent.ACTION_DELETE_USERINFO_COMPLETE);
//                EventBus.getDefault().post(event);
//            }
//        });
//    }
//
//    private void pickPhotoFromGallery(Activity activity, int requestCode) {
//        Intent intent = new Intent(Intent.ACTION_PICK,
//                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);// 调用android的图库
//        activity.startActivityForResult(intent, requestCode);
//    }
//}
