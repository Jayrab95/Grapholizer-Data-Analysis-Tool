package New.Characteristics;

import New.Model.Entities.Dot;
import New.util.math.VelocityMathUtil;

import java.util.List;

public class CharacteristicAverageDurationVelocityInversions extends Characteristic<Double> {

    public CharacteristicAverageDurationVelocityInversions(String name, String unitName) {
        super(name, unitName);
    }

    @Override
    public Double calculateImplementation(List<List<Dot>> dotLists) {
        if(dotLists.size() > 0) {
            int velocityInversionsDuration = 0;
            for (List<Dot> dotList : dotLists) {
                velocityInversionsDuration += calculateInversions(dotList);
            }
            return velocityInversionsDuration / (double) dotLists.size();
        }
        return 0.0d;
    }

    private double calculateInversions(List<Dot> dotList) {
        Dot lastDot = null;
        int velocityInversions = 0;
        double lastSign = 0;
        double lastVelocity = 0;
        double value = 0;
        double smoothing = 3d;
        for (Dot dot : dotList) {
            if(lastDot != null) {
                int timeDifference = (int)(dot.getTimeStamp() - lastDot.getTimeStamp());
                double velocity = VelocityMathUtil.calculateVelocityBetweenDots(
                        lastDot.getX(), lastDot.getY()
                        , dot.getX(), dot.getY(), timeDifference
                );

                value += (velocity - value) / (smoothing); //smoothe velocity

                double acceleration = VelocityMathUtil.acceleration(
                        lastVelocity
                        ,value
                        , timeDifference
                );
                double sign = Math.signum(acceleration);
                if(sign != lastSign) velocityInversions ++;
                lastVelocity = value;
                lastSign = sign;
            }
            lastDot = dot;
        }
        //Remove first sign change from start of stroke
        long totalTime = dotList.get(dotList.size() - 1).getTimeStamp() - dotList.get(0).getTimeStamp();
        return totalTime/(double)velocityInversions;
    }
}
