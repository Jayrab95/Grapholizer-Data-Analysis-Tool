package New.Characteristics;

import New.util.math.VelocityMathUtil;

import java.util.List;

public class CharacteristicAccelarationAverage extends Characteristic<Double, List<Double>>{
    public CharacteristicAccelarationAverage(String name) {
        super(name);
    }

    @Override
    public Double calculate(List<Double> data) {
        return 0d;
    }
}
