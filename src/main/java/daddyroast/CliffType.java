package daddyroast;

import com.jme3.math.Vector3f;
import daddyroast.io.DetectedObject;

import java.util.Vector;

public enum CliffType {
    LEFT(new DetectedObject(7, 180, 12, "cliff")),
    FRONT_LEFT(new DetectedObject(3, 100, 12, "cliff")),
    FRONT(new DetectedObject(3, 90, 12, "cliff")),
    FRONT_RIGHT(new DetectedObject(3, 80, 12, "cliff")),
    RIGHT(new DetectedObject(7, 0, 12, "cliff"));

    private final DetectedObject relative;

    CliffType(DetectedObject object) {
        this.relative = object;
    }

    public DetectedObject getObject() {
        return relative;
    }
}
