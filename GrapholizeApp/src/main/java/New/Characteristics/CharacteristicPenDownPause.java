package New.Characteristics;

import New.Model.Entities.Dot;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class CharacteristicPenDownPause extends Characteristic<Long> {

    public CharacteristicPenDownPause(String name) {
        super(name);
    }

    @Override
    public Long calculate(List<List<Dot>> dotLists) {
        long totalPenDownPause = 0;
        for (List<Dot> dotList : dotLists) {
            for (Dot dot : dotList) {

            }
        }
        return totalPenDownPause;
    }

}
