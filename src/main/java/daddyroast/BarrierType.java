package daddyroast;

/**
 * White lines
 * @author adamcorp
 */
public enum BarrierType {
    LEFT(new DetectedObject(7, 180, 12, "barrier")),
    FRONT_LEFT(new DetectedObject(3, 100, 12, "barrier")),
    FRONT_RIGHT(new DetectedObject(3, 80, 12, "barrier")),
    RIGHT(new DetectedObject(7, 0, 12, "barrier"));

    private final DetectedObject relative;

    BarrierType(DetectedObject object) {
        this.relative = object;
    }

    public DetectedObject getObject() {
        return relative;
    }
}
