package New.Characteristics;

import New.Model.Entities.Dot;

import java.util.List;

public class CharacteristicNumVelocityMax extends Characteristic<Integer, List<Dot>> {

    public CharacteristicNumVelocityMax(String name) {
        super(name);
    }

    @Override
    public Integer calculate(List<Dot> dots) {return 0;}

}
