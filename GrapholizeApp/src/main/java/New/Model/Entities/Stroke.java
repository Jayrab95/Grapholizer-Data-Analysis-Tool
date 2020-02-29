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
        //TODO: Do the dots need to be cloned? Can a copy of the stroke (or observableStroke) simply take the list reference?
        this.dots = s.dots;
    }

    public Stroke(CompressedStroke cs, long initialTimestamp) {
        this.timeStart = cs.TimeStart - initialTimestamp;
        this.timeEnd = cs.TimeEnd - initialTimestamp;
        dots = new LinkedList<>();
        long accumulator = this.timeStart;
        for (CompressedDot compressedDot : cs.CompressedDots) {
            accumulator += compressedDot.TimeDiff;
            dots.add(new Dot(compressedDot, accumulator));
        }
    }

    public long getTimeStart() {
        return timeStart;
    }

    public long getTimeEnd() {
        return timeEnd;
    }

    //Todo: Perhaps return a clone of dots. Dots should not be modifiable. Depending on the size of the list, this could hurt the performance however.
    public List<Dot> getDots() {
        return dots;
    }

    public List<Dot> getDotsWithinTimeRange(double start, double end){
        return dots.stream()
                .filter(dot -> dot.getTimeStamp() >= start && dot.getTimeStamp() <= end)
                .collect(Collectors.toList());
    }

    public boolean isWithinTimeRange(double start, double end){
        return this.timeStart >= start && this.timeEnd <= end;
    }


}
