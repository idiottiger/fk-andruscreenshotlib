package com.demo.screenshot;

import java.io.File;
import java.io.IOException;

import pkg.screenshot.Screenshot;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;

public class ScreenshotService extends Service {

    public static final String BIND_ACTION = "bind.action.SCREEN_SHOT_SERVICE";

    static final String TAG = "ScreenshotService";

    private InnerScreenshotThread mScreenshotThread;

    static final String BASE_PATH = Environment.getExternalStorageDirectory() + "/screens_shot";

    static boolean SAVE_TO_JSON = false;

    final IScreenshotService.Stub mBinder = new IScreenshotService.Stub() {

        @Override
        public void stopScreenshotSerice() throws RemoteException {
            stopScreenshotServiceImp();
        }

        @Override
        public void startScreenshotService() throws RemoteException {
            startScreenshotServiceImp();
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind");
        if (BIND_ACTION.equals(intent.getAction())) {
            return mBinder;
        }
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "onUnbind");
        stopScreenshotServiceImp();
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        super.onDestroy();
    }

    void startScreenshotServiceImp() {
        if (mScreenshotThread == null) {
            mScreenshotThread = new InnerScreenshotThread();
            mScreenshotThread.start();
            Log.i(TAG, "startScreenshotService");
        }
    }

    void stopScreenshotServiceImp() {
        if (mScreenshotThread != null) {
            mScreenshotThread.markItStop();
            mScreenshotThread = null;
            Log.i(TAG, "stopScreenshotService");
        }
    }

    class InnerScreenshotThread extends Thread {

        volatile boolean isStoped = false;

        File mFile;

        @Override
        public void run() {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
            setName("screen_shot");
            Screenshot screenshot = Screenshot.getInstance();
            while (!isStoped) {
                int result = screenshot.takeScreenshot();
                Log.i(TAG, "screen shot result:" + result);
                if (result == Screenshot.SCREENSHOT_OK) {
                    try {
                        Bitmap map = screenshot.generateScreenshotBitmap();
                        String path = getPath();
                        if (map != null) {
                            ScreenshotUtils.saveBitmap2File(map, path);
                            Log.i(TAG, "saveBitmap2File path[" + path + "] OK");
                            // map.recycle();
                            map = null;
                        }
                    } catch (OutOfMemoryError e) {
                        Log.e(TAG, "Screenshot Thread generate Screenshot error:", e);
                    } catch (IOException e) {
                        Log.e(TAG, "EXCEPTION:", e);
                    }
                } else if (result == Screenshot.SCREENSHOT_ARE_SAME) {
                    Log.i(TAG, "twice are some, so ingore this");
                }
            }
            screenshot.release();
        }

        @Override
        public synchronized void start() {
            super.start();
        }

        public void markItStop() {
            isStoped = true;
            Log.i(TAG, "make screenshot stoped");
        }

        private String getPath() {
            if (mFile == null) {
                mFile = new File(BASE_PATH);
            }
            if (!mFile.exists()) {
                mFile.mkdirs();
            }
            // get system time
            String newPath = BASE_PATH + "/" + System.currentTimeMillis() + (SAVE_TO_JSON ? ".jon" : ".jpg");
            return newPath;
        }
    }

}
