package New.Characteristics;

import New.Model.Entities.Dot;

import java.util.List;

public class CharacteristicPenUpPause extends Characteristic<Double, List<Dot>> {

    public CharacteristicPenUpPause(String name) {
        super(name);
    }

    @Override
    public Double calculate(List<Dot> dots) {throw new UnsupportedOperationException();}

}
