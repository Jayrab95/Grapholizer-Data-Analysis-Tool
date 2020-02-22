package New.Characteristics;

import New.Model.Entities.Dot;
import New.util.math.VelocityMathUtil;

import java.util.LinkedList;
import java.util.List;

public class CharacteristicNormalizedJerk extends Characteristic<Double> {

    public CharacteristicNormalizedJerk(String name, String unitName) {
        super(name, unitName);
    }

    @Override
    public Double calculate(List<List<Dot>> dotLists) {
        double normalizedJerkSum = 0;
        for (List<Dot> dotList : dotLists) {
            List<Double> jerks = new LinkedList<>();
            Dot lastDot = null;
            double lastVelocity = 0;
            double lastAcceleration = 0;
            for (Dot dot : dotList) {
                if(lastDot != null) {
                    int timedifference = (int)(dot.getTimeStamp() - lastDot.getTimeStamp());
                    double velocity = VelocityMathUtil.calculateVelocityBetweenDots(
                            lastDot.getX()
                            , lastDot.getY()
                            , dot.getX()
                            , dot.getY()
                            , timedifference
                    );
                    /*double velocity = VelocityMathUtil.calculateVerticalVelocity(
                              lastDot.getY()
                            , dot.getX()
                            , timedifference
                    );*/
                    double acceleration = VelocityMathUtil.acceleration(
                            lastVelocity
                            ,velocity
                            , timedifference
                    );
                    jerks.add(VelocityMathUtil.jerk(lastAcceleration,acceleration,timedifference));
                    lastVelocity = velocity;
                    lastAcceleration = acceleration;
                }
                lastDot = dot;
            }
            double strokeLength = getStrokeLength(dotList);
            long duration = getStrokeDuration(dotList);
            normalizedJerkSum += VelocityMathUtil.normalizedJerk(jerks, duration, strokeLength);
        }
        return normalizedJerkSum / dotLists.size();
    }

    private long getStrokeDuration(List<Dot> dotList) {
        return dotList.get(dotList.size() - 1).getTimeStamp() - dotList.get(0).getTimeStamp();
    }

    private double getStrokeLength(List<Dot> dotList) {
        List<List<Dot>> dotLists = new LinkedList<>();
        dotLists.add(dotList);
        return new CharacteristicTotalLengthOfStrokes("tempCharac", "tempUnit").calculate(dotLists);
    }
}
