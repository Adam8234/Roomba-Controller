package daddyroast.io;

public interface IRobotConnection {
    boolean init();
    void sendString(String string);
    String readString();
}
