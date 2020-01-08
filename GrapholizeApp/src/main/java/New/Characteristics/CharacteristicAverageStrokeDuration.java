package New.Characteristics;

import New.Model.Entities.Stroke;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class CharacteristicAverageStrokeDuration extends Characteristic<Double, List<Stroke>> {

    public CharacteristicAverageStrokeDuration(String name) {
        super(name);
    }

    @Override
    public Double calculate(List<Stroke> strks) {
        AtomicLong sumDuration = new AtomicLong();
        strks.forEach(stroke -> {
            sumDuration.addAndGet(stroke.getTimeEnd() - stroke.getTimeStart());
        });
        return sumDuration.doubleValue()/strks.size();
    }

}
