package com.bhsc.mobile.media;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by lynn on 10/15/15.
 */
public class ImageUtil {
    private static final String TAG = ImageUtil.class.getSimpleName();

    public final static String FilePath = "/mnt/sdcard/bhsc/";
    public final static String ImagePath = FilePath + "img/";// 图片路径
    public final static String TempPath = FilePath + "temp/";// 临时缓存路径

    public final static int PICTURE_MAX_WIDTH = 1024;
    public final static int PICTURE_MAX_HEIGHT = 1024;

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

    public static Bitmap decodeSampledBitmapFromSource(String source, int reqWidth, int reqHeight) {
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

    /**
     * 拷贝图片
     * @param source
     * @param dest
     * @return
     */
    public boolean copyPicture(String source, String dest) {
        File sourceFile = new File(source);
        if (!sourceFile.exists()) {
            return false;
        }
        try {
            File destFile = new File(dest);
            if (!destFile.exists()) {
                destFile.createNewFile();
            }
            if (!destFile.exists()) {
                return false;
            }
            FileInputStream fileInputStream = new FileInputStream(sourceFile);
            FileOutputStream fileOutputStream = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024];
            int byteCount;
            while ((byteCount = fileInputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, byteCount);
            }
            fileInputStream.close();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean isHaveSdcard() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    public static String resolvePhotoFromIntent(final Context ctx, final Intent data, final String dir) {
        if (ctx == null || data == null || dir == null) {
            Log.e(TAG, "resolvePhotoFromIntent fail, invalid argument");
            return null;
        }

        String filePath = null;

        final Uri uri = Uri.parse(data.toURI());
        Cursor cursor = ctx.getContentResolver().query(uri, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            try {
                cursor.moveToFirst();
                String imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
                String orientation = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.ORIENTATION));
                Log.e(TAG, "orition: " + orientation);
                if(!TextUtils.isEmpty(imagePath)){
                    Bitmap bitmap = decodeSampledBitmapFromSource(imagePath, PICTURE_MAX_WIDTH, PICTURE_MAX_HEIGHT);
                    if(!TextUtils.isEmpty(orientation)){
                        int angle = Integer.parseInt(orientation);
                        if(angle != 0){
                            Matrix matrix = new Matrix();
                            int width = bitmap.getWidth();
                            int height = bitmap.getHeight();
                            matrix.setRotate(angle);
                            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
                        }
                    }
                    final String fileName = "IMG" + System.currentTimeMillis() + ".png";
                    filePath = dir + fileName;
                    final File file = new File(filePath);
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, bufferedOutputStream);
                    bitmap.recycle();
                    bufferedOutputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            cursor.close();
        } else if (data.getData() != null) {
            filePath = data.getData().getPath();
            if (!(new File(filePath)).exists()) {
                filePath = null;
            }
            Log.d(TAG, "photo file from data, path:" + filePath);

        } else if (data.getAction() != null && data.getAction().equals("inline-data")) {

            try {
                final String fileName = "IMG" + System.currentTimeMillis() + ".png";
                filePath = dir + fileName;
                final Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                final File file = new File(filePath);
                if (!file.exists()) {
                    file.createNewFile();
                }
                BufferedOutputStream out;
                out = new BufferedOutputStream(new FileOutputStream(file));
                final int cQuality = 100;
                bitmap.compress(Bitmap.CompressFormat.PNG, cQuality, out);
                out.close();
                Log.d(TAG, "photo image from data, path:" + filePath);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        } else {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
            Log.e(TAG, "resolve photo from intent failed");
            return null;
        }
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
        return filePath;
    }
}
