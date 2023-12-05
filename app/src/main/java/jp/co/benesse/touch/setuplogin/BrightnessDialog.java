package jp.co.benesse.touch.setuplogin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class BrightnessDialog extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        finish();
        startActivity(new Intent().setClassName("com.android.systemui", "com.android.systemui.settings.BrightnessDialog"));
    }
}
