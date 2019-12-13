package New.util.math;

import New.Model.Entities.Dot;
import java.util.LinkedList;
import java.util.List;

public class DotCalculationsUtil {
    public static VelocityParameters getVelocityParameters(List<Dot> dots) {
        List<Double> velocities = new LinkedList<>();
        List<Double> accelerations = new LinkedList<>();
        List<Double> jerks = new LinkedList<>();
        List<Double> normalizedJerks = new LinkedList<>();
        Dot lastDot = null;
        for (int i = 0; i < dots.size(); i++) {
            if(i == 0) {
                lastDot = dots.get(i);
            }else {
                Dot nextDot = dots.get(i);
                int timeDifference = (int)(nextDot.getTimeStamp() - lastDot.getTimeStamp());
                double velocity = velocityCalc(lastDot, nextDot, timeDifference);
                double acceleration = accelerationCalc(velocity, timeDifference);
                double jerk = jerkCalc(acceleration, timeDifference);
                double normalizedJerk = normalizedJerkCalc(acceleration, timeDifference);
                velocities.add(velocity);
                accelerations.add(acceleration);
                jerks.add(jerk);
                normalizedJerks.add(normalizedJerk);
                lastDot = nextDot;
            }
        }
        return new VelocityParameters(dots,velocities,accelerations,jerks, normalizedJerks);
    }

    private static double velocityCalc(Dot lastDot, Dot nextDot, int timeDifference) {
        return VelocityMathUtil.calculateVelocityBetweenDotsMmMs(
                lastDot.getX(),lastDot.getY()
                ,nextDot.getX(),nextDot.getY()
                , timeDifference
        );
    }

    public static double accelerationCalc(double velocity, int timeDifference) {
        return VelocityMathUtil.acceleration(velocity,timeDifference);
    }

    public static double jerkCalc(double acceleration, int timeDifference) {
        return VelocityMathUtil.jerk(acceleration,timeDifference);
    }

    public static double normalizedJerkCalc(double acceleration, int timeDifference) {
        return VelocityMathUtil.normalizedJerk(acceleration,timeDifference);
    }
}

