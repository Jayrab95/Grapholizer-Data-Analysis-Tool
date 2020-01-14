package New.Characteristics;

import New.util.math.VelocityMathUtil;

import java.util.List;

public class CharacteristicAverageAccelaration extends Characteristic<Double, List<Double>>{
    public CharacteristicAverageAccelaration(String name) {
        super(name);
    }

    @Override
    public Double calculate(List<Double> data) {
        throw new UnsupportedOperationException();
    }
}
