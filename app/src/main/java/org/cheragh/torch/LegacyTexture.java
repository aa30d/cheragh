package org.cheragh.torch;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import java.io.IOException;
class LegacyTexture {
    static Object attach(Camera camera) throws IOException {
        SurfaceTexture texture = new SurfaceTexture(0);
        try { camera.setPreviewTexture(texture); } catch (IOException e) { release(texture); throw e; }
        return texture;
    }
    static void release(Object texture) { ((SurfaceTexture) texture).release(); }
}
