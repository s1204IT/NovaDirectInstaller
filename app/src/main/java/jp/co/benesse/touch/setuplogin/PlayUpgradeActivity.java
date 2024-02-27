package jp.co.benesse.touch.setuplogin;

import static android.content.pm.PackageManager.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.widget.Toast;

public class PlayUpgradeActivity extends Activity {
    private static final String GmsCore = "com.google.android.gms";
    private static final String Phonesky = "com.android.vending";
    private static final int GMS_MIN_VER = 18719037;
    private static final int FINSKY_MIN_VER = 82195010;

    public void makeToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    public void ofDisable(Class<?> cls) {
        getPackageManager().setComponentEnabledSetting(new ComponentName(this, cls), COMPONENT_ENABLED_STATE_DISABLED, DONT_KILL_APP);
    }
    public void ofEnable(Class<?> cls) {
        getPackageManager().setComponentEnabledSetting(new ComponentName(this, cls), COMPONENT_ENABLED_STATE_ENABLED, DONT_KILL_APP);
    }

    @Deprecated
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Intent intent;
        try {
            final int GMS_VER = getPackageManager().getPackageInfo(GmsCore, 0).versionCode;
            final int FINSKY_VER = getPackageManager().getPackageInfo(Phonesky, 0).versionCode;
            if (GMS_VER > GMS_MIN_VER && FINSKY_VER > FINSKY_MIN_VER) {
                new AlertDialog.Builder(this)
                        .setTitle(getApplicationInfo().loadLabel(getPackageManager()).toString())
                        .setIcon(getPackageManager().getApplicationIcon(getPackageName()))
                        .setMessage("BypassRevokePermission を有効にすると大幅に利便性が向上します｡\nただし､ Play ストアが正しく動作しなくなる可能性があります｡\n\n続行しますか？")
                        .setPositiveButton("はい", new  DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int s) {
                                ofEnable(DevelopmentOptions.class);
                                // Enable BypassRevokePermission
                                ofEnable(BypassActivity.class);
                                ofEnable(BypassService.class);
                                ofEnable(DynamicReceiver.class);
                                ofEnable(BootCompletedReceiver.class);
                                ofEnable(PackageReceiver.class);
                                // Disable
                                ofDisable(DchaStateReceiver.class);
                                ofDisable(DchaStateReceiver.class);
                                // Reboot
                                ((PowerManager) getSystemService(POWER_SERVICE)).reboot(null);
                            }
                        })
                        .setNegativeButton("いいえ", new  DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int s) {
                                finishAndRemoveTask();
                                makeToast("キャンセルされました");
                            }
                        })
                        .show();
            } else {
                finishAndRemoveTask();
                if (GMS_VER <= GMS_MIN_VER) {
                    makeToast("Play ストアから ｢Google Play開発者サービス｣ を更新してください");
                    // Require Google account
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + GmsCore));
                } else {
                    makeToast("設定から[Play ストアのバージョン]をタップして､ストアを更新してください");
                    intent = new Intent(Intent.ACTION_MAIN).setPackage(Phonesky);
                }
                startActivity(intent);
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }
    }
}
