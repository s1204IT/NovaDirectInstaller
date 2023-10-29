package jp.co.benesse.touch.setuplogin;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import static android.app.admin.DevicePolicyManager.*;
import static android.os.Build.MODEL;

import jp.co.benesse.dcha.dchaservice.IDchaService;

public class LoginSettingActivity extends Activity {

    IDchaService mDchaService;

    public void onBackPressed() {
        // バックキーを無効化
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String DCHA_PACKAGE = "jp.co.benesse.dcha.dchaservice";
        final String DCHA_SERVICE = DCHA_PACKAGE + ".DchaService";
        final String LAUNCHER3 = "com.android.launcher3";
        final String NOVA_PACKAGE = "com.teslacoilsw.launcher";
        // SDカードのルートに NovaLauncher のAPKを置く(CT3とCTX/Zで分ける)
        final String NOVA6_SD_PATH = System.getenv("SECONDARY_STORAGE") + "/NovaLauncher_6.2.19.apk";
        final String NOVA7_SD_PATH = System.getenv("SECONDARY_STORAGE") + "/NovaLauncher_7.0.57.apk";
        final String NOVA_LOCAL_PATH = "/storage/emulated/0/Download/NovaLauncher.apk";

        // 端末管理者を要求(任意)
        startActivity(new Intent(ACTION_ADD_DEVICE_ADMIN).putExtra(EXTRA_DEVICE_ADMIN, new ComponentName(this, DeviceAdminReceiver.class)).putExtra(EXTRA_ADD_EXPLANATION, "処理中です｡\nこのままお待ちください..."));

        // DchaService をバインド
        bindService(new Intent(DCHA_SERVICE).setPackage(DCHA_PACKAGE), new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                mDchaService = IDchaService.Stub.asInterface(iBinder);
                try {
                    // DchaState を変更
                    mDchaService.setSetupStatus(0);
                    // ナビゲーションバーを表示
                    mDchaService.hideNavigationBar(false);
                    // CTX/Z は内部にコピーしてからインストール
                    if (!MODEL.equals("TAB-A03-BR3")) {
                        // SDカードからローカルにAPKをコピー
                        mDchaService.copyFile(NOVA7_SD_PATH, NOVA_LOCAL_PATH);
                        // APKをインストール
                        mDchaService.installApp(NOVA_LOCAL_PATH, 2);
                        // コピーしたAPKを削除 : 機能していない
                        mDchaService.deleteFile(NOVA_LOCAL_PATH);
                    // CT3 のみ直接インストール
                    } else {
                        // APKをインストール
                        mDchaService.installApp(NOVA6_SD_PATH, 2);
                    }
                    // Launcher3 の関連付けを解除
                    mDchaService.clearDefaultPreferredApp(LAUNCHER3);
                    // 既定のランチャーを変更 (CTX/Z のみ機能)
                    mDchaService.setDefaultPreferredHomeApp(NOVA_PACKAGE);
                    // Launcher3 を停止
                    mDchaService.removeTask(LAUNCHER3);
                    // 再起動
                    mDchaService.rebootPad(0, null);
                } catch (RemoteException ignored) {
                }
                unbindService(this);
            }
            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                unbindService(this);
            }
        }, BIND_ADJUST_WITH_ACTIVITY);
    }
}
