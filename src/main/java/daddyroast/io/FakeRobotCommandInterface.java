package daddyroast.io;

import java.util.ArrayList;
import java.util.List;

public class FakeRobotCommandInterface implements IRobotCommandInterface {
    @Override
    public List<DetectedObject> scan() {
        return new ArrayList<>();
    }

    @Override
    public float moveForward(int distance) {
        return distance;
    }

    @Override
    public float moveBackward(int distance) {
        return -distance;
    }

    @Override
    public float rotateLeft(int angle) {
        return angle;
    }

    @Override
    public float rotateRight(int angle) {
        return angle;
    }

    @Override
    public IRobotConnection getRobotConnection() {
        return null;
    }
}
