package New.Characteristics;

import New.Model.Entities.Dot;
import New.Model.Entities.Stroke;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class CharacteristicAverageStrokeDuration extends Characteristic<Double> {

    public CharacteristicAverageStrokeDuration(String name, String unitName) {
        super(name, unitName);
    }

    @Override
    public Double calculate(List<List<Dot>> dotLists) {
        AtomicLong sumDuration = new AtomicLong();
        dotLists.forEach(list -> {
            long timeStart = list.get(0).getTimeStamp();
            long timeEnd = list.get(list.size() - 1).getTimeStamp();
            sumDuration.addAndGet(timeEnd - timeStart);
        });
        return sumDuration.doubleValue()/dotLists.size();
    }

}
