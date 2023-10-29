package jp.co.benesse.touch.setuplogin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import static android.net.Uri.parse;
import static android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS;
import static android.provider.Settings.System.*;

public class DchaStateChanger extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String DCHA_STATE = "dcha_state";
        finishAndRemoveTask();
        if (canWrite(this)) {
            if (getInt(getContentResolver(), DCHA_STATE, 0) == 3) {
                putInt(getContentResolver(), DCHA_STATE, 0);
            } else {
                putInt(getContentResolver(), DCHA_STATE, 3);
            }
        } else {
            startActivity(new Intent(ACTION_MANAGE_WRITE_SETTINGS, parse("package:" + getPackageName())).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }
}
