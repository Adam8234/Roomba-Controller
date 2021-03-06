package daddyroast;

/**
 * Short objects
 * @author adamcorp
 */
public enum BoulderType {
    LEFT(new DetectedObject(7, 180, 12.5, "boulder")),
    FRONT(new DetectedObject(0, 90, 12.5, "boulder")),
    RIGHT(new DetectedObject(7, 0, 12.5, "boulder"));

    private DetectedObject object;

    BoulderType(DetectedObject object) {
        this.object = object;
    }

    public DetectedObject getObject() {
        return object;
    }
}
