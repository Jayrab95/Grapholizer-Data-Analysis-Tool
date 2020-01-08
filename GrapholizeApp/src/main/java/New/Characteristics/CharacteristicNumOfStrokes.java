package New.Characteristics;

import New.Model.Entities.Dot;

import java.util.List;

public class CharacteristicNumOfStrokes extends Characteristic<Double, List<Dot>> {

    public CharacteristicNumOfStrokes(String name) {
        super(name);
    }

    @Override
    public Double calculate(List<Dot> dots) {throw new UnsupportedOperationException();}

}
