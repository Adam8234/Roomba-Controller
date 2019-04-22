package daddyroast.gui;

import daddyroast.CliffType;
import daddyroast.WallType;
import daddyroast.io.DetectedObject;

public interface GuiHandler {
    void updateStatus(String string);
    void addObject(DetectedObject detectedObject);
    void addWall(WallType type);
    void addCliff(CliffType type);
    void moveRobot(double distance);
    void rotateRobot(double degrees);
}
