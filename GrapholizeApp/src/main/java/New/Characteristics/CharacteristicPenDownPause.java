package New.Characteristics;

import New.Model.Entities.Dot;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class CharacteristicPenDownPause extends Characteristic<Double, List<Dot>> {

    public CharacteristicPenDownPause(String name) {
        super(name);
    }

    @Override
    public Double calculate(List<Dot> dots) {return 0d;}

}
