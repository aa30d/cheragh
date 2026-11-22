package org.cheragh.torch;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
class LegacyNotification {
    static void start(TorchService service) {
        int flags = Build.VERSION.SDK_INT >= 23 ? 0x04000000 : 0;
        PendingIntent tap = PendingIntent.getActivity(service, 0, new Intent(service, ToggleActivity.class), flags);
        Notification notification = new Notification.Builder(service).setSmallIcon(R.drawable.torch_on)
                .setContentTitle("Torch is on").setContentText("Tap to turn off")
                .setContentIntent(tap).build();
        service.startForeground(1, notification);
    }
}
