package Controls.TimelineElement;

import Model.Entities.TimeLineElement;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class TimeLineElementRect extends Rectangle {

    protected double timeStart;
    protected double timeStop;
    protected Color color;
    protected BooleanProperty selected;
    protected String annotationText;
    protected TimeLineElement timeLineElement;
    //Reason for having comment in baseclass: When copying annotations, there first needs to be a type check to see if
    //There's also a comment

    public TimeLineElementRect(double tStart, double tEnd, double parentHeight, Color c, String annotationText){
        this.timeStart = tStart;
        this.timeStop = tEnd;
        this.color = c;
        this.selected = new SimpleBooleanProperty(false);
        this.annotationText = annotationText;

        this.timeLineElement = new TimeLineElement(annotationText, timeStart, timeStop);


        setHeight(parentHeight);
        double width = tEnd - tStart;
        setWidth(width);
        setX(tStart);
        setY(0);

        setOnMouseClicked(e -> handleMouseClick(e));


        if(this.getWidth() > 0) {
            setFill(c);
        }
    }

    public TimeLineElementRect(Color c, Rectangle r, String annotationText){
        this(r.getX(), r.getX() + r.getWidth(), r.getHeight(), c, annotationText);
    }

    //TODO: Consider moving these fields solely to the Model timelineElement
    public double getTimeStart(){return timeStart;}

    public void setTimeStart(double start){
        timeStart = start;
        timeLineElement.setTimeStart(start);
    }
    public double getTimeStop(){return timeStop;}

    public void setTimeStop(double stop){
        timeStop = stop;
        timeLineElement.setTimeStop(stop);
    }

    public Color getColor(){return color;}
    public void setColor(Color c){this.color = c;}

    public String getAnnotationText(){
        return annotationText;
    }
    public void setAnnotationText(String text){
        this.annotationText = text;
        this.timeLineElement.setAnnotationText(text);
    }

    public TimeLineElement getTimeLineElement(){return this.timeLineElement;}

    public BooleanProperty getSelectedBooleanProperty(){return this.selected;}
    public boolean isSelected(){
        return selected.get();
    }
    public void setSelected(boolean selected){
        this.selected.set(selected);
    }
    public void toggleSelected(){
        this.selected.set(!selected.get());
    }

    //TODO: Consider moving these to Entity class?
    public boolean collidesWith(TimeLineElementRect other){
        /* Timestart of other lies before timestart of this element, and the timestop lies after the timestart of this element
         * ___[-------]_ this
         * [-----]______ other
         */
        boolean startCollidesWithOther = other.timeStart <= this.timeStart && other.timeStop >= this.timeStart;
        /* Timestart of other lies before timestop of this element, and the timestop lies after the timestop of this element
         * [-------]___ this
         * ______[----] other
         */
        boolean endCollidesWithOther = other.timeStart <= this.timeStop && other.timeStop >= this.timeStop;
        /* Timestart of other lies after timestart of this element, and the timestop lies before the timestop of this element
         * ___[--------]__ this
         * _____[----]____ other
         */
        boolean otherIsContainedInThis = other.timeStart >= this.timeStop && other.timeStop <= this.timeStop;

        return startCollidesWithOther || endCollidesWithOther || otherIsContainedInThis;
    }

    public boolean timeStampWithinTimeRange(double timeStamp){
        return timeStart <= timeStamp && timeStop >= timeStamp;
    }

    public void move(double newTimeStart){
        setTimeStart(newTimeStart);
        setTimeStop(newTimeStart + getWidth());
        setX(newTimeStart);
    }

    protected void handleMouseClick(MouseEvent e){
        toggleSelected();
    }
}
