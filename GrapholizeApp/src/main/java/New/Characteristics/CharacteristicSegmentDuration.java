package New.Characteristics;

import New.Model.Entities.Dot;
import New.Model.Entities.Segment;
import New.Model.Entities.Stroke;
import New.util.PageUtil;

import java.util.List;

public class CharacteristicSegmentDuration extends Characteristic<Long> {

    public CharacteristicSegmentDuration(String name, String unitName) {
        super(name, unitName);
    }

    @Override
    public Long calculate(Segment seg, List<Stroke> strokes) {
        return (long)seg.getDuration();
    }

    //Make the compiler happy, workaroud to implement special case
    @Override
    protected Long calculateImplementation(List<List<Dot>> data) {
        throw new UnsupportedOperationException();
    }

}
