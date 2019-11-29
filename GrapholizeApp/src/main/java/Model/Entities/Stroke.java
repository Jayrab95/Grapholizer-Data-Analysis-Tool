package Model.Entities;

import util.Import.CompressedDot;
import util.Import.CompressedStroke;

import java.util.*;

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

    @Override
    public String toString() {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("Stroke {\n");
        sBuilder.append("TimeStart");
        sBuilder.append(this.timeStart);
        sBuilder.append("Stroke {\n");
        sBuilder.append(this.timeEnd);
        sBuilder.append("Stroke {\n");
        sBuilder.append(this.dots.size());
        sBuilder.append("}\n");
        return sBuilder.toString();

    }
}
