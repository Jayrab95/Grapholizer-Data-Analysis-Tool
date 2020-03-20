package PageTest;

import New.Model.Entities.Page;
import New.Model.Entities.Segment;
import New.Observables.ObservablePage;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.*;

public class CollisionTest {
    private ObservablePage op;
    private Page p;
    private String key;

    @Before
    public void setup(){
        Set<Segment> segs = new TreeSet<>();
        segs.add(new Segment(0, 10));
        segs.add(new Segment(20,30));
        segs.add(new Segment(40,50));
        Page p = new Page(null, List.of(), "");
        key = "Test";
        p.getSegmentationsMap().put(key, segs);
        op = new ObservablePage(p);
    }

    @Test
    public void collisionTest_startTimeCollidesWithOtherSegment(){
        assertTrue(op.collidesWithOtherElements(key, 5, 15));
        assertTrue(op.collidesWithOtherElements(key, 10, 15));
        assertFalse(op.collidesWithOtherElements(key, 11, 15));
    }

    @Test
    public void collisionTest_endTimeCollidesWithOtherSegment(){
        assertTrue(op.collidesWithOtherElements(key, 15, 25));
        assertTrue(op.collidesWithOtherElements(key, 15, 20));
        assertFalse(op.collidesWithOtherElements(key, 15, 19));
    }

    @Test
    public void collisionTest_segmentContainsOtherSegment(){
        assertTrue(op.collidesWithOtherElements(key, 15, 35));
        assertTrue(op.collidesWithOtherElements(key, 20, 30));
    }

    @Test
    public void collisionTest_segmentFits(){
        assertFalse(op.collidesWithOtherElements(key, 11, 19));
    }

}
