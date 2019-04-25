package daddyroast.gui;

import daddyroast.BoulderType;
import daddyroast.CliffType;
import daddyroast.io.DetectedObject;

public interface GuiHandler {
    void addObject(DetectedObject detectedObject);
    void moveRobot(double distance);
    void rotateRobot(double degrees);
}
