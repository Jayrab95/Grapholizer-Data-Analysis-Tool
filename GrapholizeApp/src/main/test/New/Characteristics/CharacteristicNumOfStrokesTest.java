package New.Characteristics;

import New.Model.Entities.Dot;
import org.junit.Test;
import java.util.LinkedList;
import java.util.List;
import static org.junit.Assert.*;

public class CharacteristicNumOfStrokesTest {

    @Test
    public void calculate() {
        List<List<Dot>> dotLists = new LinkedList<>();
        dotLists.add(new LinkedList<>());
        dotLists.add(new LinkedList<>());
        int numberOfStrokes = new CharacteristicNumOfStrokes("not used", "unit").calculateImplementation(dotLists);
        assertEquals(2,numberOfStrokes);
    }
}