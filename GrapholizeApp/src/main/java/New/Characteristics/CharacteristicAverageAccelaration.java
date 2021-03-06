package New.Characteristics;

import New.Model.Entities.Dot;
import New.util.math.VelocityMathUtil;

import java.util.List;

public class CharacteristicAverageAccelaration extends Characteristic<Double>{

    public CharacteristicAverageAccelaration(String name, String unitName) {
        super(name, unitName);
    }

    @Override
    public Double calculateImplementation(List<List<Dot>> dotLists) {
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
                    totalAcceleration += Math.abs(VelocityMathUtil.acceleration(
                            lastVelocity
                            ,velocity
                            , timeDifference
                    ));
                    lastVelocity = velocity;
                }
                lastDot = dot;
            }
        }
        return totalAcceleration / (double)accelarationCounter;
    }
}
