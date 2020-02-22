package New.Characteristics;

import New.Model.Entities.Dot;

import java.util.List;

public class CharacteristicTotalPenUpPause extends Characteristic<Long> {

    public CharacteristicTotalPenUpPause(String name, String unitName) {
        super(name, unitName);
    }

    @Override
    public Long calculate(List<List<Dot>> dotLists) {
        long lastTimestamp = 0;
        long totalPause = 0;
        for (int i = 0; i < dotLists.size(); i++) {
            List<Dot> dotList = dotLists.get(i);
            if(i != 0) {
                totalPause += dotList.get(0).getTimeStamp() - lastTimestamp;
            }
            lastTimestamp = dotList.get(dotList.size() - 1).getTimeStamp();
        }
        return totalPause;
    }
}
