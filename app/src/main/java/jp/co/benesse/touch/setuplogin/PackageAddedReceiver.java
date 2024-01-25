package jp.co.benesse.touch.setuplogin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PackageAddedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!intent.getData().toString().replace("package:", "").equals("a.a")) {
            switch (intent.getAction()) {
                case Intent.ACTION_PACKAGE_ADDED:
                case Intent.ACTION_PACKAGE_REMOVED:
                case Intent.ACTION_PACKAGE_CHANGED:
                case Intent.ACTION_PACKAGE_REPLACED:
                    context.startService(new Intent(context, BypassService.class));
                    break;
            }
        }
    }
}
