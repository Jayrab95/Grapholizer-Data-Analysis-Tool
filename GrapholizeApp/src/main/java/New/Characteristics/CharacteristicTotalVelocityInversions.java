package New.Characteristics;

import New.Model.Entities.Dot;
import New.util.math.VelocityMathUtil;

import java.util.List;

public class CharacteristicTotalVelocityInversions extends Characteristic<Integer> {

    public CharacteristicTotalVelocityInversions(String name, String unitName) {
        super(name, unitName);
    }

    @Override
    public Integer calculateImplementation(List<List<Dot>> dotLists) {
        int velocityInversions = 0;
        for (List<Dot> dotList : dotLists) {
            velocityInversions += calculateInversions(dotList);
        }
        return velocityInversions;
    }

    private int calculateInversions(List<Dot> dotList) {
        Dot lastDot = null;
        int velocityInversions = 0;
        double lastSign = 0;
        double lastVelocity = 0;
        for (Dot dot : dotList) {
            if(lastDot != null) {
                int timeDifference = (int)(dot.getTimeStamp() - lastDot.getTimeStamp());
                double velocity = VelocityMathUtil.calculateVelocityBetweenDots(
                        lastDot.getX(), lastDot.getY()
                        , dot.getX(), dot.getY(), timeDifference
                );
                double acceleration = VelocityMathUtil.acceleration(
                        lastVelocity
                        ,velocity
                        , timeDifference
                );
                double sign = Math.signum(acceleration);
                if(sign != lastSign) velocityInversions ++;
                lastVelocity = velocity;
                lastSign = sign;
            }
            lastDot = dot;
        }
        //Remove first sign change from start of stroke
        return velocityInversions - 1;
    }
}
