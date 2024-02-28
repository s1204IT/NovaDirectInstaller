package me.s1204.benesse.touch.support;

import android.app.Activity;
import android.content.Intent;
import android.os.BenesseExtension;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;

public class DevelopmentOptions extends Activity {
    private boolean getEnabled(String variable) throws Settings.SettingNotFoundException {
        return Settings.Global.getInt(getContentResolver(), variable) == 1;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BenesseExtension.setDchaState(3);
        finishAndRemoveTask();
        startActivity(new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS));
        try {
            if (getEnabled(Settings.Global.ADB_ENABLED) && getEnabled(Settings.Global.DEVELOPMENT_SETTINGS_ENABLED)) {
                new Handler(Looper.getMainLooper()).postDelayed(() -> BenesseExtension.setDchaState(0), 1000);
            }
        } catch (Settings.SettingNotFoundException ignored) {
        }
    }
}
