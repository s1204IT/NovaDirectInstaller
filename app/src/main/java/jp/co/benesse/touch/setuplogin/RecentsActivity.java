package jp.co.benesse.touch.setuplogin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import static android.provider.Settings.System.putInt;

public class RecentsActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        finishAndRemoveTask();
        putInt(getContentResolver(), "hide_navigation_bar", 0);
        startActivity(new Intent().setClassName("com.android.launcher3", "com.android.quickstep.RecentsActivity"));
    }
}
