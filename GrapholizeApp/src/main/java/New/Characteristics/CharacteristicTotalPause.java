package New.Characteristics;

import New.Model.Entities.Dot;

import java.util.List;

public class CharacteristicTotalPause extends Characteristic<Double, List<Dot>> {

    public CharacteristicTotalPause(String name) {
        super(name);
    }

    @Override
    public Double calculate(List<Dot> dots) {return 0d;}

}
