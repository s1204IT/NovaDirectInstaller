package jp.co.benesse.touch.setuplogin;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import static android.app.admin.DevicePolicyManager.*;
import static android.content.pm.PackageManager.*;
import static android.util.TypedValue.*;
import static android.view.Gravity.*;
import static android.widget.LinearLayout.LayoutParams.*;

import static android.os.BenesseExtension.COUNT_DCHA_COMPLETED_FILE;
import jp.co.benesse.dcha.dchaservice.IDchaService;

public class LoginSettingActivity extends Activity {

    IDchaService mDchaService;

    public void onBackPressed() {
    // バックキーを無効化
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final int DCHA_STATE = 0;
        final String msg = "お待ちください...";
        final float fontSize = 50.0F;
        final String DCHA_PACKAGE = "jp.co.benesse.dcha.dchaservice";
        final String DCHA_SERVICE = DCHA_PACKAGE + ".DchaService";
        final String QUICK_STEP = "com.android.launcher3";
        final String NOVA_PACKAGE = "com.teslacoilsw.launcher";
        // SDカードのルートに"NovaLauncher.apk"を置く
        final String NOVA_SD_PATH = System.getenv("SECONDARY_STORAGE") + "/NovaLauncher.apk";
        final String NOVA_LOCAL_PATH = "/storage/emulated/0/Download/NovaLauncher.apk";

        // アプリの表示
        LinearLayout linearLayout = new LinearLayout(getApplicationContext());
        TextView textView = new TextView(getApplicationContext());
        linearLayout.setGravity(CENTER);
        textView.setTextSize(COMPLEX_UNIT_SP, fontSize);
        textView.setText(msg);
        linearLayout.addView(textView, new LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        setContentView(linearLayout);

        // ファイルの存在確認
        if (COUNT_DCHA_COMPLETED_FILE.exists()) {
            // 別アクティビティを有効化
            getPackageManager().setComponentEnabledSetting(new ComponentName(this, Dev.class), COMPONENT_ENABLED_STATE_ENABLED, DONT_KILL_APP);
        }
        // 端末管理者を要求(任意)
        startActivity(new Intent(ACTION_ADD_DEVICE_ADMIN).putExtra(EXTRA_DEVICE_ADMIN, new ComponentName(this, DeviceAdminReceiver.class)).putExtra(EXTRA_ADD_EXPLANATION, "｢端末管理アプリ｣を有効にする事で､ DchaService による自動アンインストールをブロックします｡"));

        // DchaService をバインド
        bindService(new Intent(DCHA_SERVICE).setPackage(DCHA_PACKAGE), new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                mDchaService = IDchaService.Stub.asInterface(iBinder);
                try {
                    // DchaState を変更
                    mDchaService.setSetupStatus(DCHA_STATE);
                    // ナビゲーションバーを表示
                    mDchaService.hideNavigationBar(false);
                    // SDカードからローカルにAPKをコピー
                    mDchaService.copyFile(NOVA_SD_PATH, NOVA_LOCAL_PATH);
                    // APKをインストール
                    mDchaService.installApp(NOVA_LOCAL_PATH, 2);
                    // 既定のランチャーを変更
                    mDchaService.setDefaultPreferredHomeApp(NOVA_PACKAGE);
                    // QuickStepを停止
                    mDchaService.removeTask(QUICK_STEP);
                    // QuickStepの関連付けを解除
                    mDchaService.clearDefaultPreferredApp(QUICK_STEP);
                    // ローカルにコピーしたAPKを削除
                    mDchaService.deleteFile(NOVA_LOCAL_PATH);
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
