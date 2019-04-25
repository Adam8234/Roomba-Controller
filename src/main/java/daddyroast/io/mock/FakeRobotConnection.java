package daddyroast.io.mock;

import daddyroast.io.IRobotConnection;

/**
 * Fake robot connection for testing
 * @author adamcorp
 */
public class FakeRobotConnection implements IRobotConnection {
    @Override
    public boolean init() {
        return true;
    }

    @Override
    public void sendString(String string) {
    }

    @Override
    public String readString() {
        return null;
    }
}
