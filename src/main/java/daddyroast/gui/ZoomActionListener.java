package daddyroast.gui;

import com.jme3.input.controls.ActionListener;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

/**
 * Zooms the camera in and out
 * @author adamcorp
 */
public class ZoomActionListener implements ActionListener {
    private Camera cam;

    public ZoomActionListener(Camera cam) {
        this.cam = cam;
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (!isPressed) {
            return;
        }
        if (name.equals("ZoomI")) {
            cam.setLocation(cam.getLocation().add(new Vector3f(0, 0, -10)));
        } else if (name.equals("ZoomO")) {
            cam.setLocation(cam.getLocation().add(new Vector3f(0, 0, 10)));
        }
    }
}
