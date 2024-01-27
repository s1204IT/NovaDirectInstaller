package jp.co.benesse.touch.setuplogin;

import static jp.co.benesse.touch.setuplogin.BypassActivity.isActiveBypassService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Objects;

public class PackageReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction()) && !Objects.requireNonNull(intent.getExtras()).getBoolean(Intent.EXTRA_REPLACING)) {
            _run(context, intent);
        }

        if (Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction())) {
            if (Objects.requireNonNull(intent.getExtras()).getBoolean(Intent.EXTRA_DATA_REMOVED) && intent.getExtras().getBoolean(Intent.EXTRA_REPLACING)) {
                _run(context, intent);
            }

            if (!intent.getExtras().getBoolean(Intent.EXTRA_DATA_REMOVED) && intent.getExtras().getBoolean(Intent.EXTRA_REPLACING)) {
                _run(context, intent);
            }
        }

        if (Intent.ACTION_PACKAGE_FULLY_REMOVED.equals(intent.getAction())) {
            _run(context, intent);
        }
    }

    private void _run(Context context, Intent intent) {
        if (!Objects.requireNonNull(intent.getData()).toString().replace("package:", "").equals("a.a")) {
            context.startActivity(new Intent(context, BypassActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }

        if (!isActiveBypassService(context)) {
            context.startService(new Intent(context, BypassService.class));
        }
    }
}