package New.util.math;

import New.Model.Entities.Dot;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class VelocityMathUtilTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void calculateNormalizedJerk() {
        List<Double> jerks = new LinkedList<>();
        long duration = 100l;
        double strokeLength = 1d;
        VelocityMathUtil.normalizedJerk(jerks,duration,strokeLength);
    }

    @Test
    public void calculateVelocityBetweenDots() {
        Dot dot1 = new Dot(12.0f,11.1f,0f,1000l);
        Dot dot2 = new Dot(13.0f,12.1f,0f,1005l);;
        double velocity = VelocityMathUtil.calculateVelocityBetweenDots(
              dot1.getX(),dot1.getY()
            , dot2.getX(), dot2.getY()
            ,(byte) (dot2.getTimeStamp() - dot1.getTimeStamp())
        );
        assertEquals(67.05257803028755,velocity, 0.000001);
    }

    @Test
    public void vectorLengthMm() {
        List<Double> xMM = new LinkedList<>();
        xMM.add(0.0);xMM.add(15.1);xMM.add(20.7);xMM.add(8.0);xMM.add(100.1);
        List<Double> yMM = new LinkedList<>();
        yMM.add(0.0);yMM.add(38.354);yMM.add(52.578);yMM.add(20.32);yMM.add(254.254);
        List<Double> expLen = new LinkedList<>();
        expLen.add(0.0);expLen.add(41.21940460);expLen.add( 56.50607121);expLen.add(21.8380951550);expLen.add(273.2491656272714);

        for (int i = 0; i < xMM.size(); i++) {
            assertEquals(expLen.get(i), VelocityMathUtil.vectorLength(xMM.get(i),yMM.get(i)),0.000001);
        }

    }

    @Test
    public void convertNeoCoordToInch() {
        List<Float> inches = new LinkedList<>();
        inches.add(0.0f);inches.add(15.1f);inches.add(20.7f);inches.add(8.0f);inches.add(100.1f);
        List<Float> expected = new LinkedList<>();
        expected.add(0.0f);expected.add(1.4093333333283f);expected.add(1.931999999931f);expected.add(0.74666666664f);expected.add(9.34266666633f);
        for (int i = 0; i < inches.size(); i++) {
            assertEquals(expected.get(i),VelocityMathUtil.convertNeoCoordToInch(inches.get(i)),0.00001);
        }
    }

    @Test
    public void convertInchToCm() {
        List<Double> inches = new LinkedList<>();
        inches.add(0.0);inches.add(15.1);inches.add(20.7);inches.add(8.0);inches.add(100.1);
        List<Double> expected = new LinkedList<>();
        expected.add(0.0);expected.add(38.354);expected.add(52.578);expected.add(20.32);expected.add(254.254);
        for (int i = 0; i < inches.size(); i++) {
            assertEquals(expected.get(i),VelocityMathUtil.convertInchToCm(inches.get(i)),0.00001);
        }

    }
}