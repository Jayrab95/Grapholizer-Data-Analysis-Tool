package New.util.math;


import java.util.List;
import java.util.Optional;

public class VelocityMathUtil {
    //Dots-Per-Inch is a standard value that is used by NeoNotes for all their papersizes
    private final static double DPI = 600;
    //Every NCode Cell on the paper has a number of pixels it can store
    private final static double PIXEL_PER_NCODE_CELL = 56;
    private final static double  PIXEL_DPI_RATIO = PIXEL_PER_NCODE_CELL/DPI;
    private final static float CM_TO_INCH_RATIO = 2.54f;

    public static double calculateVelocityBetweenDots(
            float x1, float y1
            , float x2, float y2
            , int timeDifference
    ) {
        //Convert all points to mm coordinates
        double x1Mm = convertToMilimeterCoords(x1);
        double y1Mm = convertToMilimeterCoords(y1);
        double x2Mm = convertToMilimeterCoords(x2);
        double y2Mm = convertToMilimeterCoords(y2);
        //calculate the distance of two points with euclidean norm
        double distanceVecX = x1Mm - x2Mm;
        double distanceVecY = y1Mm - y2Mm;
        double vecLenghtMm = vectorLength(distanceVecX,distanceVecY);
        //calculate the velocity
        double res = velocityMMPerMS(vecLenghtMm, timeDifference);
        return res >= 0 ? res : 0;
    }

    public static double calculateHorizontalVelocity(float x1, float x2, int timeDifference) {
        double x1MM = convertToMilimeterCoords(x1);
        double x2MM = convertToMilimeterCoords(x2);
        double vecLenghtMm = vectorLength(x2MM - x1MM, 0d);
        return velocityMMPerMS(vecLenghtMm, timeDifference);
    }

    public static double calculateVerticalVelocity(float y1, float y2, int timeDifference) {
        double y1MM = convertToMilimeterCoords(y1);
        double y2MM = convertToMilimeterCoords(y2);
        double vecLenghtMm = vectorLength(0d, y1MM - y2MM);
        return velocityMMPerMS(vecLenghtMm, timeDifference);
    }

    public static double calculateDistanceBetweenPoints(float x1, float y1, float x2, float y2) {
        float nCodeX = x1 - x2;
        float nCodeY = y1 - y2;
        double mmX = convertToMilimeterCoords(nCodeX);
        double mmY = convertToMilimeterCoords(nCodeY);
        return vectorLength(mmX,mmY);
    }

    //TODO sqrt (0.5 * Sum (jerk(t)**2) * duration**5 / length**2).
    public static double normalizedJerk(List<Double> jerkPoints, long duration, double length) {
        //Summ up all the jerks
        if(length == 0) return 0d;
        double normJerk;
        Optional<Double> accelerationSum = jerkPoints.stream().reduce((d1, d2) ->
                Math.pow(d1,2.0d) + Math.pow(d2,2.0d)
        );
        if(accelerationSum.isPresent() && jerkPoints.size() != 0){
            double squaredJerkSum = accelerationSum.get() / (double) jerkPoints.size();
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

    //Converts Ncode™coordinates to Inch: InchVal(NcodeXY) = NcodeXY * 56.0 / 600.0
    public static double convertNeoCoordToInch(float xOrYCoord) {
        return xOrYCoord * PIXEL_DPI_RATIO;
    }

    public static double convertInchToCm(double inches){
        return inches * CM_TO_INCH_RATIO;
    }

    //Converts Inch to screen coordinates: ScreenXY(InchVal) = InchVal * Screen DPI //TODO does not belong here
    public static double convertToScreenCoordinate(double xOrYCoord, int screenDPI) {
        return xOrYCoord * screenDPI;
    }

    public static double velocityMMPerMS(double mm, int milliseconds){
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

    private static double convertToMilimeterCoords(float neoCoord) {
        return convertInchToCm(convertNeoCoordToInch(neoCoord)) * 10;
    }
}
