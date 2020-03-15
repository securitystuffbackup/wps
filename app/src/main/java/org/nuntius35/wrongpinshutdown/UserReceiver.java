package org.nuntius35.wrongpinshutdown;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class UserReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ShutdownManager sm = new ShutdownManager(context);
        IntentFilter filter = new IntentFilter(Intent.ACTION_USER_PRESENT);
        if (filter.matchAction(intent.getAction())) {
            sm.resetTries();
        }
    }
}
