package util.math;

import Model.Entities.Dot;

import java.util.LinkedList;
import java.util.List;

public class DotCalculationsUtil {
    public static List<Double> getVelocityList(List<Dot> dots) {
        List<Double> result = new LinkedList<>();
        for (int i = 0; i < dots.size(); i++) {
            Dot lastDot = null;
            if(i == 0) {
                lastDot = dots.get(i);
            }else {
                Dot nextDot = dots.get(i);
                result.add(util.math.VelocityMathUtil.calculateVelocityBetweenDots(
                        lastDot.getX(),lastDot.getY()
                        ,nextDot.getX(),nextDot.getY()
                        , (int) (lastDot.getTimeStamp() - nextDot.getTimeStamp())
                ));
                lastDot = nextDot;
            }
        }
        return result;
    }
}
