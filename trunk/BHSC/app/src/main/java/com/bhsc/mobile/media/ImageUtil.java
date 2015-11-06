package com.bhsc.mobile.media;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by lynn on 10/15/15.
 */
public class ImageUtil {
    public final static String FilePath = "/mnt/sdcard/bhsc/";
    public final static String ImagePath = FilePath + "img/";// 图片路径
    public final static String TempPath = FilePath + "temp/";// 临时缓存路径

    private static class SingletonHolder {
        public static ImageUtil sImageUtil = new ImageUtil();
    }

    public static ImageUtil getInstance() {
        return SingletonHolder.sImageUtil;
    }

    private LruCache<String, Bitmap> mMemoryCache;

    private ImageUtil() {
        File imgDir = new File(ImagePath);
        if (!imgDir.exists()) {
            imgDir.mkdirs();
        }
        File tempdir = new File(TempPath);
        if (!tempdir.exists()) {
            tempdir.mkdirs();
        }
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // 重写此方法来衡量每张图片的大小，默认返回图片数量。
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public Bitmap decodeSampledBitmapFromSource(String source, int reqWidth, int reqHeight) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(source, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(source, options);
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    public void loadBitmap(final String source, final ImageView imageView, final int width, final int height) {
        Bitmap bitmap = getBitmapFromMemCache(source);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final Bitmap bitmap = decodeSampledBitmapFromSource(source, width, height);
                    addBitmapToMemoryCache(source, bitmap);
                    imageView.post(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(bitmap);
                        }
                    });
                }
            }).start();
        }
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * 保存图片
     *
     * @param absolutePath 图片绝对路径
     * @param mBitmap
     * @param quality      Hint to the compressor, 0-100. 0 meaning compress for
     *                     small size, 100 meaning compress for max quality. Some
     *                     formats, like PNG which is lossless, will ignore the
     *                     quality setting
     * @throws IOException
     */
    public String savePicture(String absolutePath, Bitmap mBitmap, int quality) throws IOException {
        String absoluteFilePath = null;
        File f = new File(absolutePath);
        f.createNewFile();

        absoluteFilePath = f.getAbsolutePath();

        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

        mBitmap.compress(Bitmap.CompressFormat.PNG, quality, fOut);
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
        return absoluteFilePath;
    }

    public boolean copyPicture(Context context, Uri source, String dest) {
        boolean result = false;
        int bytesum = 0;
        int byteread = 0;
        File destFile = new File(dest);
        String scheme = source.getScheme();
        if (ContentResolver.SCHEME_CONTENT.equals(scheme)
                || ContentResolver.SCHEME_FILE.equals(scheme)) {
            InputStream inStream = null;
            try {
                inStream = context.getContentResolver().openInputStream(source);
                if (!destFile.exists()) {
                    result = destFile.createNewFile();
                }
                if (result) {
                    FileOutputStream fs = new FileOutputStream(dest);
                    byte[] buffer = new byte[1024];
                    while ((byteread = inStream.read(buffer)) != -1) {
                        bytesum += byteread; //字节数 文件大小
                        System.out.println(bytesum);
                        fs.write(buffer, 0, byteread);
                    }
                    inStream.close();
                    fs.flush();
                    fs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                result = false;
            }
        }
        return result;
    }

    private boolean isHaveSdcard() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }
}
