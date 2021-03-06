package daddyroast;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * DetectedObject just contains the distance, angle, and width relative to the IR and PING sensor
 * @author adamcorp
 */
public class DetectedObject {
    public double distance;
    public double angle;
    public double width;
    public String name;

    public DetectedObject(double distance, double angle, double width) {
        this.distance = distance;
        this.angle = angle;
        this.width = width;
        name = "object";
    }

    public DetectedObject(double distance, double angle, double width, String name) {
        this.distance = distance;
        this.angle = angle;
        this.width = width;
        this.name = name;
    }

    public static DetectedObject fromString(String string) {
        List<Double> values = Lists.newArrayList();
        System.out.println(string);
        for (String s : Splitter.on(',').split(string)) {
             values.add(Double.parseDouble(s));
        }
        return new DetectedObject(values.get(1), values.get(0), values.get(2));
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("Distance: %.2f, Width: %.2f, Angle: %.2f", distance, width, angle);
    }
}
