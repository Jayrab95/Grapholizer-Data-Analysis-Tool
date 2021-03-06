package New.util.math;


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class VelocityMathUtil {
    //Dots-Per-Inch is a standard value that is used by NeoNotes for all their papersizes
    private final static double DPI = 600;
    //Every NCode Cell on the paper has a number of pixels it can store
    private final static double PIXEL_PER_NCODE_CELL = 56;
    private final static double  PIXEL_DPI_RATIO = PIXEL_PER_NCODE_CELL/DPI;
    private final static double CM_TO_INCH_RATIO = 2.54f;

    public static double calculateVelocityBetweenDots(
            float x1, float y1
            , float x2, float y2
            , int timeDifference
    ) {
        double distanceVecX = x1 - x2;
        double distanceVecY = y1 - y2;
        double vecLenghtMm = convertToMillimeterCoords(
                vectorLength(distanceVecX,distanceVecY)
        );
        //calculate the velocity
        double res = velocityMMPerMS(vecLenghtMm, timeDifference);
        return res;
    }

    public static double calculateHorizontalVelocity(float x1, float x2, int timeDifference) {
        double x1MM = convertToMillimeterCoords(x1);
        double x2MM = convertToMillimeterCoords(x2);
        double vecLenghtMm = vectorLength(x2MM - x1MM, 0d);
        return velocityMMPerMS(vecLenghtMm, timeDifference);
    }

    public static double calculateVerticalVelocity(float y1, float y2, int timeDifference) {
        double y1MM = convertToMillimeterCoords(y1);
        double y2MM = convertToMillimeterCoords(y2);
        double vecLenghtMm = vectorLength(0d, y1MM - y2MM);
        return velocityMMPerMS(vecLenghtMm, timeDifference);
    }

    public static double calculateDistanceBetweenPoints(float x1, float y1, float x2, float y2) {
        float nCodeX = x1 - x2;
        float nCodeY = y1 - y2;
        double mmX = convertToMillimeterCoords(nCodeX);
        double mmY = convertToMillimeterCoords(nCodeY);
        return vectorLength(mmX,mmY);
    }

    public static double normalizedJerk(List<Double> jerkPoints, long duration, double length) {
        //Summ up all the jerks
        if(length == 0) return 0d;
        double normJerk = 0;
        double jerkSum = 0;
        for (int i = 0; i < jerkPoints.size(); i++) {
            double value = jerkPoints.get(i);
            jerkSum += Math.pow(value,2.0d);
        }
        /*
        Optional<Double> accelerationSum = jerkPoints.stream().reduce((d1, d2) ->
                Math.pow(d1,2.0d) + Math.pow(d2,2.0d)
        );*/
        if(jerkPoints.size() != 0){
            double squaredJerkSum = jerkSum / (double) jerkPoints.size();
            double integratedSquaredJerk = squaredJerkSum * duration;
            double powerDuration = Math.pow(duration,5.0d);
            double squaredLength = Math.pow(length,2.0d);
            normJerk =  Math.sqrt(0.5d * integratedSquaredJerk * (powerDuration / squaredLength));
        }else {
            return 0d;
        }
        return normJerk;
    }

    public static double vectorLength(double vecX, double vecY) {
        return Math.sqrt(Math.pow(vecX,2.0d) + Math.pow(vecY,2.0d));
    }

    //Converts Inch to screen coordinates: ScreenXY(InchVal) = InchVal * Screen DPI
    public static double convertToScreenCoordinate(double xOrYCoord, int screenDPI) {
        return xOrYCoord * screenDPI;
    }

    private static double velocityMMPerMS(double mm, int milliseconds){
        if(milliseconds == 0 || mm == 0) return 0.0d;
        return mm/(double)milliseconds;
    }

    public static double acceleration(double velocityMmPerMs1, double velocityMmPerMs2, int milliseconds){
        if(milliseconds == 0) return 0.0d;
        return (velocityMmPerMs2 - velocityMmPerMs1)/(double)milliseconds;
    }

    public static double jerk(double acceleration1, double acceleration2, int milliseconds) {
        if(milliseconds == 0) return 0.0d;
        return (acceleration2 - acceleration1)/(double)milliseconds;
    }


    private static double convertToMillimeterCoords(double neoCoord) {
        return convertInchToCm(convertNeoCoordToInch(neoCoord)) * 1000d;
    }

    //Converts Ncode™coordinates to Inch: InchVal(NcodeXY) = NcodeXY * 56.0 / 600.0
    public static double convertNeoCoordToInch(double xOrYCoord) {
        return xOrYCoord * PIXEL_DPI_RATIO;
    }

    public static double convertInchToCm(double inches){
        return inches * CM_TO_INCH_RATIO;
    }
}
