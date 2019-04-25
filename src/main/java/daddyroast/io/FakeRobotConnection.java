package daddyroast.io;

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
