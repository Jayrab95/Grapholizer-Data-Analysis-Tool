package Model.Entities;

import java.util.List;

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


}
