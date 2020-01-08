package New.Characteristics;

import New.Model.Entities.Dot;

import java.util.List;

public class CharacteristicTotalLengthOfStrokes extends Characteristic<Integer, List<Dot>> {

    public CharacteristicTotalLengthOfStrokes(String name) {
        super(name);
    }

    @Override
    public Integer calculate(List<Dot> dots) {throw new UnsupportedOperationException();}

}
