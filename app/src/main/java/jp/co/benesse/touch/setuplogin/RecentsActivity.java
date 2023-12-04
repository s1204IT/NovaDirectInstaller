package jp.co.benesse.touch.setuplogin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class RecentsActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        finishAndRemoveTask();
        startActivity(new Intent().setClassName("com.android.launcher3", "com.android.quickstep.RecentsActivity"));
    }
}
