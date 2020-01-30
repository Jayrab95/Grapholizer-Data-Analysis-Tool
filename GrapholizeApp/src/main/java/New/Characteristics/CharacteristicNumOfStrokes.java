package New.Characteristics;

import New.Model.Entities.Dot;

import java.util.List;

public class CharacteristicNumOfStrokes extends Characteristic<Integer> {

    public CharacteristicNumOfStrokes(String name) {
        super(name);
    }

    @Override
    public Integer calculate(List<List<Dot>> dotLists) {
        return 0;
    }

}
