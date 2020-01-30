package New.Characteristics;

import New.Model.Entities.Dot;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class CharacteristicPenDownPause extends Characteristic<Double> {

    public CharacteristicPenDownPause(String name) {
        super(name);
    }

    @Override
    public Double calculate(List<List<Dot>> dots) {throw new UnsupportedOperationException();}

}
