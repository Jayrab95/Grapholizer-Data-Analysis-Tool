package New.Characteristics;

import New.Model.Entities.Dot;

import java.util.List;

public class CharacteristicNumOfStrokes extends Characteristic<Integer, List<Dot>> {

    public CharacteristicNumOfStrokes(String name) {
        super(name);
    }

    @Override
    public Integer calculate(List<Dot> dots) {
        return 0;
    }

}
