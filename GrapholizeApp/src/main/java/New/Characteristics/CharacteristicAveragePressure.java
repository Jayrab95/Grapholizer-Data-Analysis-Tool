package New.Characteristics;

import New.Model.Entities.Dot;

import java.util.List;

public class CharacteristicAveragePressure extends Characteristic<Integer, List<Dot>> {

    public CharacteristicAveragePressure(String name) {
        super(name);
    }

    @Override
    public Integer calculate(List<Dot> dots) {return 0;}

}
