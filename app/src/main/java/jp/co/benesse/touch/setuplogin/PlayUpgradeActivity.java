package jp.co.benesse.touch.setuplogin;

import static android.content.pm.PackageManager.*;

import android.app.Activity;
import android.content.ComponentName;
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

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        finishAndRemoveTask();
        try {
            final int GMS_VER = getPackageManager().getPackageInfo(GmsCore, 0).versionCode;
            final int FINSKY_VER = getPackageManager().getPackageInfo(Phonesky, 0).versionCode;
            if (GMS_VER > GMS_MIN_VER && FINSKY_VER > FINSKY_MIN_VER) {
                // Enable BypassRevokePermission
                getPackageManager().setComponentEnabledSetting(new ComponentName(this, BypassActivity.class), COMPONENT_ENABLED_STATE_ENABLED, DONT_KILL_APP);
                getPackageManager().setComponentEnabledSetting(new ComponentName(this, BypassService.class), COMPONENT_ENABLED_STATE_ENABLED, DONT_KILL_APP);
                getPackageManager().setComponentEnabledSetting(new ComponentName(this, DynamicReceiver.class), COMPONENT_ENABLED_STATE_ENABLED, DONT_KILL_APP);
                getPackageManager().setComponentEnabledSetting(new ComponentName(this, BootCompletedReceiver.class), COMPONENT_ENABLED_STATE_ENABLED, DONT_KILL_APP);
                getPackageManager().setComponentEnabledSetting(new ComponentName(this, PackageReceiver.class), COMPONENT_ENABLED_STATE_ENABLED, DONT_KILL_APP);
                // Disable this Activity
                getPackageManager().setComponentEnabledSetting(new ComponentName(this, getClass()), COMPONENT_ENABLED_STATE_DISABLED, DONT_KILL_APP);
                // Disable DchaStateReceiver
                getPackageManager().setComponentEnabledSetting(new ComponentName(this, DchaStateReceiver.class), COMPONENT_ENABLED_STATE_DISABLED, DONT_KILL_APP);
                // Reboot
                PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
                pm.reboot(null);
            } else {
                if (GMS_VER <= GMS_MIN_VER) {
                    Toast.makeText(this, "Play ストアから､｢Google Play開発者サービス｣を更新してください", Toast.LENGTH_LONG).show();
                    // Require Google account
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + GmsCore)));
                } else {
                    Toast.makeText(this, "設定から[Play ストアのバージョン]をタップして､ストアを更新してください", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(Intent.ACTION_MAIN).setPackage(Phonesky));
                }
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }
    }
}
