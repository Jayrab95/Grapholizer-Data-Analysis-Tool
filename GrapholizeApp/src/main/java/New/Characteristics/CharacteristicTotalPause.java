package New.Characteristics;

import New.Model.Entities.Dot;

import java.util.List;

public class CharacteristicTotalPause extends Characteristic<Integer, List<Dot>> {

    public CharacteristicTotalPause(String name) {
        super(name);
    }

    @Override
    public Integer calculate(List<Dot> dots) {throw new UnsupportedOperationException();}

}
