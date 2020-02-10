package New.Characteristics;

import New.Model.Entities.Dot;

import java.util.List;

public class CharacteristicTotalDurationStrokes extends Characteristic<Long> {

    public CharacteristicTotalDurationStrokes(String name) {
        super(name);
    }

    @Override
    public Long calculate(List<List<Dot>> dotLists) {
        long totalDuration = 0;
        for (List<Dot> dotList : dotLists) {
            totalDuration += dotList.get(
                    dotList.size() - 1).getTimeStamp()
                    - dotList.get(0).getTimeStamp();
        }
        return totalDuration;
    }

}