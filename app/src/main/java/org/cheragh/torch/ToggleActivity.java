package org.cheragh.torch;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

/** A transparent trampoline so widget taps may start a foreground service on modern Android. */
public class ToggleActivity extends Activity {
    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        if (Build.VERSION.SDK_INT < 5) {
            Toast.makeText(this, "Flashlight control requires Android 2.0 or newer", Toast.LENGTH_LONG).show();
        } else {
            try {
                Intent intent = new Intent(this, TorchService.class);
                if (Build.VERSION.SDK_INT >= 26) startForegroundService(intent);
                else startService(intent);
            } catch (RuntimeException e) {
                Toast.makeText(this, "Unable to start flashlight", Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }
}
