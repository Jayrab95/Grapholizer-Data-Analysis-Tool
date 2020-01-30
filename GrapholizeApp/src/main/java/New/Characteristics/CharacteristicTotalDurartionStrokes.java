package New.Characteristics;

import New.Model.Entities.Dot;

import java.util.List;

public class CharacteristicTotalDurartionStrokes extends Characteristic<Double> {

    public CharacteristicTotalDurartionStrokes(String name) {
        super(name);
    }

    @Override
    public Double calculate(List<List<Dot>> dots) {throw new UnsupportedOperationException();}

}
