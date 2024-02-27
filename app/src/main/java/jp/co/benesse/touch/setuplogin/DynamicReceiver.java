package jp.co.benesse.touch.setuplogin;

import static jp.co.benesse.touch.setuplogin.BypassActivity.isActiveBypassService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BenesseExtension;

import java.util.Objects;

public class DynamicReceiver extends BroadcastReceiver {
    @Deprecated
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.requireNonNull(intent.getAction()).equals(Intent.ACTION_SHUTDOWN)) {
            BenesseExtension.setDchaState(3);
        }

        if (Objects.requireNonNull(intent.getAction()).equals(Intent.ACTION_SCREEN_ON)) {
            if (!isActiveBypassService(context)) {
                context.startService(new Intent(context, BypassService.class));
            }
        }
    }
}