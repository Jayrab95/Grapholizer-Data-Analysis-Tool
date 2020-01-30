package New.Characteristics;

import New.Model.Entities.Dot;

import java.util.List;

public class CharacteristicAveragePressure extends Characteristic<Integer> {

    public CharacteristicAveragePressure(String name) {
        super(name);
    }

    @Override
    public Integer calculate(List<List<Dot>> dots) {throw new UnsupportedOperationException();}

}
