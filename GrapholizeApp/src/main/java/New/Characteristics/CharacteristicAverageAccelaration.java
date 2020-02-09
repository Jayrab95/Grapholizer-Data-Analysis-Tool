package New.Characteristics;

import New.Model.Entities.Dot;
import New.util.math.VelocityMathUtil;

import java.util.List;

public class CharacteristicAverageAccelaration extends Characteristic<Double>{
    public CharacteristicAverageAccelaration(String name) {
        super(name);
    }

    @Override
    public Double calculate(List<List<Dot>> dotLists) {
        double totalAcceleration = 0;
        int accelarationCounter = 0;
        for (List<Dot> dotList : dotLists) {
            Dot lastDot = null;
            double lastVelocity = 0;
            accelarationCounter += dotList.size() - 1;
            for (Dot dot : dotList) {
                if(lastDot != null) {
                    int timeDifference = (int)(dot.getTimeStamp() - lastDot.getTimeStamp());
                    double velocity = VelocityMathUtil.calculateVelocityBetweenDots(
                            lastDot.getX(), lastDot.getY()
                            , dot.getX(), dot.getY(), timeDifference
                    );
                    totalAcceleration += VelocityMathUtil.acceleration(
                            lastVelocity
                            ,velocity
                            , timeDifference
                    );
                    lastVelocity = velocity;
                }
                lastDot = dot;
            }
        }
        return totalAcceleration / accelarationCounter;
    }
}
