package New.Characteristics;

import New.Model.Entities.Dot;

import java.util.List;

public class CharacteristicTotalPause extends Characteristic<Integer> {

    public CharacteristicTotalPause(String name, String unitName) {
        super(name, unitName);
    }

    @Override
    public Integer calculateImplementation(List<List<Dot>> dots) {throw new UnsupportedOperationException();}

}
