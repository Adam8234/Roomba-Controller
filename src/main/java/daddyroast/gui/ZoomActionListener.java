package daddyroast.gui;

import com.jme3.input.controls.ActionListener;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

public class ZoomActionListener implements ActionListener {
    Camera cam;

    public ZoomActionListener(Camera cam) {
        this.cam = cam;
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if(name.equals("ZoomI")){
            cam.setLocation(cam.getLocation().add(new Vector3f(0, 0, -1)));
        } else if(name.equals("ZoomO")) {
            cam.setLocation(cam.getLocation().add(new Vector3f(0, 0, 1)));
        }
    }
}
