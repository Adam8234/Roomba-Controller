package daddyroast.io.tcp;

import daddyroast.gui.GuiHandler;
import daddyroast.io.IRobotConnection;
import daddyroast.io.RobotCommandInterface;

public class TCPRobotCommandInterface extends RobotCommandInterface {
    private TCPRobotConnection connection;

    public TCPRobotCommandInterface(GuiHandler guiHandler) {
        super(guiHandler);
        connection = new TCPRobotConnection();
        connection.init();
    }

    @Override
    public IRobotConnection getRobotConnection() {
        return connection;
    }
}
