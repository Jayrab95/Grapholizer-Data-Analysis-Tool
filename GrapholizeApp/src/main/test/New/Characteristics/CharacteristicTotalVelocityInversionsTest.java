package New.Characteristics;

import New.Model.Entities.Dot;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class CharacteristicTotalVelocityInversionsTest {

    @Test
    public void calculate() {
        List<List<Dot>> dotLists = new LinkedList<>();
        dotLists.add(new LinkedList<>());
        dotLists.add(new LinkedList<>());

        Float[] x1s = {0f, 12.89f, 13.57f, 13.364f, 13.767f, 14.572f};
        Float[] y1s = {0f, 5.227f, 6.161f ,7.394f , 8.687f , 9.334f};
        Float[] x2s = {10.0f, 11.80f, 12.3f, 12.37f, 12.7f, 13.572f};
        Float[] y2s = {2.45f, 3.227f, 4.161f ,4.594f , 5.687f , 5.9f};

        Long[] timeStamps1 = {10009l, 10019l, 10022l, 10030l, 10033l, 10040l};
        Long[] timeStamps2 = {10109l, 10119l, 10122l, 10130l, 10133l, 10140l};

        for (int i = 0; i < x1s.length; i++) {
            dotLists.get(0).add(new Dot(x1s[i],y1s[i],0,timeStamps1[i]));
        }

        for (int i = 0; i < x2s.length; i++) {
            dotLists.get(1).add(new Dot(x2s[i],y2s[i],0,timeStamps2[i]));
        }

        int result = new CharacteristicTotalVelocityInversions("Characert", "unit")
                .calculateImplementation(dotLists);
        assertEquals(6,result);
    }

    @Test
    public void calculate1() {
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

        int result = new CharacteristicTotalVelocityInversions("Characert", "unit")
                .calculateImplementation(dotLists);
        assertEquals(6,result);
    }
}