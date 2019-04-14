package daddyroast.io.tcp;

import com.google.common.collect.Lists;
import daddyroast.io.DetectedObject;
import daddyroast.io.IRobotCommandInterface;
import daddyroast.io.IRobotConnection;

import java.util.List;

public class TCPRobotCommandInterface implements IRobotCommandInterface {
    TCPRobotConnection connection;
    public TCPRobotCommandInterface() {
        connection = new TCPRobotConnection();
        connection.init();
    }

    @Override
    public List<DetectedObject> scan() {
        connection.sendString("s;");
        List<String> response = connection.readResponse();
        List<DetectedObject> objects = Lists.newArrayListWithCapacity(response.size());
        for (String s : response) {
            objects.add(DetectedObject.fromString(s));
        }
        return objects;
    }

    @Override
    public float moveForward(int distance) {
        connection.sendString(String.format("f%d;", distance));
        return Float.parseFloat(connection.readResponse().get(0));
    }

    @Override
    public float moveBackward(int distance) {
        connection.sendString(String.format("b%d;", distance));
        return Float.parseFloat(connection.readResponse().get(0));
    }

    @Override
    public float rotateLeft(int angle) {
        connection.sendString(String.format("l%d;", angle));
        List<String> strings = connection.readResponse();
        return Float.parseFloat(strings.get(0));
    }

    @Override
    public float rotateRight(int angle) {
        connection.sendString(String.format("r%d;", angle));
        List<String> strings = connection.readResponse();
        return Float.parseFloat(strings.get(0));
    }

    @Override
    public IRobotConnection getRobotConnection() {
        return connection;
    }
}
