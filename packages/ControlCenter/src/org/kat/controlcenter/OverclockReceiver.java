
package org.kat.controlcenter;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class OverclockReceiver extends BroadcastReceiver {
    boolean thtt = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Process p = Runtime.getRuntime().exec("getprop eos.overclocking.failed");
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String input;
            while ((input = reader.readLine()) != null) {
                if (input.contains("1"))
                    thtt = true;
            }
        } catch (IOException e) {
            return;
        }

        if (thtt) {
            Utils.deletePrefFlag(context, Utils.CLOCKS_ON_BOOT_PREF);
            NotificationManager mNotificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            long when = System.currentTimeMillis();

            Resources resources = context.getResources();
            Notification notification = new Notification(android.R.drawable.ic_dialog_alert,
                    resources.getString(R.string.eos_performance_not_applied), when);
            Intent intent1 = new Intent()
                    .setClassName("org.kat.controlcenter",
                            "org.kat.controlcenter.SingleFragmentActivity")
                    .putExtra(Utils.INCOMING_FRAG_KEY, Utils.PERFORMANCE_FRAG_TAG)
                    .addCategory(Intent.CATEGORY_DEFAULT)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent1, 0);
            notification.flags = Notification.FLAG_AUTO_CANCEL;
            notification.setLatestEventInfo(context,
                    resources.getString(R.string.eos_performance_notification_title),
                    resources.getString(R.string.eos_performance_notification_text), contentIntent);
            mNotificationManager.notify(0, notification);
        } else {
            if (Utils.prefFlagExists(context, Utils.CLOCKS_ON_BOOT_PREF)) {
                String val = Utils.readPrefValue(context, Utils.MIN_PREF);
                Utils.writeKernelValue(Utils.CPU_MIN_SCALE, val);
                val = Utils.readPrefValue(context, Utils.MAX_PREF);
                Utils.writeKernelValue(Utils.CPU_MAX_SCALE, val);
                val = Utils.readPrefValue(context, Utils.GOV_PREF);
                Utils.writeKernelValue(Utils.CPU_GOV, val);
            }
            if (Utils.prefFlagExists(context, Utils.IOSCHED_PREF)) {
                String val = Utils.readPrefValue(context, Utils.IOSCHED_PREF);
                Utils.writeKernelValue(Utils.IO_SCHED, val);
            }
            if (Utils.hasKernelFeature(Utils.S2W_PATH)) {
                boolean enabled = Utils.prefFlagExists(context, Utils.S2W_PREF);
                Utils.writeKernelValue(Utils.S2W_PATH, enabled ? "1" : "0");
            }
        }
    }
}
