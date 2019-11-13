package Model;

import java.util.ArrayList;
import java.util.List;

public class Stroke {

    //perhaps, everything in here can be set to final except for the color.
    private Color color;
    private boolean selected;
    private final long timeStart;
    private final long timeEnd;
    private final List<Dot> dots;

    public Stroke (long timeStart, long timeEnd, List<Dot> dots){
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.dots = dots;
        color = new Color();
    }
    public Stroke (long timeStart, long timeEnd, List<Dot> dots, Color c){
        this(timeStart, timeEnd, dots);
        color = new Color();
    }

    /*
    Is this way of adding dots to a stroke really needed?
    The only way strokes are built is during the file ooeping process.

    @Override
    public boolean add(Dot dot){
        //Check if this is even really necessary.
        //Apparently, the timestamps of dots are read as time differences from one dot to the next.
        if(size() <=0){
            timeStart = dot.getTimeStamp();
            color = dot.getColor();
        }
        timeEnd = dot.getTimeStamp();
        return super.add(dot);
    }
     */

    //Is this needed? Perhaps remove after testing.
    public String ToString()
    {
        return String.format( "Stroke => sectionId : timeStart : {0}, timeEnd : {1}, numOfDots: {3}", timeStart, timeEnd, dots.size() );
    }

    public long getTimeStart() {
        return timeStart;
    }

    public long getTimeEnd() {
        return timeEnd;
    }

    public Color getColor() {
        return color;
    }

    //Todo: Perhaps return a clone of dots. Dots should not be modifiable. Depending on the size of the list, this could hurt the performance however.
    public List<Dot> getDots() {
        return dots;
    }


    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isSelected(){
        return this.selected;
    }

    public void setSelected(boolean select){
        this.selected = select;
    }

}
