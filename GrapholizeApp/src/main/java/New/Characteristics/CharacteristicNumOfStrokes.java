package New.Characteristics;

import New.Model.Entities.Dot;

import java.util.List;

public class CharacteristicNumOfStrokes extends Characteristic<Integer> {

    public CharacteristicNumOfStrokes(String name, String unitName) {
        super(name, unitName);
    }

    @Override
    public Integer calculate(List<List<Dot>> dotLists) {
        return dotLists.size();
    }

}
