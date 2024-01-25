package jp.co.benesse.touch.setuplogin;


import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import jp.co.benesse.dcha.dchaservice.IDchaService;

/*
  Based by: Kobold831/BypassRevokePermission
 */

public class BypassService extends Service {

    IDchaService mDchaService;
    final int DelayMillis = 800;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        bindService(new Intent("jp.co.benesse.dcha.dchaservice.DchaService").setPackage("jp.co.benesse.dcha.dchaservice"), new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                mDchaService = IDchaService.Stub.asInterface(iBinder);
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
            }
        }, Context.BIND_AUTO_CREATE);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    mDchaService.setSetupStatus(3);
                } catch (RemoteException ignored) {
                }
            }
        };
        new Handler().postDelayed(runnable, DelayMillis);

        copyAssetsFile();

        Runnable runnable2 = new Runnable() {
            @Override
            public void run() {
                try {
                    mDchaService.hideNavigationBar(false);
                    mDchaService.installApp(Environment.getExternalStorageDirectory() + "/" + "base.apk", 2);
                    mDchaService.uninstallApp("a.a", 1);
                } catch (RemoteException ignored) {
                }
            }
        };
        new Handler().postDelayed(runnable2, DelayMillis);

        Runnable runnable3 = new Runnable() {
            @Override
            public void run() {
                try {
                    mDchaService.setSetupStatus(0);
                } catch (RemoteException ignored) {
                }
            }
        };
        new Handler().postDelayed(runnable3, DelayMillis);
        stopSelf();
        return START_NOT_STICKY;
    }

    private void copyAssetsFile() {
        try {
            InputStream inputStream = getAssets().open("base.apk");
            FileOutputStream fileOutputStream = new FileOutputStream(Environment.getExternalStorageDirectory() + "/" + "base.apk", false);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) >= 0) {
                fileOutputStream.write(buffer, 0, length);
            }
            fileOutputStream.close();
            inputStream.close();
        } catch (IOException ignored) {
        }
    }
}