package com.xugang.hwa5d4;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ASUS on 2016-09-22.
 */
public class LrcCacheUtil {

    private static String TAG = "test";
    private static LruCache<String, Bitmap> lrcCache = new LruCache<String, Bitmap>(20 * 1024 * 1024) {
        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value.getByteCount();
        }
    };

    public void putBitmap(String url) {
        final String dirPath = context.getExternalCacheDir().getAbsolutePath();
        final String key = getKey(url);
        new PictureDownloadTask(new PictureDownloadTask.PictureDownloadCallback() {
            @Override
            public void onPreExecute() {

            }

            @Override
            public void onProgressUpdate(Integer... values) {

            }

            @Override
            public Bitmap onPostExecute(Bitmap bitmap) {
                callback.getBitmap(bitmap);
                Log.e(TAG, "onPostExecute:下载完成 ");
                lrcCache.put(key, bitmap);
                boolean b = MyFileUtil.writeBitmap(context, bitmap, dirPath, key, Bitmap.CompressFormat.JPEG);
                Log.e(TAG, "onPostExecute:下载完成? " + b);
                writeBitmap(context, bitmap, dirPath, key, Bitmap.CompressFormat.JPEG);
                lrcCache.put(key, bitmap);

                return null;
            }
        }).execute(url);
    }

    public Bitmap getBitmap(final String url) {
        final String dirPath = context.getExternalCacheDir().getAbsolutePath();
        final String key = getKey(url);
        if (lrcCache.get(key) != null) {
            return lrcCache.get(key);
        } else {
            Bitmap bitmaps = MyFileUtil.readBitmap(context, dirPath, key);
            if (bitmaps != null) {
                Log.e(TAG, "getBitmap: " + bitmaps);
                lrcCache.put(key, bitmaps);
                return bitmaps;
            } else {
                new PictureDownloadTask(new PictureDownloadTask.PictureDownloadCallback() {
                    @Override
                    public void onPreExecute() {

                    }

                    @Override
                    public void onProgressUpdate(Integer... values) {

                    }

                    @Override
                    public Bitmap onPostExecute(Bitmap bitmap) {
                        callback.getBitmap(bitmap);
                        Log.e(TAG, "onPostExecute:下载完成 ");
                        lrcCache.put(key, bitmap);
                        boolean b = MyFileUtil.writeBitmap(context, bitmap, dirPath, key, Bitmap.CompressFormat.JPEG);
                        Log.e(TAG, "onPostExecute:存储是否成功? " + b);
                        return null;

                    }
                }).execute(url);
            }
        }

        return null;
    }

    public static String getKey(String url) {
        return url.substring(url.lastIndexOf("/") + 1, url.length());
    }

    public static boolean writeBitmap(Context context, Bitmap bitmap, String dirPath, String fileName, Bitmap.CompressFormat format) {
        FileOutputStream fos = null;
        try {
            //定义写出的目标文件
            File file = null;
            //创建目标文件夹
            File dirFile = createDir(dirPath);
            if (dirFile == null) {
                return false;
            }

            //创建目标文件
            file = createFile(context, dirFile, fileName);
            if (file == null) {
                return false;
            }

            //向目标文件输出内容
            fos = new FileOutputStream(file, false);
            bitmap.compress(format, 100, fos);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static File createDir(String dirPath) {
        File dirFile = new File(dirPath);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            boolean success = dirFile.mkdirs();
            if (!success) {
                return null;
            }
        }

        return dirFile;
    }

    public static File createFile(Context context, File dirFile, String fileName) {
        try {
            File file = new File(dirFile.getAbsolutePath() + File.separator + fileName);
            if (!file.exists() || file.isDirectory()) {
                boolean newFile = file.createNewFile();
                if (!newFile) {
                    return null;
                }
            }
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "创建目标文件失败！", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public static Bitmap readBitmap(Context context, String dirPath, String fileName) {
        FileInputStream fis = null;
        try {
            //定位目标文件
            boolean exists = checkIfFileExists(dirPath, fileName);
            if (!exists) {
                Toast.makeText(context, "目标文件不存在！", Toast.LENGTH_SHORT).show();
                return null;
            }

            File file = new File(dirPath + File.separator + fileName);
            //读入内容并转化为图片
            fis = new FileInputStream(file);

            return BitmapFactory.decodeStream(fis);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "异常:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static boolean checkIfFileExists(String dirPath, String fileName) {
        return new File(dirPath + File.separator + fileName).exists();

    }

    BitmapCallback callback;
    Context context;

    public LrcCacheUtil(BitmapCallback callback, Context context) {
        this.callback = callback;
        this.context = context;
    }

    interface BitmapCallback {
        void getBitmap(Bitmap bitmap);
    }

}

