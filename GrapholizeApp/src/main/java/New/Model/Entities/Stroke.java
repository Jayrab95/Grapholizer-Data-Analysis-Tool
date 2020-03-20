package New.Model.Entities;

import New.util.Import.model.CompressedDot;
import New.util.Import.model.CompressedStroke;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A Stroke represents a line drawn by a participant.
 * Stroke has two timestamps:
 * timeStart: The first instance of a dot with the PEN_DOWN DotType since the last occurence of a dot with the PEN_UP DotType
 * timeStop: The end of the stroke, marked by a dot of the PEN_UP DotType.
 * Stroke also contains the list of Dots representing the stroke.
 * The stroke is immutable after creation
 */
public class Stroke {


    private final long timeStart;
    private final long timeEnd;
    private final List<Dot> dots;

    public Stroke (long timeStart, long timeEnd, List<Dot> dots){
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.dots = dots;
    }

    /**
     * Copy constructor for Stroke
     * @param s the original stroke which needs to be copied.
     */
    public Stroke(Stroke s){
        this.timeStart = s.timeStart;
        this.timeEnd = s.timeEnd;
        this.dots = s.dots;
    }

    public Stroke(CompressedStroke cs, long initialTimestamp) {
        this.timeStart = cs.TimeStart - initialTimestamp;
        this.timeEnd = cs.TimeEnd - initialTimestamp;
        dots = new LinkedList<>();
        long accumulator = this.timeStart;
        for (CompressedDot compressedDot : cs.CompressedDots) {
            //no unsigned bytes in java
            int temp = compressedDot.TimeDiff;
            if(temp < 0) temp = temp & 0xff; //remove minus sign
            accumulator += temp;
            dots.add(new Dot(compressedDot, accumulator));
        }
    }

    public long getTimeStart() {
        return timeStart;
    }

    public long getTimeEnd() {
        return timeEnd;
    }

    public long getDuration(){ return timeEnd - timeStart;}

    public List<Dot> getDots() {
        return dots;
    }

    /**
     * Returns a list of dots whose timestamp lie within the given time raneg
     * @param start start of timeframe
     * @param end end of timeframe
     * @return list of dots within timeframe
     */
    public List<Dot> getDotsWithinTimeRange(double start, double end){
        return dots.stream()
                .filter(dot -> dot.getTimeStamp() >= start && dot.getTimeStamp() <= end)
                .collect(Collectors.toList());
    }

    /**
     * Checks if this stroke is within the given timeframe.
     * (timeStart >= start && timeEnd <= end)
     * @param start start of timeframe
     * @param end end of timeframe
     * @return true if stroke is within given timeframe, false if not.
     */
    public boolean isWithinTimeRange(double start, double end){
        return this.timeStart >= start && this.timeEnd <= end;
    }


}
