package jp.co.benesse.touch.setuplogin;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.Settings;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import jp.co.benesse.dcha.dchaservice.IDchaService;

public class LoginSettingActivity extends Activity {

    private static final String DCHA_PACKAGE = "jp.co.benesse.dcha.dchaservice";
    private static final String DCHA_SERVICE = DCHA_PACKAGE + ".DchaService";
    private static final String CT2S = "TAB-A03-BS";
    private static final String CT2K = "TAB-A03-BR";
    private static final String CT2L = "TAB-A03-BR2";
    private static final String CT3 = "TAB-A03-BR3";
    private static final String CTX = "TAB-A05-BD";
    private static final String CTZ = "TAB-A05-BA1";
    private static final String APK_EXT = ".apk";

    IDchaService mDchaService;

    // Based by Kobold831
    private String copyAssetsFile(String model) throws IOException{
        final String ASSET_APK = model + APK_EXT;
        final String APK_PATH = getApplicationContext().getFilesDir().getPath() + "/" + ASSET_APK;
        InputStream inputStream = getApplicationContext().getAssets().open(ASSET_APK);
        FileOutputStream fileOutputStream = new FileOutputStream(APK_PATH, false);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) >= 0) {
            fileOutputStream.write(buffer, 0, length);
        }
        fileOutputStream.close();
        inputStream.close();
        return APK_PATH;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String CT_MODEL = Build.MODEL;
        final String BC_PASSWORD_HIT_FLAG = "bc_password_hit";
        final int PASSWORD_FLAG = 1;
        final int UNDIGICHALIZE = 0;
        final int DIGICHALIZING_DL_COMPLETE = 2;
        final int DIGICHALIZED = 3;
        final int INSTALL_FLAG = 2;
        final int UNINSTALL_FLAG = 1;
        final int REBOOT_DEVICE = 0;
        final String DSW_PACKAGE = "jp.co.benesse.dcha.setupwizard";
        final String DSS_PACKAGE = "jp.co.benesse.dcha.systemsettings";
        final String DSS_ACTIVITY = DSS_PACKAGE + ".TabletInfoSettingActivity";
        final String LAUNCHER3 = "com.android.launcher3";
        final String CUSTOMIZE_TOOL = "com.saradabar.cpadcustomizetool";
        final String KOBOLD_STORE = "com.saradabar.vending";

        // DchaSystemSettings を呼び出し
        startActivity(new Intent().setClassName(DSS_PACKAGE, DSS_ACTIVITY));
        // (ループ)プログレスバー実装 //
        // 再起動時にADBの状態を保持する
        Settings.System.putInt(getContentResolver(), BC_PASSWORD_HIT_FLAG, PASSWORD_FLAG);

        // DchaService をバインド
        bindService(new Intent(DCHA_SERVICE).setPackage(DCHA_PACKAGE), new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                mDchaService = IDchaService.Stub.asInterface(iBinder);
                try {
                    // DchaState を変更
                    mDchaService.setSetupStatus(UNDIGICHALIZE);
                    // ナビゲーションバーを表示
                    mDchaService.hideNavigationBar(false);

                    // インストール部分 //
                    // CT2はバイパス必須
                    //mDchaService.installApp(copyAssetsFile("ALL"), INSTALL_FLAG);
                    if (CT_MODEL.equals(CT3) && !Build.ID.startsWith("01")) {
                        mDchaService.installApp(copyAssetsFile("CT3"), INSTALL_FLAG);
                    } else if (CT_MODEL.equals(CTX) || CT_MODEL.equals(CTZ)) {
                        mDchaService.installApp(copyAssetsFile("CTX"), INSTALL_FLAG);
                    }

                    // Launcher3 の関連付けを解除
                    mDchaService.clearDefaultPreferredApp(LAUNCHER3);
                    // 既定のランチャーを変更 (CTX/Z のみ機能)
                    mDchaService.setDefaultPreferredHomeApp(KOBOLD_STORE);
                    // DchaSetupWizard をタスクキル
                    mDchaService.removeTask(DSW_PACKAGE);
                    // 再起動
                    mDchaService.rebootPad(REBOOT_DEVICE, null);
                    // このアプリを削除
                    mDchaService.uninstallApp(getPackageName(), UNINSTALL_FLAG);
                } catch (RemoteException | IOException ignored) {
                }
                unbindService(this);
            }
            @Override
            public void onServiceDisconnected(ComponentName componentName) {
            }
        }, BIND_ADJUST_WITH_ACTIVITY);
    }
}
