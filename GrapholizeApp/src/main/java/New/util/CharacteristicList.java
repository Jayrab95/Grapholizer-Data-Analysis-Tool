package New.util;

import New.Characteristics.*;

import java.util.LinkedList;
import java.util.List;

public class CharacteristicList {
    public static List<Characteristic> characteristics(){
        List<Characteristic> characteristicList = new LinkedList<>();
        characteristicList.add(new CharacteristicVelocityAverage("Velocity Average"));
        characteristicList.add(new CharacteristicAverageLengthOfStrokes("Average Stroke Length"));
        characteristicList.add(new CharacteristicAverageAccelaration("Average Acceleration"));
        characteristicList.add(new CharacteristicAverageStrokeDuration("Average Stroke Duration"));
        characteristicList.add(new CharacteristicTotalPenUpPause("Pen Up Pause"));
        characteristicList.add(new CharacteristicNumOfStrokes("Number of Strokes"));
        characteristicList.add(new CharacteristicTotalDurationStrokes("total stroke durations"));
        characteristicList.add(new CharacteristicTotalVelocityInversions("Number of Velocity Inversions"));
        characteristicList.add(new CharacteristicNormalizedJerk("Normalized Jerk"));
        characteristicList.add(new CharacteristicAveragePressure("Average Pressure"));
        return characteristicList;
    }
}
