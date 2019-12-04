package Model.Entities;

import Interfaces.Observable;
import Interfaces.Observer;

import java.util.ArrayList;
import java.util.List;

public class TimeLineElement {
    private String annotationText;
    private double timeStart;
    private double timeStop;

    public TimeLineElement(String annotationText, double timeStart, double timeStop){
        this.annotationText = annotationText;
        this.timeStart = timeStart;
        this.timeStop = timeStop;
    }

    public TimeLineElement(TimeLineElement tle){
        this(tle.annotationText, tle.timeStart, tle.timeStop);
    }

    public String getAnnotationText() {
        return annotationText;
    }

    public void setAnnotationText(String annotationText) {
        this.annotationText = annotationText;
    }

    public double getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(double timeStart) {
        this.timeStart = timeStart;
    }

    public double getTimeStop() {
        return timeStop;
    }

    public void setTimeStop(double timeStop) {
        this.timeStop = timeStop;
    }

    //TODO: Consider moving these to Entity class?
    public boolean collidesWith(TimeLineElement other){
        /* Timestart of other lies before timestart of this element, and the timestop lies after the timestart of this element
         * ___[-------]_ this
         * [-----]______ other
         */
        boolean startCollidesWithOther =
                other.getTimeStart() <= getTimeStart()
                        && other.getTimeStop() >= getTimeStart();
        /* Timestart of other lies before timestop of this element, and the timestop lies after the timestop of this element
         * [-------]___ this
         * ______[----] other
         */
        boolean endCollidesWithOther =
                other.getTimeStart() <= getTimeStop()
                        && other.getTimeStop() >= getTimeStop();
        /* Timestart of other lies after timestart of this element, and the timestop lies before the timestop of this element
         * ___[--------]__ this
         * _____[----]____ other
         */
        boolean otherIsContainedInThis =
                other.getTimeStart() >= getTimeStop()
                        && other.getTimeStop() <= getTimeStop();

        return startCollidesWithOther || endCollidesWithOther || otherIsContainedInThis;
    }

    public boolean timeStampWithinTimeRange(double timeStamp){
        return getTimeStart() <= timeStamp && getTimeStop() >= timeStamp;
    }



}
