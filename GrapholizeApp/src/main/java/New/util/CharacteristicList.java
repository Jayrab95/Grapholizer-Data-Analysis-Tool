package New.util;

import New.Characteristics.*;

import java.util.LinkedList;
import java.util.List;

public class CharacteristicList {
    public static List<Characteristic> characteristics(){
        List<Characteristic> characteristicList = new LinkedList<>();
        characteristicList.add(new CharacteristicVelocityAverage("Velocity Average", "mm/ms"));
        characteristicList.add(new CharacteristicAverageLengthOfStrokes("Average Stroke Length", "mm"));
        characteristicList.add(new CharacteristicAverageAccelaration("Average Acceleration", "mm/ms^2"));
        characteristicList.add(new CharacteristicAverageStrokeDuration("Average Stroke Duration", "ms"));
        characteristicList.add(new CharacteristicTotalDurationStrokes("total stroke durations", "ms"));
        characteristicList.add(new CharacteristicTotalVelocityInversions("Number of Velocity Inversions", "none"));
        characteristicList.add(new CharacteristicNormalizedJerk("Normalized Jerk", "none"));
        characteristicList.add(new CharacteristicAveragePressure("Average Pressure", "find out"));
        return characteristicList;
    }

    public static List<Characteristic> characteristicsExport(){
        List<Characteristic> characteristicList = new LinkedList<>();
        characteristicList.add(new CharacteristicTotalPenUpPause("Pen Up Pause", "amount"));
        characteristicList.add(new CharacteristicNumOfStrokes("Number of Strokes", "amount"));
        characteristicList.add(new CharacteristicVelocityAverage("Velocity Average", "mm/ms"));
        characteristicList.add(new CharacteristicAverageLengthOfStrokes("Average Stroke Length", "mm"));
        characteristicList.add(new CharacteristicAverageAccelaration("Average Acceleration", "mm/ms^2"));
        characteristicList.add(new CharacteristicAverageStrokeDuration("Average Stroke Duration", "ms"));
        characteristicList.add(new CharacteristicTotalDurationStrokes("total stroke durations", "ms"));
        characteristicList.add(new CharacteristicTotalVelocityInversions("Number of Velocity Inversions", "none"));
        characteristicList.add(new CharacteristicNormalizedJerk("Normalized Jerk", "none"));
        characteristicList.add(new CharacteristicAveragePressure("Average Pressure", "find out"));
        return characteristicList;
    }

}
