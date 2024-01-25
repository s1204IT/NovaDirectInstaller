package jp.co.benesse.touch.setuplogin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class BypassActivity extends Activity {

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        startService(new Intent(this, BypassService.class));
        Toast.makeText(this, "実行しました", Toast.LENGTH_SHORT).show();
        finishAndRemoveTask();
    }
}