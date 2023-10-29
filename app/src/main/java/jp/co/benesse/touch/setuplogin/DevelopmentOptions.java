package jp.co.benesse.touch.setuplogin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.provider.Settings.System;
import static android.net.Uri.parse;
import static android.provider.Settings.Global.*;

public class DevelopmentOptions extends Activity {

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String DCHA_STATE = "dcha_state";
        Intent intent;
        finishAndRemoveTask();
        if (System.canWrite(this)) {
            System.putInt(getContentResolver(), DCHA_STATE, 3);
            intent = new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS);
            try {
                if ((getInt(getContentResolver(), ADB_ENABLED) == 1) && (getInt(getContentResolver(), DEVELOPMENT_SETTINGS_ENABLED) == 1)) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            System.putInt(getContentResolver(), DCHA_STATE, 0);
                        }
                    }, 1000);
                }
            } catch (Settings.SettingNotFoundException ignored) {
            }
        } else {
            intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, parse("package:" + getPackageName())).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        startActivity(intent);
    }
}
