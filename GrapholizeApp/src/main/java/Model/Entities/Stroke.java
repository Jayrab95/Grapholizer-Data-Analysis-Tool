package Model.Entities;

import util.Import.CompressedDot;
import util.Import.CompressedStroke;

import java.util.*;
import java.util.stream.Collectors;

public class Stroke {

    private final long timeStart;
    private final long timeEnd;
    private final List<Dot> dots;

    public Stroke (long timeStart, long timeEnd, List<Dot> dots){
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.dots = dots;
    }

    public Stroke(Stroke s){
        this.timeStart = s.timeStart;
        this.timeEnd = s.timeEnd;
        //TODO: Do the dots need to be cloned? Can a copy of the stroke (or observableStroke) simply take the list reference?
        this.dots = s.dots;
    }

    public Stroke(CompressedStroke cs) {
        this.timeStart = cs.TimeStart;
        this.timeEnd = cs.TimeEnd;
        dots = new LinkedList<>();
        long accumulator = cs.TimeStart;
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
                .filter(dot -> dot.getTimeStamp() - this.timeStart >= start && dot.getTimeStamp()-this.timeStart <= end)
                .collect(Collectors.toList());
    }

    //TODO: Wait for uniform timestamps that start with 0
    public boolean isWithinTimeRange(double start, double end){
        return false;
    }


}
