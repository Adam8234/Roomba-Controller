package daddyroast;

/**
 * State based on what the command the robot is currently performing
 * @author adamcorp
 */
public enum State {
    DONE,
    TURNING_LEFT,
    TURNING_RIGHT,
    SCANNING,
    MOVING_FORWARD;
}
