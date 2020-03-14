package New.Characteristics;
import New.Model.Entities.Segment;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static org.junit.Assert.*;
import org.junit.Test;

public class CharacteristicSegmentDurationTest {
    @Test
    public void calculate() {
        Characteristic characteristic = new CharacteristicSegmentDuration("test", "unit");
        Segment segment = new Segment(1001, 2000, new HashMap<>());
        long time =(Long) characteristic.calculate(segment, new LinkedList<>());
        assertEquals(999,time);
    }
}
