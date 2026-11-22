package org.cheragh.torch;

import android.app.Service;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Build;
import android.os.IBinder;

/** Keeps the torch's camera client alive until it is switched off. */
public class TorchService extends Service {
    static boolean on;
    private Camera camera;
    private ModernTorch modern;
    private Object texture;

    @Override public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= 26) Notifications.start(this);
        else LegacyNotification.start(this);
        if (Build.VERSION.SDK_INT >= 23) modern = new ModernTorch(this);
    }

    @Override public int onStartCommand(Intent intent, int flags, int startId) {
        if (modern != null) {
            modern.toggle();
        } else {
            if (on) { stopSelf(); return START_NOT_STICKY; }
            try {
                camera = Camera.open();
                Camera.Parameters p = camera.getParameters();
                if (p.getSupportedFlashModes() == null || !p.getSupportedFlashModes().contains(Camera.Parameters.FLASH_MODE_TORCH))
                    throw new IllegalStateException("No torch");
                p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                camera.setParameters(p);
                texture = LegacyTexture.attach(camera);
                camera.startPreview();
                setOn(true);
            } catch (Exception e) { fail(); }
        }
        return START_NOT_STICKY;
    }

    void setOn(boolean value) { on = value; TorchWidget.update(this, value); }
    void fail() {
        stopSelf();
    }
    @Override public void onDestroy() {
        if (modern != null) modern.close();
        if (camera != null) {
            try { camera.stopPreview(); } catch (RuntimeException ignored) { }
            camera.release();
            camera = null;
        }
        if (texture != null) LegacyTexture.release(texture);
        setOn(false);
        super.onDestroy();
    }
    @Override public IBinder onBind(Intent intent) { return null; }
}
