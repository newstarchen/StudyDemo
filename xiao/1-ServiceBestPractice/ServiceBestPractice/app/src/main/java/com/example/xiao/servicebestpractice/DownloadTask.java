package com.example.xiao.servicebestpractice;

import android.app.DownloadManager;
import android.app.VoiceInteractor;
import android.os.AsyncTask;
import android.os.Environment;
import android.service.voice.VoiceInteractionSession;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Random;
import java.util.RandomAccess;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by jason on 2018/12/4.
 */
public class DownloadTask extends AsyncTask<String, Integer, Integer> {

    public static final int TYPE_SUCCESS = 0;
    public static final int TYPE_FAILED = 1;
    public static final int TYPE_PAUSED = 2;
    public static final int TYPE_CANCELED = 3;

    private DownloadListener listener;

    private boolean isCanceled = false;

    private boolean isPaused = false;

    private int lastProgress;

    public DownloadTask(DownloadListener listener) {
        this.listener = listener;
    }

    @Override
    protected Integer doInBackground(String... params) {
        InputStream is = null;
        RandomAccessFile savedFile = null;
        File file = null;
        try {
            long downloadLength = 0;
            String downloadUrl = params[0];
            String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
            String direction = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath();
            file = new File(direction + fileName);
            if (file.exists()) {
                downloadLength = file.length();
            }
            long contentLength = getContentLength(downloadUrl);
            if (contentLength == 0) {
                return TYPE_FAILED;
            } else if (contentLength == downloadLength) {
                return TYPE_SUCCESS;
            }
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .addHeader("RANGE", "bytes=" + downloadLength + "-")
                .url(downloadUrl)
                .build();
        Response response = client.newCall(request).execute();
        if (response != null) {
            is = response.body().byteStream();
            savedFile = new RandomAccessFile(file, "rw");
            savedFile.seek(downloadLength);
            byte[] b = new byte[1024];
            int total = 0;
            int len;
            while ((len = is.read(b)) != -1) {
                if (isCanceled) {
                    return TYPE_CANCELED;
                } else if (isPaused) {
                    return TYPE_PAUSED;
                } else {
                    total += len;
                    savedFile.write(b, 0, len);
                    int progress = (int) ((total + downloadLength) * 100 / contentLength);
                    publishProgress(progress);
                }
            }
            response.body().close();
            return TYPE_SUCCESS;
        }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        try {
            if (is != null) {
                is.close();
            }
            if (savedFile != null) {
                savedFile.close();
            }
            if (isCanceled && file != null) {
                file.delete();
            }
        }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return TYPE_FAILED;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress = values[0];
        if (progress > lastProgress) {
            listener.onProgress(progress);
        }
    }

    @Override
    protected void onPostExecute(Integer status) {
        switch (status) {
            case TYPE_SUCCESS:
                listener.onSuccess();
                break;
            case TYPE_FAILED:
                listener.onFailed();
                break;
            case TYPE_PAUSED:
                listener.onPaused();
                break;
            case TYPE_CANCELED:
                listener.onCanceled();
                break;
            default:
                break;
        }
    }

    public void pauseDownload() {
        isPaused = true;
    }

    public  void cancelDownload() {
        isCanceled = true;
    }

    private long getContentLength(String downloadUrl) throws IOException {
        OkHttpClient client  = new OkHttpClient();
        Request request = new Request.Builder()
                .url(downloadUrl)
                .build();
        Response response = client.newCall(request).execute();
        if (response != null && response.isSuccessful()) {
            long contentLength = response.body().contentLength();
            response.body().close();
            return contentLength;
        }
        return 0;
    }
}
