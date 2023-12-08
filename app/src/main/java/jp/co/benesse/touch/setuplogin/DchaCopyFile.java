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

public class DchaCopyFile extends Activity {
    IDchaService mDchaService;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.copyfile);

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

            // copyFile(from, to)
            findViewById(R.id.exec).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText fromBox = findViewById(R.id.copyFile_from);
                    EditText toBox = findViewById(R.id.copyFile_to);
                    String fromPath = fromBox.getText().toString();
                    String toPath = toBox.getText().toString();
                    if (!fromPath.startsWith("/") || !toPath.startsWith("/")) {
                        Toast.makeText(getApplicationContext(), "フルパスで入力してください", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (fromPath.endsWith("/") || toPath.endsWith("/")) {
                        Toast.makeText(getApplicationContext(), "ファイルを指定してください", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    try {
                        String result = String.valueOf(mDchaService.copyFile(fromPath, toPath));
                        Toast.makeText(getApplicationContext(), "実行結果：" + result, Toast.LENGTH_LONG).show();
                    } catch (RemoteException ignored) {
                    }
                }
            });
        } else {
            Toast.makeText(this, "DchaService をバインド出来ませんでした\nアクティビティを無効化します", Toast.LENGTH_LONG).show();
            getPackageManager().setComponentEnabledSetting(new ComponentName(this, DchaCopyFile.class), COMPONENT_ENABLED_STATE_DISABLED, DONT_KILL_APP);
            finishAndRemoveTask();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
