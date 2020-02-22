package New.Characteristics;

import New.Model.Entities.Dot;
import New.util.math.VelocityMathUtil;

import java.util.List;

public class CharacteristicAverageLengthOfStrokes extends Characteristic<Double> {

    public CharacteristicAverageLengthOfStrokes(String name, String unitName) {
        super(name, unitName);
    }

    @Override
    public Double calculate(List<List<Dot>> dotlists) {
        int numberOfStrokes = dotlists.size();
        double totalLength = 0;
        for (List<Dot> dotlist : dotlists) {
            Dot lastDot = null;
            for (Dot dot : dotlist) {
                if(lastDot != null) {
                    totalLength += VelocityMathUtil.calculateDistanceBetweenPoints(
                            lastDot.getX()
                            ,lastDot.getY()
                            ,dot.getX()
                            ,dot.getY()
                    );
                }
                lastDot = dot;
            }
        }
        return totalLength /numberOfStrokes;
    }
}
