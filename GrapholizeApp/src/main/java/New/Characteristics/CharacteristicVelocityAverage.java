package New.Characteristics;

import New.Model.Entities.Dot;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class CharacteristicVelocityAverage extends Characteristic<Double, List<Dot>> {

    public CharacteristicVelocityAverage(String name) {
        super(name);
    }

    @Override
    public Double calculate(List<Dot> dots) {
        List<Double> result = new LinkedList<>();
        for (int i = 0; i < dots.size(); i++) {
            Dot lastDot = null;
            if(i == 0) {
                lastDot = dots.get(i);
            }else {
                Dot nextDot = dots.get(i);
                result.add(New.util.math.VelocityMathUtil.calculateVelocityBetweenDots(
                        lastDot.getX(),lastDot.getY()
                        ,nextDot.getX(),nextDot.getY()
                        , (int) (lastDot.getTimeStamp() - nextDot.getTimeStamp())
                ));
                lastDot = nextDot;
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