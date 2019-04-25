package daddyroast.gui;

import daddyroast.DetectedObject;

/**
 * @author adamcorp
 */
public interface GuiHandler {
    /**
     * Add the object to the UI
     * @param detectedObject the detected object
     */
    void addObject(DetectedObject detectedObject);

    /**
     * Move the robot in the UI
     * @param distance the distance the robot moved
     */
    void moveRobot(double distance);

    /**
     * Rotate the robot in the UI
     * @param degrees the degrees the robot turned
     */
    void rotateRobot(double degrees);
}
