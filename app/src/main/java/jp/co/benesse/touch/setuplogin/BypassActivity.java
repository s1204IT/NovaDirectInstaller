package jp.co.benesse.touch.setuplogin;

/*
  Based by: Kobold831/BypassRevokePermission
 */

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.BenesseExtension;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import jp.co.benesse.dcha.dchaservice.IDchaService;

public class BypassActivity extends Activity {

    private static final String BASE_APK = "base.apk";

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        run(this);
        finishAndRemoveTask();
    }

    @Deprecated
    public static void run(final Context context) {
        BenesseExtension.setDchaState(3);
        copyAssetsFile(context);
        context.bindService(new Intent(LoginSettingActivity.DCHA_SERVICE).setPackage(LoginSettingActivity.DCHA_PACKAGE), new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                IDchaService mDchaService = IDchaService.Stub.asInterface(iBinder);
                try {
                    mDchaService.hideNavigationBar(false);
                    mDchaService.installApp(context.getExternalFilesDir("") + "/" + BASE_APK, 2);
                    mDchaService.uninstallApp("a.a", 1);
                } catch (Exception ignored) {
                }
                context.unbindService(this);
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
            }
        }, Context.BIND_AUTO_CREATE);

        Runnable runnable = () -> BenesseExtension.setDchaState(0);
        new Handler().postDelayed(runnable, 800);

        if (!isActiveBypassService(context)) {
            context.startService(new Intent(context, BypassService.class));
        }
    }

    public static void copyAssetsFile(Context context) {
        try {
            if (new File(context.getExternalFilesDir("") + "/" + BASE_APK).exists()) {
                return;
            }
            InputStream inputStream = context.getAssets().open(BASE_APK);
            FileOutputStream fileOutputStream = new FileOutputStream(context.getExternalFilesDir("") + "/" + BASE_APK, false);
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

    @Deprecated
    public static boolean isActiveBypassService(Context context) {
        for (ActivityManager.RunningServiceInfo serviceInfo : ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningServices(Integer.MAX_VALUE)) {
            if (BypassService.class.getName().equals(serviceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}