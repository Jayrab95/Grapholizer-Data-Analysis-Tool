package New.util.math;



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
        double x1Mm = convertInchToCm(convertNeoCoordToInch(x1)) * 10;
        double y1Mm = convertInchToCm(convertNeoCoordToInch(y1)) * 10;
        double x2Mm = convertInchToCm(convertNeoCoordToInch(x2)) * 10;
        double y2Mm = convertInchToCm(convertNeoCoordToInch(y2)) * 10;
        //calculate the distance of two points with euclidean norm
        double distanceVecX = x1Mm - x2Mm;
        double distanceVecY = y1Mm - y2Mm;
        double vecLenghtMm = vectorLength(distanceVecX,distanceVecY);
        //calculate the velocity
        double res = velocityMMPerMS(vecLenghtMm, timeDifference);
        return res >= 0 ? res : 0;
    }

    public static double calculateDistanceBetweenPoints(float x1, float y1, float x2, float y2) {
        float nCodeX = x1 - x2;
        float nCodeY = y1 - y2;
        double mmX = convertInchToCm(convertNeoCoordToInch(nCodeX)) * 10;
        double mmY = convertInchToCm(convertNeoCoordToInch(nCodeY)) * 10;
        return vectorLength(mmX,mmY);
    }

    public static double vectorLength(double xMm, double yMm) {
        return Math.sqrt(Math.pow(xMm,2.0d) + Math.pow(yMm,2.0d));
    }

    //Converts Ncode™coordinates to Inch: InchVal(NcodeXY) = NcodeXY * 56.0 / 600.0
    public static double convertNeoCoordToInch(float xOrYCoord) {
        return xOrYCoord * PIXEL_DPI_RATIO;
    }

    public static double convertInchToCm(double inches){
        return inches * CM_TO_INCH_RATIO;
    }

    //Converts Inch to screen coordinates: ScreenXY(InchVal) = InchVal * Screen DPI
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

    //TODO Normalize it
    public static double jerk(double accelerationMmPerMsSquare, int milliseconds) {
        if(milliseconds == 0 || accelerationMmPerMsSquare == 0) return 0.0d;
        return accelerationMmPerMsSquare/(double)milliseconds;
    }

    //TODO Normalize this man
    public static double normalizedJerk(double accelerationMmPerMsSquare, int miliseconds) {
        if(miliseconds == 0 || accelerationMmPerMsSquare == 0) return 0.0d;
        return 1.0d / (accelerationMmPerMsSquare/(double)miliseconds);
    }
}
