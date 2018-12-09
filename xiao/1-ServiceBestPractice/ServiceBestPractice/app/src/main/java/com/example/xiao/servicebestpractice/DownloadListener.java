package com.example.xiao.servicebestpractice;

/**
 * Created by jason on 2018/12/4.
 */
public interface DownloadListener {
    void onProgress(int progress);

    void onSuccess();

    void onFailed();

    void onPaused();

    void onCanceled();

}
