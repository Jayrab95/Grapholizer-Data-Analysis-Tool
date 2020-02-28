package New.Characteristics;

import New.Model.Entities.Dot;

import java.util.List;

public class CharacteristicTotalDurationAnnotation extends Characteristic<Double> {

    public CharacteristicTotalDurationAnnotation(String name, String unitName) {
        super(name, unitName);
    }

    @Override
    public Double calculateImplementation(List<List<Dot>> dots) {return 0d;}

}
