package New.Characteristics;

import New.Model.Entities.Dot;
import New.util.math.VelocityMathUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class CharacteristicVelocityVerticalAverage extends Characteristic<Double> {

    public CharacteristicVelocityVerticalAverage(String name, String unitName) {
        super(name, unitName);
    }

    @Override
    public Double calculateImplementation(List<List<Dot>> dotLists) {
        List<Double> result = new LinkedList<>();
        for (List<Dot> dots : dotLists) {
            Dot lastDot = null;
            for (int i = 0; i < dots.size(); i++) {
                if (i == 0) {
                    lastDot = dots.get(i);
                } else {
                    Dot nextDot = dots.get(i);
                    int timeDifference = (int)(nextDot.getTimeStamp() - lastDot.getTimeStamp());
                    result.add(VelocityMathUtil.calculateVerticalVelocity(
                            lastDot.getY()
                            , nextDot.getY()
                            , timeDifference
                    ));
                    lastDot = nextDot;
                }
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
