package daddyroast.io;

/**
 * Basic implementation for communications
 * This allows the UI to communicate in multiple ways. You can write your own to use Serial, Wifi, Bluetooth, etc.
 * @author adamcorp
 */
public interface IRobotConnection {
    /**
     * Initializes the connection
     * @return true if init completed correctly
     */
    boolean init();

    /**
     * Sends a string to the robot
     * @param string the string to send
     */
    void sendString(String string);

    /**
     * Reads a string from the robot
     * @return the string from the robot
     */
    String readString();
}
