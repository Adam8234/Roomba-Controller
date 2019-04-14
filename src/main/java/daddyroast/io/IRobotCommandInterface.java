package daddyroast.io;

import java.util.List;

public interface IRobotCommandInterface {
    List<DetectedObject> scan();
    float moveForward(int distance);
    float moveBackward(int distance);
    float rotateLeft(int angle);
    float rotateRight(int angle);
    IRobotConnection getRobotConnection();
}
