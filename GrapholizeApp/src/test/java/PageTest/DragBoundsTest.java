package PageTest;

import New.Model.Entities.Dot;
import New.Model.Entities.Page;
import New.Model.Entities.Segment;
import New.Model.Entities.Stroke;
import New.Observables.ObservablePage;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.*;

public class DragBoundsTest {

    private ObservablePage op;
    private Page p;
    private String key;

    @Before
    public void setup(){
        Set<Segment> segs = new TreeSet<>();
        segs.add(new Segment(5, 10));
        segs.add(new Segment(20,30));
        segs.add(new Segment(40,50));
        Stroke s1 = new Stroke(0, 100, List.of(new Dot(0,0,0,0), new Dot(1,1,0,100)));
        p = new Page(null, List.of(s1), "");
        key = "Test";
        p.getSegmentationsMap().put(key, segs);
        op = new ObservablePage(p);
    }

    @Test
    public void test1(){
        double[] dragBounds = op.getBounds(3, key);
        assertEquals(0, dragBounds[0], 0.0);
        assertEquals(5, dragBounds[1], 0.0);
    }

    @Test
    public void test2(){
        double[] dragBounds = op.getBounds(15, key);
        assertEquals(10, dragBounds[0], 0.0);
        assertEquals(20, dragBounds[1], 0.0);
    }

    @Test
    public void test3(){
        double[] dragBounds = op.getBounds(60, key);
        assertEquals(50, dragBounds[0], 0.0);
        assertEquals(p.getPageDuration(), dragBounds[1], 0.0);
    }

}
