package com.cskd20.module.main.server;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Author：lucas on 2016/8/10 09:54
 * Email：lucas_developer@163.com
 * Description：下载服务
 */
public class DownloadServer extends Service {

    private File mDir;
    private String mFileName;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("lcuas", "创建服务");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new DownloadBind();
    }

    private OnDownloadListener mListener;

    public class DownloadBind extends Binder {

        public void setOnDownloadListener(OnDownloadListener listener) {
            mListener = listener;
        }

        //开始下载
        public void start(String url) {
            isStop = false;
            startDownload(url);
            Log.d("lucas", "开始下载任务");
        }

        //停止下载
        public void stop() {
            isStop = true;
            stopSelf();
        }
    }

    public void startDownload(String url) {
        //获取文件名和存储的本地路径
        if (TextUtils.isEmpty(url)) return;
        mFileName = url.substring(url.lastIndexOf("/")+1);
        Log.d("lucas", "当前下载任务的文件名："+mFileName);
//        mDir = getDir("apk", MODE_PRIVATE);
        mDir = new File(Environment.getExternalStorageDirectory()+"/Download/");
        new Thread(new DownloadTask(url)).start();
    }

    private int mCurrentProgress;
    private boolean isStop;

    class DownloadTask implements Runnable {
        private String mUrl;
        public DownloadTask(String url) {
            mUrl = url;
        }
        @Override
        public void run() {
            InputStream is = null;
            FileOutputStream os=null;
            try {
                if (mListener != null)
                    mListener.onStart();
                URL url = new URL(mUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setDoOutput(false);
                conn.setConnectTimeout(10 * 1000);
                conn.setReadTimeout(10 * 1000);
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("Accept-Language", "zh-CN");
                conn.setRequestProperty("Charset", "UTF-8");
                conn.setRequestProperty("Referer", url.toString());
                conn.setRequestProperty("Accept", "image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
                conn.connect();
                int length = conn.getContentLength();
                Log.d("lucas", "文件大小:" + length);
                is = conn.getInputStream();
                File apkFile = new File(mDir, mFileName);
                os = new FileOutputStream(apkFile);
                byte[] buffer = new byte[10*1024];
                int read;
                mCurrentProgress = 0;
                while ((read = is.read(buffer)) != -1 && !isStop) {
                    os.write(buffer, 0, read);
                    os.flush();
                    mCurrentProgress += read;
                    if (mListener != null)
                        mListener.onProgressUpdate((int) ((mCurrentProgress * 1.0f) / length * 100));
//                    Log.d("lucas", "当前下载进度:" + mCurrentProgress);
                }
                conn.disconnect();
                if (mListener != null)
                    mListener.onComplete(apkFile);
            } catch (IOException e) {
                e.printStackTrace();
                if (mListener != null)
                    mListener.onError(e);
            } finally {
                if (is!=null)
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                if (os!=null)
                    try {
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                stopSelf();
            }
        }
    }

    public interface OnDownloadListener {
        //获取进度，百分比
        void onProgressUpdate(int progress);
        void onStart();
        void onComplete(File apkFile);
        void onError(Exception e);
    }
}
