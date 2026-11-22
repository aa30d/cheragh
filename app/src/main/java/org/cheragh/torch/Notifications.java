package org.cheragh.torch;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;

/** Only loaded on API 26+. */
class Notifications {
    static void start(TorchService service) {
        NotificationManager manager = (NotificationManager) service.getSystemService(TorchService.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(new NotificationChannel("torch", "Flashlight", NotificationManager.IMPORTANCE_LOW));
        PendingIntent tap = PendingIntent.getActivity(service, 0, new Intent(service, ToggleActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT | 0x04000000);
        Notification notification = new Notification.Builder(service, "torch")
                .setSmallIcon(R.drawable.torch_on).setContentTitle("Torch is on")
                .setContentText("Tap to turn off").setContentIntent(tap).setOngoing(true).build();
        service.startForeground(1, notification);
    }
}
