package daddyroast.io.fake;

import daddyroast.io.IRobotConnection;

public class FakeRobotConnection implements IRobotConnection {

    @Override
    public boolean init() {
        return false;
    }

    @Override
    public void sendString(String string) {
    }

    @Override
    public String readString() {
        return null;
    }
}
