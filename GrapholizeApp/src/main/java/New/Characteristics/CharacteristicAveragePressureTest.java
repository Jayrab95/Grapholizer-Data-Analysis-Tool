package New.Characteristics;

import New.Model.Entities.Dot;
import org.junit.Test;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class CharacteristicAveragePressureTest {
    private List<Float> randomDoubles;
    private List<Float> randomDoubles2;
    @Test
    public void calculate() {
        List<List<Dot>> dotLists = new LinkedList<>();
        dotLists.add(new LinkedList<>());
        dotLists.add(new LinkedList<>());
        Float[] floats1 = {1.0f, 0.89f, 0.57f, 0.364f, 0.767f, 0.572f};
        Float[] floats2 = {0.45f, 0.227f, 0.161f ,0.394f , 0.687f , 0.334f , 0.77f , 0.42f };


        for (Float randomDouble : floats1) {
            Dot dot = new Dot(0,0,randomDouble,0);
            dotLists.get(0).add(dot);
        }
        for (Float randomDouble : floats2) {
            Dot dot = new Dot(0,0,randomDouble,0);
            dotLists.get(1).add(dot);
        }

        Double result = new CharacteristicAveragePressure("Charac1").calculate(dotLists);
        assertEquals(0.5432857, result ,0.00001 );
    }
}