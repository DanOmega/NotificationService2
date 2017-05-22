package com.daniel.notificationsevice2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Daniel on 5/13/2017.
 */

public class BootCompletedIntentReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Intent pushIntent = new Intent(context, NotificationListener.class);
            context.startService(pushIntent);


        }
    }
}
