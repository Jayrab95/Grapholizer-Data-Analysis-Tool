package New.Characteristics;

import New.Model.Entities.Dot;

import java.util.List;

public class CharacteristicNormalizedJerk extends Characteristic<Double> {

    public CharacteristicNormalizedJerk(String name) {
        super(name);
    }

    @Override
    public Double calculate(List<List<Dot>> dots) {throw new UnsupportedOperationException();}

}
