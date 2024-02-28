package me.s1204.benesse.touch.support;

import android.app.Activity;
import android.os.BenesseExtension;
import android.os.Bundle;

public class DchaStateChanger extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        finishAndRemoveTask();
        if (BenesseExtension.getDchaState() == 3) {
            BenesseExtension.setDchaState(0);
        } else {
            BenesseExtension.setDchaState(3);
        }
    }
}
