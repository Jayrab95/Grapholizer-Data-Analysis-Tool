package New.Characteristics;

import New.Model.Entities.Dot;
import New.util.math.VelocityMathUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class CharacteristicVelocityAverage extends Characteristic<Double> {

    public CharacteristicVelocityAverage(String name) {
        super(name);
    }

    public static List<Double> getVelocitySteps(List<Dot> dots){
        List<Double> result = new LinkedList<>();
        if(dots.size() >=2){
            for (int i = 0; i < dots.size() - 1; i++) {
                Dot lastDot = dots.get(i);
                Dot nextDot = dots.get(i + 1);
                result.add(New.util.math.VelocityMathUtil.calculateVelocityBetweenDots(
                        lastDot.getX(),lastDot.getY()
                        ,nextDot.getX(),nextDot.getY()
                        , (int) (nextDot.getTimeStamp()- lastDot.getTimeStamp())
                ));
            }
        }
        return result;
    }

    @Override
    public Double calculate(List<List<Dot>> dotsLists) {
        List<Double> result = new LinkedList<>();
        for (List<Dot> dots : dotsLists) {
            Dot lastDot = null;
            for (Dot dot : dots) {
                if(lastDot != null) {
                    int timeDifference = (int)(dot.getTimeStamp() - lastDot.getTimeStamp());
                    result.add(VelocityMathUtil.calculateVelocityBetweenDots(
                            lastDot.getX(), lastDot.getY()
                            , dot.getX(), dot.getY()
                            , timeDifference
                    ));
                }
                lastDot = dot;
            }
        }
        Optional<Double> resSum = result.stream().reduce((a, b) -> a + b);
        if(resSum.isPresent() && resSum.get() != 0) {
            return resSum.get() / result.size();
        } else {
            return 0d;
        }
    }
}
