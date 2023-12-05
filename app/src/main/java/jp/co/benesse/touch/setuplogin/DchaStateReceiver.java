package jp.co.benesse.touch.setuplogin;
 
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import static android.os.BenesseExtension.setDchaState;
 
public class DchaStateReceiver extends BroadcastReceiver {
    public void onReceive(Context c, Intent i) {
        setDchaState(3);
    }
}
