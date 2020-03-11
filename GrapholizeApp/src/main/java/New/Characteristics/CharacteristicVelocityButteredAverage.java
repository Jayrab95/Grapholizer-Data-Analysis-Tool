package New.Characteristics;

import New.Model.Entities.Dot;
import New.util.math.VelocityMathUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class CharacteristicVelocityButteredAverage extends Characteristic<Double> {


    public CharacteristicVelocityButteredAverage(String name, String unitName) {
        super(name, unitName);
    }

    /**
     *
     * @param dots dot liste eines strokes
     * @return Liste der velocities zwischen den dots, zusätzlich mit einem Lowpass filters
     */
    public static List<Double> getVelocitySteps(List<Dot> dots){
        double value = 0;
        double smoothing = 3d;
        List<Double> result = new LinkedList<>();

        if(dots.size() >=2){
            for (int i = 0; i < dots.size() - 1; i++) {
                Dot lastDot = dots.get(i);
                Dot nextDot = dots.get(i + 1);
                int timeDifference = (int) (nextDot.getTimeStamp()- lastDot.getTimeStamp());
                double velocity = VelocityMathUtil.calculateVelocityBetweenDots(
                        lastDot.getX(),lastDot.getY()
                        ,nextDot.getX(),nextDot.getY()
                        , timeDifference
                );
                value += (velocity - value) / (smoothing); //Smoothe the output
                //TODO use threshold extract to utility function
                result.add(value);
            }
        }
        return result;
    }
    
    @Override
    public Double calculateImplementation(List<List<Dot>> dotsLists) {
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
