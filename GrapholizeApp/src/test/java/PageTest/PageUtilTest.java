package PageTest;
import New.Model.Entities.Dot;
import New.Model.Entities.Page;
import New.Model.Entities.Segment;
import New.Model.Entities.Stroke;
import New.util.PageUtil;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class PageUtilTest {

    private List<Stroke> strokes;
    private List<Segment> segments;

    @Before
    public void setUp(){
        strokes = new LinkedList<>();
        int timeStamp = 0;
        for(int i = 0; i < 10; i++){
            List<Dot> dotSection = new LinkedList<>();
            for(int j = 0; j < 10; j++){
                dotSection.add(new Dot(0,0,0,timeStamp++));
            }
            Stroke s = new Stroke(dotSection.get(0).getTimeStamp(), dotSection.get(dotSection.size() - 1).getTimeStamp(), dotSection);
            strokes.add(s);
        }

        segments = new LinkedList<>();
        segments.add(new Segment(0,9)); //Denotes first stroke
        segments.add(new Segment(10,29)); //Denotes 2nd and 3rd stroke
        segments.add(new Segment(35, 45)); //Denotes area containing a part of stroke 3 and stroke 4
        segments.add(new Segment(52, 57)); //Denotes area  within stroke 5.
        segments.add(new Segment(95, 105)); //Denotes area which contains part of last stroke and is part out of bounds
        segments.add(new Segment(110, 120)); //Denotes area outside of strokes.
    }

    @Test
    public void test(){
        List<List<Dot>> dotSections = PageUtil.getDotSectionsForAnnotation(segments.get(0), strokes);
        assertEquals(1, dotSections.size());
        assertEquals(10, dotSections.get(0).size());

    }

    @Test
    public void test2(){
        List<List<Dot>> dotSections = PageUtil.getDotSectionsForAnnotation(segments.get(1), strokes);
        assertEquals(2, dotSections.size());
        assertEquals(10, dotSections.get(0).size());
        assertEquals(10, dotSections.get(1).size());
    }

    @Test
    public void test3(){
        List<List<Dot>> dotSections = PageUtil.getDotSectionsForAnnotation(segments.get(2), strokes);
        assertEquals(2, dotSections.size());
        assertEquals(5, dotSections.get(0).size());
        assertEquals(6, dotSections.get(1).size());
    }

    @Test
    public void test4(){
        List<List<Dot>> dotSections = PageUtil.getDotSectionsForAnnotation(segments.get(3), strokes);
        assertEquals(1, dotSections.size());
        assertEquals(6, dotSections.get(0).size());
    }

    @Test
    public void test5(){
        List<List<Dot>> dotSections = PageUtil.getDotSectionsForAnnotation(segments.get(4), strokes);
        assertEquals(1, dotSections.size());
        assertEquals(5, dotSections.get(0).size());
    }

    @Test
    public void test6(){
        List<List<Dot>> dotSections = PageUtil.getDotSectionsForAnnotation(segments.get(5), strokes);
        assertEquals(0, dotSections.size());
    }


}
