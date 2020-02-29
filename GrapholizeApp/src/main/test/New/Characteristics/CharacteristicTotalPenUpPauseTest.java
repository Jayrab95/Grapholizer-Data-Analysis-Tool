package New.Characteristics;

import New.Model.Entities.Dot;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class CharacteristicTotalPenUpPauseTest {

    @Test
    public void calculate() {
        List<List<Dot>> dotLists = new LinkedList<>();
        dotLists.add(new LinkedList<>());
        dotLists.add(new LinkedList<>());
        dotLists.add(new LinkedList<>());
        Long[] longs2 = {20015l,20023l,20028l,20036l,20038l,20069l,20079l};
        Long[] longs1 = {10010l,10017l,10023l,10025l,10028l,10038l,10045l,10048l,10052l};
        Long[] longs0 = {9009l, 9012l, 9025l, 9033l, 9045l};

        for (Long randomLongs : longs0) {
            Dot dot = new Dot(0,0,0, randomLongs);
            dotLists.get(0).add(dot);
        }
        for (Long randomLongs : longs1) {
            Dot dot = new Dot(0,0,0, randomLongs);
            dotLists.get(1).add(dot);
        }
        for (Long randomLongs : longs2) {
            Dot dot = new Dot(0,0,0,randomLongs);
            dotLists.get(2).add(dot);
        }

        long result = new CharacteristicTotalPenUpPause("Charac1", "unit").calculateImplementation(dotLists);
        assertEquals(10928,result);
    }
}