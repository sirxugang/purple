package com.xugang.hwa5d4;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by idea on 2016/9/21.
 */
public class PictureDownloadTask extends AsyncTask<String, Integer, Bitmap> {

    @Override
    protected void onPreExecute() {
        if (callback != null) {
            callback.onPreExecute();
        }
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        String url = params[0];
        InputStream inputStream = null;
        ByteArrayOutputStream baos = null;
        try {
            URL imgUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) imgUrl.openConnection();
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                inputStream = connection.getInputStream();
                baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = -1;
                int current = 0;

                while ((len = inputStream.read(buffer)) != -1) {
                    baos.write(buffer, 0, len);
                    current += len;
                }

                byte[] bytes = baos.toByteArray();
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
                if (baos != null)
                    baos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        if (callback != null) {
            callback.onProgressUpdate(values);
        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (callback != null) {
            callback.onPostExecute(bitmap);
        }
    }

    PictureDownloadCallback callback;

    public PictureDownloadTask(PictureDownloadCallback callback) {
        this.callback = callback;
    }

    interface PictureDownloadCallback {
        void onPreExecute();

        void onProgressUpdate(Integer... values);

        Bitmap onPostExecute(Bitmap bitmap);
    }
}
