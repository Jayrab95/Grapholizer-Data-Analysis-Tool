package New.Characteristics;

import New.Model.Entities.Dot;

import java.util.List;

public class CharacteristicPenUpPause extends Characteristic<Double> {

    public CharacteristicPenUpPause(String name) {
        super(name);
    }

    @Override
    public Double calculate(List<List<Dot>> dots) {throw new UnsupportedOperationException();}

}
