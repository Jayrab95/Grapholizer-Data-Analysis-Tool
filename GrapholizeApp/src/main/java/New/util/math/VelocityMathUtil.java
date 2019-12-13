package New.util.math;



public class VelocityMathUtil {
    //Dots-Per-Inch is a standard value that is used by NeoNotes for all their papersizes
    private final static int DPI = 600;
    //Every NCode Cell on the paper has a number of pixels it can store
    private final static int PIXEL_PER_NCODE_CELL = 56;
    private final static double  PIXEL_DPI_RATIO = PIXEL_PER_NCODE_CELL/DPI;
    private final static float CM_TO_INCH_RATIO = 2.54f;

    public static double calculateVelocityBetweenDots(
            float x1, float y1
            , float x2, float y2
            , int timeDifference
    ) {
        //Convert all points to mm coordinates
        double x1Mm = convertInchToCm(convertNeoCoordToInch(x1)) * 10;
        double y1Mm = convertInchToCm(convertNeoCoordToInch(x1)) * 10;
        double x2Mm = convertInchToCm(convertNeoCoordToInch(x1)) * 10;
        double y2Mm = convertInchToCm(convertNeoCoordToInch(x1)) * 10;
        //calculate the distance of two points with euclidean norm
        double distanceVecX = x1Mm - x2Mm;
        double distanceVecY = y1Mm - y2Mm;
        double vecLenghtMm = vectorLengthMm(distanceVecX,distanceVecY);
        return velocityMMPerMS(vecLenghtMm, timeDifference);
    }

    public static double vectorLengthMm(double xMm, double yMm) {
        double pythagoras = (Math.pow(xMm,2) + Math.pow(yMm,2));
        return Math.sqrt(pythagoras);
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

    //unit is mm/ms^2
    public static double acceleration(double velocityMmPerMs, int milliseconds){
        if(milliseconds == 0 || velocityMmPerMs == 0) return 0.0d;
        return velocityMmPerMs/(double)milliseconds;
    }

    //TODO Normalize it
    public static double jerk(double accelerationMmPerMsSquare, int milliseconds) {
        if(milliseconds == 0 || accelerationMmPerMsSquare == 0) return 0.0d;
        return accelerationMmPerMsSquare/(double)milliseconds;
    }

    public static double normalizedJerk(double accelerationMmPerMsSquare, int miliseconds) {
        if(miliseconds == 0 || accelerationMmPerMsSquare == 0) return 0.0d;
        return 1.0d / (accelerationMmPerMsSquare/(double)miliseconds);
    }
}
