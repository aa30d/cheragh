package org.cheragh.torch;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

/** Transparent trampoline; no layout, screen or controls are displayed. */
public class ToggleActivity extends Activity {
    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        try {
            Intent intent = new Intent(this, TorchService.class);
            if (Build.VERSION.SDK_INT >= 26) startForegroundService(intent);
            else startService(intent);
        } catch (RuntimeException ignored) { }
        finish();
    }
}
