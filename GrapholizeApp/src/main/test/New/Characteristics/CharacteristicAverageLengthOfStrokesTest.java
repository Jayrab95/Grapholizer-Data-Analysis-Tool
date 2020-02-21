package New.Characteristics;

import New.Model.Entities.Dot;
import New.util.math.VelocityMathUtil;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class CharacteristicAverageLengthOfStrokesTest {

    @Test
    public void calculate() {
        List<List<Dot>> dotLists = new LinkedList<>();
        dotLists.add(new LinkedList<>());
        dotLists.add(new LinkedList<>());

        Float[] x1s = {0f, 12.89f, 13.57f, 13.364f, 13.767f, 14.572f};
        Float[] y1s = {0f, 5.227f, 6.161f ,7.394f , 8.687f , 9.334f};
        Float[] x2s = {10.0f, 11.80f, 12.3f, 12.37f, 12.7f, 13.572f};
        Float[] y2s = {2.45f, 3.227f, 4.161f ,4.594f , 5.687f , 5.9f};

        for (int i = 0; i < x1s.length; i++) {
            dotLists.get(0).add(new Dot(x1s[i],y1s[i],0,0));
        }

        for (int i = 0; i < x2s.length; i++) {
            dotLists.get(1).add(new Dot(x2s[i],y2s[i],0,0));
        }

        double result = new CharacteristicAverageLengthOfStrokes("Characert").calculate(dotLists);
        assertEquals(2868.5017109125406,result, 0.00001);
    }
}