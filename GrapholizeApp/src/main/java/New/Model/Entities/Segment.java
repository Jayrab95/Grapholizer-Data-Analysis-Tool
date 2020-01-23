package New.Model.Entities;

import java.util.HashMap;
import java.util.Map;

public class Segment {
    //TODO: Remove Annotationtext.
    private String annotationText;
    private double timeStart;
    private double timeStop;
    private final Map<String, String> annotationsMap;

    public Segment(String annotationText, double timeStart, double timeStop){
        this.annotationText = annotationText;
        this.timeStart = timeStart;
        this.timeStop = timeStop;
        annotationsMap = new HashMap<>();
    }

    public Segment(Segment segment){
        this(segment.annotationText, segment.timeStart, segment.timeStop);
    }

    public Segment(String annotationText, double timeStart, double timeStop, Map<String, String> annotations){
        this(annotationText, timeStart, timeStop);
        this.annotationsMap.putAll(annotations);
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

    public double getDuration(){return timeStop - timeStart;}

    public String getAnnotation(String topicID){
        if(annotationsMap.containsKey(topicID)){
            annotationsMap.put(topicID, "");
        }
        return annotationsMap.get(topicID);
    }

    public void setAnnotation(String topicID, String annotationText){
        annotationsMap.put(topicID, annotationText);
    }

    //TODO: Consider moving these to Entity class?
    public boolean collidesWith(double otherTimeStart, double otherTimeStop){
        /* Timestart of other lies before timestart of this element, and the timestop lies after the timestart of this element
         * ___[-------]_ this
         * [-----]______ other
         */
        boolean startCollidesWithOther =
                otherTimeStart <= getTimeStart()
                        && otherTimeStop >= getTimeStart();
        /* Timestart of other lies before timestop of this element, and the timestop lies after the timestop of this element
         * [-------]___ this
         * ______[----] other
         */
        boolean endCollidesWithOther =
                otherTimeStart <= getTimeStop()
                        && otherTimeStop >= getTimeStop();
        /* Timestart of other lies after timestart of this element, and the timestop lies before the timestop of this element
         * ___[--------]__ this
         * _____[----]____ other
         */
        boolean otherIsContainedInThis =
                otherTimeStart >= getTimeStart()
                        && otherTimeStop <= getTimeStop();

        return startCollidesWithOther || endCollidesWithOther || otherIsContainedInThis;
    }

    public boolean timeStampWithinTimeRange(double timeStamp){
        return getTimeStart() <= timeStamp && getTimeStop() >= timeStamp;
    }

    public void move(double delta){
        timeStart += delta;
        timeStop += delta;
    }





}
