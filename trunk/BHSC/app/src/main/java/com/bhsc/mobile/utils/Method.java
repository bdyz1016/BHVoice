package com.bhsc.mobile.utils;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;

@SuppressLint({"WorldReadableFiles", "WorldWriteableFiles", "SimpleDateFormat"})
public class Method {
    private final static String currentImageFilePath = "/mnt/sdcard/bhsc";// 存放图像路径

    /**
     * 检测app是否显示
     * 2014年11月21日上午10:13:58
     *
     * @param context
     * @return
     * @author chen
     */
    public static boolean isRunningForeground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        try {
            ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
            String currentPackageName = cn.getPackageName();
            if (!TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(context.getPackageName())) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }

        return false;
    }

    /**
     * 判断是不是手机号
     *
     * @param mobiles
     * @return
     * @author 赵玮
     * 2014-12-2下午3:09:03
     */
    public static boolean isMobileNO(String mobiles) {

        Pattern p = Pattern.compile("^((13[0-9])|(15[0-9])|(18[0-9])|(17[0-9])|(14[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);

        return m.matches();

    }


    /**
     * Email 验证
     *
     * @param email
     * @return
     */
    public static boolean checkEmail(String email) {
        String str = "^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * password 验证密码长度在6到128位
     *
     * @param password
     * @return
     */
    public static boolean checkPassword(String password) {
        String str = "^([0-9a-zA-Z]){6,128}$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(password);
        return m.matches();
    }

    /**
     * password 验证密码必须有数字大小写字母
     *
     * @param password
     * @return
     */
    public static boolean checkPassword_word(String password) {
        String str = "^(?![0-9a-z]+$)(?![0-9A-Z]+$)(?![a-zA-Z]+$)[a-zA-Z0-9]+$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(password);
        return m.matches();
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * 网络下载用户头像的方法
     *
     * @param imageUrl  用户头像url
     * @param imageName 要保存的头像名 = 用户头像保存时的TS
     * @return
     */
    public static boolean getNetWorkPicture(String imageUrl, String imageName) {
        boolean isOK = false;
        try {
            byte[] data = getImage(imageUrl);
            if (data != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);// bitmap
                saveMyBitmap(imageName, bitmap);
                isOK = true;
            } else {
                isOK = false;
            }
        } catch (Exception ex) {
            isOK = false;
        }
        return isOK;
    }

    /**
     * 通用下载的方法
     *
     * @param path
     * @return
     */
    public static byte[] getImage(String path) {
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream inStream = conn.getInputStream();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return readStream(inStream);
            }
        } catch (Exception ex) {
        }
        return null;
    }

    /**
     * 处理下载流的方法
     *
     * @param inStream
     * @return
     * @throws Exception
     */
    public static byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();
        return outStream.toByteArray();
    }

    /**
     * 保存图像
     */
    public static void saveMyBitmap(String bitName, Bitmap mBitmap) throws IOException {

        if (isHaveSdcard()) {
            createImageSaveDirectory();
        }
        File f = new File(currentImageFilePath + "/" + bitName + ".png");
//		Log.d("tutu", "saveBitmap:" + currentImageFilePath + "/" + bitName + ".png");
        f.createNewFile();
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

        mBitmap.compress(CompressFormat.PNG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 确认是否有SD卡
     *
     * @return
     */
    public static boolean isHaveSdcard() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 确认是否有本路径
     *
     * @return
     */
    public static void createImageSaveDirectory() {

        File path = new File(currentImageFilePath);
        if (!path.exists())
            path.mkdirs();
    }

    /**
     * 取时间戳
     */
    public static long getTS() {
        long val = System.currentTimeMillis() / 1000;
        return val;
    }

    /**
     * 图像转base64编码
     */
    public static String PicPathToBase64(String picturename) {
        String stringBase64 = "";
        // 将Bitmap转换成字符串
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(currentImageFilePath + "/" + picturename);
            ByteArrayOutputStream bStream = new ByteArrayOutputStream();
            bitmap.compress(CompressFormat.PNG, 100, bStream);
            byte[] bytes = bStream.toByteArray();
            stringBase64 = Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (Exception ex) {
            return stringBase64;
        }
        return stringBase64;
    }

    /**
     * 读取图像
     */
    public static Bitmap readMyBitmap(String fileName) throws IOException {
        Bitmap bp = null;
        if (isHaveSdcard()) {
            createImageSaveDirectory();
            FileInputStream fis = new FileInputStream(currentImageFilePath + "/" + fileName);
            bp = BitmapFactory.decodeStream(fis);
        }
        return bp;
    }

    public static String intToString(int i) {
        String str = "";
        switch (i) {
            case 0:
                str = "Jan";
                break;
            case 1:
                str = "Jan";
                break;
            case 2:
                str = "Feb";
                break;
            case 3:
                str = "Mar";
                break;
            case 4:
                str = "Apr";

                break;
            case 5:
                str = "May";
                break;
            case 6:
                str = "Jun";
                break;

            case 7:
                str = "Jul";
                break;
            case 8:
                str = "Aug";
                break;
            case 9:
                str = "Sep";
                break;
            case 10:
                str = "Oct";
                break;
            case 11:
                str = "Nov";
                break;
            case 12:
                str = "Dec";
                break;
            case 13:
                break;
        }
        return str;
    }

    /**
     * @return 判断是否有sd卡
     */
    public static boolean isSdcardExit() {
        boolean sdCardExist = false;
        sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        return sdCardExist;
    }

    /**
     * 获取sd卡根目录
     */
    public static String getSDPath() {
        File sdDir = null;
        sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
        return sdDir.toString();
    }


    /**
     * @return 获取当前系统语言 （1.en, 2.zh） author_GYL 2013-7-12 下午5:08:08
     */
    public static int getLanguage(String language) {
        int languageInt = 0;

        if (language.equals("en")) {
            languageInt = 1;
        } else if (language.equals("zh")) {
            languageInt = 2;
        }

        return languageInt;
    }

    /**
     * @return 获取手机可使用最大内存 author_GYL 2013-8-12 上午9:22:54
     */
    public int getMaxMemory() {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
//		Log.d("asd", "Max memory is " + maxMemory + "KB");
        return maxMemory;
    }

    public static void saveMyBitmap_low(String bitName, Bitmap mBitmap)
            throws IOException {

        if (isHaveSdcard()) {
            createImageSaveDirectory();
        }
        File f = new File(currentImageFilePath + "/" + bitName + ".png");
        // Log.d("log", "saveBitmap:" + currentImageFilePath + "/" + bitName +
        // ".png");
        f.createNewFile();
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

        mBitmap.compress(Bitmap.CompressFormat.PNG, 30, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String createDataId() {
        return UUID.randomUUID().toString();
    }

    private static final long DAY = 24 * 60 * 60 * 1000;
    private static final long HOUR = 60 * 60 * 1000;
    private static final long MINUTE = 60 * 1000;

    private static final String HOUR_STRING = "小时前";
    private static final String MINUTE_STRING = "分钟前";
    private static final String JUST_STRING = "刚刚";

    public static String getTime(long ts) {
        String time;
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("MM-dd hh:mm");
        long currentTime = System.currentTimeMillis();
        long interval = currentTime - ts * 1000;
        if (interval < DAY) {
            if (interval > HOUR) {
                time = Math.floor(interval / HOUR) + HOUR_STRING;
            } else if (interval > MINUTE) {
                time = Math.floor(interval / MINUTE) + MINUTE_STRING;
            } else {
                time = JUST_STRING;
            }
        } else {
            time = mSimpleDateFormat.format(new Date(ts * 1000));
        }
        return time;
    }

    /**
     * 验证账号密码的合法性,和是否Guest流程
     * 账号验证:不为空,正则,长度小于129
     * 密码验证:不为空,长度大于6小于129
     *
     * @param email
     * @param passWord
     * @return 验证结果
     * 成功:  	0
     * 失败:	1:邮箱为空	 2:邮箱格式(正则+长度小于129)		3,密码为空	4,密码格式(长度大于6小于129)
     */
    public static int checkUserNameAndPswd(String email, String passWord) {
        int result = 0;
        //邮箱
        if (email.trim().equals("")) {
            return result = 1;
        } else if (!checkEmail(email) && email.length() < 129) {
            return result = 2;
        } else if (passWord.trim().equals("")) {
            return result = 3;
        } else if (passWord.length() > 128 || passWord.length() < 6) {
            return result = 4;
        } else {
            //正常
        }

        return result;
    }
}