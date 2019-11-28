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



}
