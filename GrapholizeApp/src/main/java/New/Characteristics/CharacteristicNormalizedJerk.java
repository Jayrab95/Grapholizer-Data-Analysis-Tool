package New.Characteristics;

import New.Model.Entities.Dot;

import java.util.List;

public class CharacteristicNormalizedJerk extends Characteristic<Integer> {

    public CharacteristicNormalizedJerk(String name) {
        super(name);
    }

    @Override
    public Integer calculate(List<List<Dot>> dots) {throw new UnsupportedOperationException();}

}
