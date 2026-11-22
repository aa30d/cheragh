package org.cheragh.torch;

import android.content.Context;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;

/** API 23+, isolated from old Android's class verifier. */
class ModernTorch extends CameraManager.TorchCallback {
    private final TorchService service;
    private final CameraManager manager;
    private String id;
    private boolean ready, inFlight, expected, closed, requested;
    private int pending;

    ModernTorch(TorchService service) {
        this.service = service;
        manager = (CameraManager) service.getSystemService(Context.CAMERA_SERVICE);
        try {
            for (String candidate : manager.getCameraIdList()) {
                CameraCharacteristics c = manager.getCameraCharacteristics(candidate);
                if (Boolean.TRUE.equals(c.get(CameraCharacteristics.FLASH_INFO_AVAILABLE))) {
                    if (id == null) id = candidate;
                    if (Integer.valueOf(CameraCharacteristics.LENS_FACING_BACK).equals(c.get(CameraCharacteristics.LENS_FACING))) {
                        id = candidate;
                        break;
                    }
                }
            }
            if (id != null) manager.registerTorchCallback(this, null);
        } catch (Exception e) { id = null; }
    }

    void toggle() {
        if (id == null) { service.fail(); return; }
        requested = true;
        pending++;
        drain();
    }

    private void drain() {
        if (closed || !ready || inFlight || pending == 0) return;
        pending--;
        expected = !TorchService.on;
        inFlight = true;
        try { manager.setTorchMode(id, expected); }
        catch (Exception e) { inFlight = false; service.fail(); }
    }

    @Override public void onTorchModeChanged(String cameraId, boolean enabled) {
        if (closed || !cameraId.equals(id)) return;
        ready = true;
        service.setOn(enabled);
        if (inFlight && enabled == expected) inFlight = false;
        drain();
        if (requested && !enabled && !inFlight && pending == 0) service.stopSelf();
    }

    @Override public void onTorchModeUnavailable(String cameraId) {
        if (!closed && cameraId.equals(id)) service.fail();
    }

    void close() {
        closed = true;
        try {
            if (id != null) {
                if (TorchService.on) manager.setTorchMode(id, false);
                manager.unregisterTorchCallback(this);
            }
        } catch (Exception ignored) { }
    }
}
