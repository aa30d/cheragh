package org.cheragh.torch;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
class LegacyNotification {
    static void start(TorchService service) {
        int flags = Build.VERSION.SDK_INT >= 23 ? 0x04000000 : 0;
        PendingIntent tap = PendingIntent.getActivity(service, 0, new Intent(service, ToggleActivity.class), flags);
        Notification n;
        if (Build.VERSION.SDK_INT >= 11) {
            n = LegacyNotification11.make(service, tap);
        } else {
            n = new Notification(R.drawable.torch_on, "Torch is on", System.currentTimeMillis());
            try {
                Notification.class.getMethod("setLatestEventInfo", android.content.Context.class,
                        CharSequence.class, CharSequence.class, PendingIntent.class)
                        .invoke(n, service, "Torch is on", "Tap to turn off", tap);
            } catch (Exception e) { throw new IllegalStateException(e); }
        }
        service.startForeground(1, n);
    }
}
