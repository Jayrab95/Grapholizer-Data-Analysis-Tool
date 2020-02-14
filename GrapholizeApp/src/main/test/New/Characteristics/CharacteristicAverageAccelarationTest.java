package New.Characteristics;

import New.Model.Entities.Dot;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class CharacteristicAverageAccelarationTest {

    @Test
    public void calculate() {
        List<List<Dot>> dotLists = new LinkedList<>();
        dotLists.add(new LinkedList<>());
        dotLists.add(new LinkedList<>());

        Float[] x1s = {0f, 2.1f, 4.22f, 5.333f, 6.1f, 6.7f};
        Float[] y1s = {0f, 5.6f, 5.9f ,6.6f , 7.2f , 8.1f};
        Float[] x2s = {6.6f, 7.0f, 8.12f, 8.73f, 9.1f, 10.32f};
        Float[] y2s = {3.4f, 4.126f, 4.567f ,4.89f , 5.687f , 6.8f};

        Long[] timeStamps1 = {10009l, 10019l, 10022l, 10030l, 10033l, 10040l};
        Long[] timeStamps2 = {10109l, 10119l, 10122l, 10130l, 10133l, 10140l};

        for (int i = 0; i < x1s.length; i++) {
            dotLists.get(0).add(new Dot(x1s[i],y1s[i],0,timeStamps1[i]));
        }

        for (int i = 0; i < x2s.length; i++) {
            dotLists.get(1).add(new Dot(x2s[i],y2s[i],0,timeStamps2[i]));
        }

        double result = new CharacteristicAverageAccelaration("Characert").calculate(dotLists);
        assertEquals(0.04612527309272141,result, 0.0000001);
    }
}