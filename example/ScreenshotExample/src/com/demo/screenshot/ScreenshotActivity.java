package com.demo.screenshot;

import pkg.screenshot.ScreenshotPolicy;
import pkg.screenshot.ScreenshotPolicy.OnAcquireRootPermissionCallback;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ScreenshotActivity extends Activity implements ServiceConnection, OnAcquireRootPermissionCallback {

    static final String TAG = "ScreenshotActivity";

    private IScreenshotService mService;

    static final int MESSAGE_ROOT_ERROR = 1, MESSAGE_ROOT_OK = 2;

    private Button mBTNStartService, mBTNStopService, mBTNAcquireRoot;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initView();
    }

    private void initView() {
        mBTNAcquireRoot = (Button) findViewById(R.id.btn_acquire_root);
        mBTNStartService = (Button) findViewById(R.id.btn_start_screenshot);
        mBTNStopService = (Button) findViewById(R.id.btn_stop_screenshot);

        if (ScreenshotPolicy.isCurrentAppAcquiredRoot()) {
            bindScreenService();
            enableStartAndStop();
        }
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            int what = msg.what;
            if (what == MESSAGE_ROOT_OK) {
                bindScreenService();
                enableStartAndStop();
            } else if (what == MESSAGE_ROOT_ERROR) {
                showRootErrorToast();
            }
        };
    };

    void bindScreenService() {
        Intent intent = new Intent(ScreenshotService.BIND_ACTION);
        bindService(intent, this, BIND_AUTO_CREATE);
    }

    void unBindScreenService() {
        if (mService != null) {
            unbindService(this);
        }
    }
    
    void enableStartAndStop(){
        mBTNAcquireRoot.setEnabled(false);
        mBTNStartService.setEnabled(true);
        mBTNStopService.setEnabled(true);
    }

    void showRootErrorToast() {
        Toast.makeText(this, R.string.str_root_error_toast, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        Log.i(TAG, "onServiceConnected");
        mService = IScreenshotService.Stub.asInterface(service);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        Log.i(TAG, "onServiceDisconnected");
        mService = null;
    }

    public void processOnClick(View view) {
        int id = view.getId();
        try {
            if (id == R.id.btn_start_screenshot && mService != null) {
                mService.startScreenshotService();
            } else if (id == R.id.btn_stop_screenshot && mService != null) {
                mService.stopScreenshotSerice();
            } else if (id == R.id.btn_acquire_root) {
                ScreenshotPolicy.acquireRootPermission(this);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Remote ERROR", e);
        }

    }

    @Override
    public void finish() {
        unBindScreenService();
        super.finish();
    }

    @Override
    public void onAcquireRootPermission(boolean acquired) {
        int what = acquired ? MESSAGE_ROOT_OK : MESSAGE_ROOT_ERROR;
        Message msg = Message.obtain(mHandler, what);
        msg.sendToTarget();
    }

}