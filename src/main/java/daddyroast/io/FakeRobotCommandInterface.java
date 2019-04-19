package daddyroast.io;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

public class FakeRobotCommandInterface implements IRobotCommandInterface {
    @Override
    public List<DetectedObject> scan() {
        List<DetectedObject> list = Lists.newArrayList();
        list.add(new DetectedObject(10, 90, 10));
        list.add(new DetectedObject(10, 120, 6));
        return list;
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
