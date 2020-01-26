package New.util.datagenerator;

import org.apache.commons.lang3.text.StrSubstitutor;

import java.util.HashMap;
import java.util.Map;

public class CircleGenerator extends DataGenerator{

    public CircleGenerator(double averageVelocity
            , int timeDifference, int pauseBetweenStrokes
            , int numberOfDots, int numberOfStrokes
            , int startX, int startY
            , int stepX, int stepY
            , double force) {
        super(averageVelocity, timeDifference
                , pauseBetweenStrokes, numberOfStrokes
                , numberOfDots
                , startX, startY
                , stepX, stepY
                , force);
    }

    /**
     * @return Json String of a List of Dots representing the shape of circle
     */
    @Override
    public String substituteDots(int startX, int startY) {
        //(x−x0​)2+(y−y0​)2=r2 Kreisgleichung
        //x=x0​+r⋅cost y=y0+r⋅sin(t) *y=y_0+r\cdot\sin ty=y0​+r⋅sint
        double x_0 = startX;
        double y_0 = startY;
        int radius = 3;
        double angleSteps = 360d/(double)numberOfDots;
        double angle = 0;
        StringBuilder sBuilder = new StringBuilder();
        for(int i = 0; i < numberOfDots; i++) {
            Map<String, String> values = new HashMap<>();
            StrSubstitutor sub = new StrSubstitutor(values,"((", "))");

            //calculate x und y coords
            //calculate x
            double radian = Math.toRadians(angle);
            double x = x_0 + (radius * Math.sin(radian));
            double y = y_0 + (radius * Math.cos(radian));
            long xIntegral = (long) x;
            long yIntegral = (long) y;
            long xFractional = (long)((x - xIntegral)*10000);
            long yFractional =  (long)((y - yIntegral)*10000);

            values.put("x",Long.toString(xIntegral));
            values.put("fx",Long.toString(xFractional));
            values.put("y", Long.toString(yIntegral));
            values.put("fy", Long.toString(yFractional));
            values.put("force", "0.5"); //TODO add to private members
            values.put("dotType", "0");
            values.put("timeDifference", Integer.toString(timeDifference));

            sBuilder.append(sub.replace(preparedDot));
            angle += angleSteps;

            if(i < numberOfDots - 1) {
                sBuilder.append(",");
            }
        }
        return sBuilder.toString();
    }
}
