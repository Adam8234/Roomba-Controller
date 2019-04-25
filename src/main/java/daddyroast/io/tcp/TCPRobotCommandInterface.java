package daddyroast.io.tcp;

import com.google.common.base.Splitter;
import daddyroast.gui.GuiHandler;
import daddyroast.io.IRobotConnection;
import daddyroast.io.RobotCommandInterface;

/**
 * Command Interface for TCP WiFi communication
 * @author adamcorp
 */
public class TCPRobotCommandInterface extends RobotCommandInterface {
    private TCPRobotConnection connection;
    //Reads from the robot and asynchronously sends it to the stream.
    Runnable readingThread = () -> {
        while(true) {
            for (String s : Splitter.on(';').split(connection.readString())) {
                //Weird issues with empty strings...
                if(!s.isEmpty() || s.charAt(0) != 0) {
                    responseStream.onNext(s);
                }
            }
        }
    };

    public TCPRobotCommandInterface(GuiHandler guiHandler) {
        super(guiHandler);
        connection = new TCPRobotConnection();
        connection.init();
        new Thread(readingThread).start();
    }

    @Override
    public IRobotConnection getRobotConnection() {
        return connection;
    }
}
