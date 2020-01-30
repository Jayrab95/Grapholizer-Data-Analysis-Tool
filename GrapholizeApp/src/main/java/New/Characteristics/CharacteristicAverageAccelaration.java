package New.Characteristics;

import New.Model.Entities.Dot;
import New.util.math.VelocityMathUtil;

import java.util.List;

public class CharacteristicAverageAccelaration extends Characteristic<Double>{
    public CharacteristicAverageAccelaration(String name) {
        super(name);
    }

    @Override
    public Double calculate(List<List<Dot>> data) {
        throw new UnsupportedOperationException();
    }
}
