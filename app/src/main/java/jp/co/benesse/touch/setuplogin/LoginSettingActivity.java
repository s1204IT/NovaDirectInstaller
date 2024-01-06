package jp.co.benesse.touch.setuplogin;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import static android.content.pm.PackageManager.*;
import static android.os.Build.MODEL;
import static android.provider.Settings.System.putInt;

import java.io.File;

import jp.co.benesse.dcha.dchaservice.IDchaService;

public class LoginSettingActivity extends Activity {

    IDchaService mDchaService;

    public void onBackPressed() {
        // バックキーを無効化
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String BC_PASSWORD_HIT_FLAG = "bc_password_hit";
        final int PASSWORD_FLAG = 1;
        final String DCHA_PACKAGE = "jp.co.benesse.dcha.dchaservice";
        final String DCHA_SERVICE = DCHA_PACKAGE + ".DchaService";
        final int UNDIGICHALIZE = 0;
        final int DIGICHALIZING_DL_COMPLETE = 2;
        final int DIGICHALIZED = 3;
        final int INSTALL_FLAG = 2;
        final int REBOOT_DEVICE = 0;
        final String DSS_PACKAGE = "jp.co.benesse.dcha.systemsettings";
        final String DSS_ACTIVITY = DSS_PACKAGE + ".TabletInfoSettingActivity";
        final String LAUNCHER3 = "com.android.launcher3";
        final String NOVA_PACKAGE = "com.teslacoilsw.launcher";
        final String LOCAL_PATH = "/storage/emulated/0/Download/";
        final String SD_PATH = "/storage/sdcard1/";
        final String CT3 = "TAB-A03-BR3";
        final String CTZ = "TAB-A05-BA1";
        // SDカードのルートに NovaLauncher のAPKを置く(CT3とCTX/Zで分ける)
        final String NOVA6_SD_PATH = SD_PATH + "NovaLauncher_6.2.19.apk";
        final String NOVA7_SD_PATH = SD_PATH + "NovaLauncher_7.0.57.apk";
        final String NOVA_LOCAL_PATH = LOCAL_PATH + "NovaLauncher.apk";
        // Googleサービス
        final String[] GApps = {
            "GoogleServicesFramework",
            "GmsCore",
            "Phonesky",
            "GoogleCalendarSyncAdapter",
            "GoogleContactsSyncAdapter"
        };
        final String APK_EXT = ".apk";

        // DchaSystemSettings を呼び出し (外部なら何でも良い)
        startActivity(new Intent().setClassName(DSS_PACKAGE, DSS_ACTIVITY));
        // 再起動時にADBの状態を保持する
        putInt(getContentResolver(), BC_PASSWORD_HIT_FLAG, PASSWORD_FLAG);

        // DchaService をバインド
        bindService(new Intent(DCHA_SERVICE).setPackage(DCHA_PACKAGE), new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                mDchaService = IDchaService.Stub.asInterface(iBinder);
                try {
                    // DchaState を変更
                    mDchaService.setSetupStatus(DIGICHALIZING_DL_COMPLETE);
                    // ナビゲーションバーを表示
                    mDchaService.hideNavigationBar(false);

                    // CT3 のみ直接インストール
                    if (MODEL.equals(CT3)) {
                        // アクティビティを無効化
                        getPackageManager().setComponentEnabledSetting(new ComponentName(getApplicationContext(), DchaStateChanger.class), COMPONENT_ENABLED_STATE_DISABLED, DONT_KILL_APP);
                        getPackageManager().setComponentEnabledSetting(new ComponentName(getApplicationContext(), DevelopmentOptions.class), COMPONENT_ENABLED_STATE_DISABLED, DONT_KILL_APP);
                        getPackageManager().setComponentEnabledSetting(new ComponentName(getApplicationContext(), DeviceAdminReceiver.class), COMPONENT_ENABLED_STATE_DISABLED, DONT_KILL_APP);
                        // 有効化
                        getPackageManager().setComponentEnabledSetting(new ComponentName(getApplicationContext(), DchaStateChanger3.class), COMPONENT_ENABLED_STATE_ENABLED, DONT_KILL_APP);
                        getPackageManager().setComponentEnabledSetting(new ComponentName(getApplicationContext(), DevelopmentOptions3.class), COMPONENT_ENABLED_STATE_ENABLED, DONT_KILL_APP);
                        // APKをインストール
                        mDchaService.installApp(NOVA6_SD_PATH, INSTALL_FLAG);

                    // CTX/Z は内部にコピーしてからインストール
                    } else {
                        // SDカードからローカルにAPKをコピー
                        mDchaService.copyFile(NOVA7_SD_PATH, NOVA_LOCAL_PATH);
                        // APKをインストール
                        mDchaService.installApp(NOVA_LOCAL_PATH, INSTALL_FLAG);
                        // コピーしたAPKを削除
                        new File(NOVA_LOCAL_PATH).delete();

                        // CTZ は GMS もインストール
                        if (MODEL.equals(CTZ)) {
                            // アクティビティを無効化
                            getPackageManager().setComponentEnabledSetting(new ComponentName(getApplicationContext(), DchaCopyFile.class), COMPONENT_ENABLED_STATE_DISABLED, DONT_KILL_APP);
                            getPackageManager().setComponentEnabledSetting(new ComponentName(getApplicationContext(), DchaInstallApp.class), COMPONENT_ENABLED_STATE_DISABLED, DONT_KILL_APP);
                            getPackageManager().setComponentEnabledSetting(new ComponentName(getApplicationContext(), DevelopmentOptions.class), COMPONENT_ENABLED_STATE_DISABLED, DONT_KILL_APP);
                            // 有効化
                            getPackageManager().setComponentEnabledSetting(new ComponentName(getApplicationContext(), DchaStateReceiver.class), COMPONENT_ENABLED_STATE_ENABLED, DONT_KILL_APP);
                            // DchaState を 3 にする
                            mDchaService.setSetupStatus(DIGICHALIZED);
                            // Googleサービス
                            for (String pkg : GApps) {
                                mDchaService.copyFile(SD_PATH + pkg + APK_EXT, LOCAL_PATH + pkg + APK_EXT);
                                mDchaService.installApp(LOCAL_PATH + pkg + APK_EXT, INSTALL_FLAG);
                                new File(LOCAL_PATH + pkg + APK_EXT).delete();
                            }
                        }
                    }
                    // Launcher3 の関連付けを解除
                    mDchaService.clearDefaultPreferredApp(LAUNCHER3);
                    // 既定のランチャーを変更 (CTX/Z のみ機能)
                    mDchaService.setDefaultPreferredHomeApp(NOVA_PACKAGE);
                    // DchaState を 0 にする
                    if (!MODEL.equals(CTZ)) {
                        mDchaService.setSetupStatus(UNDIGICHALIZE);
                    }
                    // このアクティビティを無効化
                    getPackageManager().setComponentEnabledSetting(new ComponentName(getApplicationContext(), LoginSettingActivity.class), COMPONENT_ENABLED_STATE_DISABLED, DONT_KILL_APP);
                    // 再起動
                    mDchaService.rebootPad(REBOOT_DEVICE, null);
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
