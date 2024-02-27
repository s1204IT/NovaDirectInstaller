package jp.co.benesse.touch.setuplogin;

import static android.content.pm.PackageManager.*;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import jp.co.benesse.dcha.dchaservice.IDchaService;

public class DchaInstallApp extends Activity {
    IDchaService mDchaService;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.installapp);

        // DchaService をバインド
        if (bindService(new Intent("jp.co.benesse.dcha.dchaservice.DchaService").setPackage("jp.co.benesse.dcha.dchaservice"), new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                mDchaService = IDchaService.Stub.asInterface(iBinder);
            }
            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                unbindService(this);
                Toast.makeText(getApplicationContext(), "DchaService から切断されました", Toast.LENGTH_LONG).show();
                finishAndRemoveTask();
            }
        }, Context.BIND_AUTO_CREATE)) {

            // installApp(file)
            findViewById(R.id.exec).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText fileBox = findViewById(R.id.installApp_packagePath);
                    EditText flagBox = findViewById(R.id.installApp_flag);
                    String filePath = fileBox.getText().toString();
                    String flag = flagBox.getText().toString();
                    if (!filePath.startsWith("/")) {
                        Toast.makeText(getApplicationContext(), "フルパスで入力してください", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!filePath.endsWith(".apk")) {
                        Toast.makeText(getApplicationContext(), "APKファイルを指定してください", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (flag.equals("")) {
                        flag = "2";
                    }
                    try {
                        String result = String.valueOf(mDchaService.installApp(filePath, Integer.parseInt(flag)));
                        Toast.makeText(getApplicationContext(), "実行結果：" + result, Toast.LENGTH_LONG).show();
                    } catch (RemoteException ignored) {
                    }
                }
            });
        } else {
            Toast.makeText(this, "DchaService をバインド出来ませんでした\nアクティビティを無効化します", Toast.LENGTH_LONG).show();
            getPackageManager().setComponentEnabledSetting(new ComponentName(this, DchaInstallApp.class), COMPONENT_ENABLED_STATE_DISABLED, DONT_KILL_APP);
            finishAndRemoveTask();
        }
    }

    @Deprecated
    @Override
    public void onBackPressed() {
        finish();
    }
}
