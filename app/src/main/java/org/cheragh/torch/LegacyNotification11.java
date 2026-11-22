package org.cheragh.torch;
import android.app.Notification;
import android.app.PendingIntent;
class LegacyNotification11 {
    static Notification make(TorchService service, PendingIntent tap) {
        return new Notification.Builder(service).setSmallIcon(R.drawable.torch_on)
                .setContentTitle("Torch is on").setContentText("Tap to turn off")
                .setContentIntent(tap).getNotification();
    }
}
