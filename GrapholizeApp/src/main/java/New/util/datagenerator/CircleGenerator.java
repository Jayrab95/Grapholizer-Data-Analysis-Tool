package New.util.datagenerator;

import org.apache.commons.lang3.text.StrSubstitutor;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CircleGenerator extends DataGenerator{
    private int radius = 0;


    private int strokeCounter = 0;

    public CircleGenerator(double averageVelocity
            , int timeDifference, int pauseBetweenStrokes
            , int numberOfDots, int numberOfStrokes
            , int startX, int startY
            , int stepX, int stepY
            , double force
            , int radius) {
        super(averageVelocity, timeDifference
                , pauseBetweenStrokes, numberOfStrokes
                , numberOfDots
                , startX, startY
                , stepX, stepY
                , force);
        this.radius = radius;
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
        List<Double> listX = new LinkedList<>();
        List<Double> listY = new LinkedList<>();
        StringBuilder sBuilder = new StringBuilder();
        for(int i = 0; i < numberOfDots; i++) {
            Map<String, String> values = new HashMap<>();
            StrSubstitutor sub = new StrSubstitutor(values,"((", "))");

            //calculate x und y coords
            //calculate x
            double radian = Math.toRadians(angle);
            double x = x_0 + (radius * Math.sin(radian));
            double y = y_0 + (radius * Math.cos(radian));
            listX.add(x);
            listY.add(y);

            long xIntegral = (long) x;
            long yIntegral = (long) y;
            long xFractional = (long)((x - xIntegral)*10000);
            long yFractional =  (long)((y - yIntegral)*10000);

            values.put("x",Long.toString(xIntegral));
            values.put("fx",Long.toString(xFractional));
            values.put("y", Long.toString(yIntegral));
            values.put("fy", Long.toString(yFractional));
            values.put("force", Double.toString(force));
            values.put("dotType", "0");
            values.put("timeDifference", Integer.toString(timeDifference));

            sBuilder.append(sub.replace(preparedDot));
            angle += angleSteps;

            if(i < numberOfDots - 1) {
                sBuilder.append(",");
            }
        }
        debugOut(listX, listY);
        return sBuilder.toString();
    }

    private void debugOut(List<Double> listX, List<Double> listY) {
        StringBuilder debugBuilderX = new StringBuilder();
        StringBuilder debugBuilderY = new StringBuilder();
        debugBuilderX.append("{");debugBuilderY.append("{");
        for (int i = 0; i < listX.size(); i++) {
            debugBuilderX.append(listX.get(i)); debugBuilderY.append(listY.get(i));
            debugBuilderX.append("f"); debugBuilderY.append("f");
            if(i < listX.size() - 1) {
                debugBuilderX.append(','); debugBuilderY.append(",");
            }
        }
        debugBuilderX.append("};");debugBuilderY.append("};");

        StringBuilder debugBuilderTime1 = new StringBuilder();
        debugBuilderTime1.append("{");
        strokeCounter ++;
        int incrementer = 0;
        for (int i = 0; i < listX.size(); i++) {
            debugBuilderTime1.append(strokeCounter*100000 + incrementer);
            debugBuilderTime1.append("l");
            if(i < listX.size() - 1) {
                debugBuilderTime1.append(',');
            }
            incrementer += 5;
        }
        debugBuilderTime1.append("};");
    }
}
