package New.util.datagenerator;

import org.apache.commons.lang3.text.StrSubstitutor;

import java.util.HashMap;
import java.util.Map;

public abstract class DataGenerator {
    protected double averageVelocity;
    protected int timeDifference;
    protected int pauseBetweenStrokes;
    protected int numberOfDots;
    protected int numberOfStrokes;
    protected int startX;
    protected int startY;
    protected int stepX;
    protected int stepY;
    protected double force;
    protected final String preparedParticipant = "[\n" +
            "  {\n" +
            " \"Id\": \"lukas\",\n" +
            " \"Pages\": [\n" +
            " {\n" +
            " \"Section\": 3,\n" +
            " \"Owner\": 27,\n" +
            " \"Book\": 603,\n" +
            " \"Number\": ((pageNumber)),\n" +
            " \"X1\": 0.0,\n" +
            " \"Y1\": 0.0,\n" +
            " \"X2\": 0.0,\n" +
            " \"Y2\": 0.0,\n" +
            " \"MarginL\": 0.0,\n" +
            " \"MarginR\": 0.0,\n" +
            " \"MarginT\": 0.0,\n" +
            " \"MarginB\": 0.0,\n" +
            " \"Strokes\": [ " + "((strokes))" + "]\n}\n]\n]";
    protected final String preparedStroke = "{\"TimeStart\": ((timeStart)),\n" +
            " \"TimeEnd\": ((timeEnd)),\n" +
            " \"CompressedDots\": [\n" + "((dots))" + "]}";
    protected final String preparedDot = "{\n" +
            "\"X\": ((x)),\n" +
            "\"Fx\": ((fx)),\n" +
            "\"Y\": ((y)),\n" +
            "\"Fy\": ((fy)),\n" +
            "\"TiltX\": 0,\n" +
            "\"TiltY\": 0,\n" +
            "\"Twist\": 0,\n" +
            "\"Force\": ((force)),\n" +
            "\"DotType\": ((dotType)),\n" +
            "\"TimeDiff\": ((timeDifference))\n" +
            "}" ;

    public DataGenerator(double averageVelocity
            , int timeDifference, int pauseBetweenStrokes
            , int numberOfStrokes ,int numberOfDots
            , int startX, int startY
            , int stepX, int stepY
            , double force) {
        this.averageVelocity = averageVelocity;
        this.timeDifference = timeDifference;
        this.pauseBetweenStrokes = pauseBetweenStrokes;
        this.numberOfDots = numberOfDots;
        this.numberOfStrokes = numberOfStrokes;
        this.startX = startX;
        this.startY = startY;
        this.stepX = stepX;
        this.stepY = stepY;
        this.force = force;
    }

    public String createJsonFromShape() {
        return substituteParticipant();
    }

    private String substituteParticipant() {
        Map<String, String> values = new HashMap<String, String>();
        //Start Values defined
        StrSubstitutor sub = new StrSubstitutor(values,"((", "))");
        values.put("strokes",substituteStrokes());
        values.put("pageNumber", "1");
        return sub.replace(preparedParticipant);
    }

    /**
     * @return Json string of a List of strokes
    */
    private String substituteStrokes() {
        StringBuilder sBuilder = new StringBuilder();
        long timeStart = 0;
        long timeEnd = timeDifference * numberOfDots;
        int x = startX;
        int y = startY;
        for(int i = 0; i < numberOfStrokes; i++) {
            Map<String, String> values = new HashMap<>();
            StrSubstitutor sub = new StrSubstitutor(values,"((", "))");
            values.put("timeStart",Long.toString(timeStart));
            values.put("timeEnd",Long.toString(timeEnd));
            values.put("dots", substituteDots(x,y));
            //add stroke to collection
            sBuilder.append(sub.replace(preparedStroke));
            //add pause between strokes
            timeStart += pauseBetweenStrokes;
            timeEnd += pauseBetweenStrokes;
            x += stepX;
            y += stepY;
            //add a comma to every stroke, except the last one
            if(i < numberOfStrokes - 1){
                sBuilder.append(",");
            }
        }
        return sBuilder.toString();
    }

    public abstract String substituteDots(int centerX, int centerY);
}
