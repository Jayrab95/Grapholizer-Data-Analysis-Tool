package New.Model.Entities;

import java.util.HashMap;
import java.util.Map;

/**
 * A segment denotes a timeframe on a page's segmentation
 * The segment class contains the timeStart and timeStop values of the timeframe.
 * It also contains an annotations map. This map stores the annotations for this segment,
 * using the topicID (String) as the key, and storing the annotation (String) as the value.
 * The Segment class also implements the Comparable interface, so that segments can be ordered
 * after their timeStart attribute
 */
public class Segment implements Comparable<Segment> {

    private double timeStart;
    private double timeStop;
    private final Map<String, String> annotationsMap;

    /**
     * Constructor which takes a timeStart and a timeStop value and initializes a new, empty annotations map
     * @param timeStart timeStart of segment
     * @param timeStop timeSTop of segment
     */
    public Segment(double timeStart, double timeStop){
        this.timeStart = timeStart;
        this.timeStop = timeStop;
        annotationsMap = new HashMap<>();
    }

    /**
     * Copy constructor, copying the timeStart, timeSTop and the map of the given segment.
     * @param segment segment to be copied
     */
    public Segment(Segment segment){
        this(segment.timeStart, segment.timeStop, segment.annotationsMap);
    }

    /**
     * Constructor which takes an initial annotations map
     * @param timeStart timeStart of this segment
     * @param timeStop timeStop of this segment
     * @param annotations initial annotations Map
     */
    public Segment(double timeStart, double timeStop, Map<String, String> annotations){
        this(timeStart, timeStop);
        this.annotationsMap.putAll(annotations);
    }

    /**
     * @return the timeStart value of this segment
     */
    public double getTimeStart() {
        return timeStart;
    }

    /**
     * sets the timeStart value of this segment
     * @param timeStart new timeStart value for this segment
     */
    public void setTimeStart(double timeStart) {
        this.timeStart = timeStart;
    }

    /**
     * @return timeStop value of segment
     */
    public double getTimeStop() {
        return timeStop;
    }

    /**
     * Sets the timeStop of this segment
     * @param timeStop timestop value
     */
    public void setTimeStop(double timeStop) {
        this.timeStop = timeStop;
    }

    /**
     * Returns the duration of this segment, defined as "timeStop - timeStart".
     * @return duration value of segment
     */
    public double getDuration(){return timeStop - timeStart;}

    /**
     * @return the annotationsMap, which stores the annotations by using the topicID (String)
     * as a key and keeps the annotation (String) as a value.
     */
    public Map<String, String> getAnnotationsMap() {
        return annotationsMap;
    }

    /**
     * Retrieves the annotation with the given topicID from the annotationsMap. If the given ID
     * is not present in the map, a new empty will be created, pointing to an empty string.
     * @param topicID topicID that the target annotation is defined under.
     * @return the annotation defined for the topic id if present, and an empty string is no annotation
     * is present.
     */
    public String getAnnotation(String topicID){
        if(!annotationsMap.containsKey(topicID)){
            annotationsMap.put(topicID, "");
        }
        return annotationsMap.get(topicID);
    }

    /**
     * Puts the given annotation into the annotationsmap, using the topic ID as a key.
     * @param topicID topicID of the annotation that will be used as a key
     * @param annotationText annotation value for the given topic id.
     */
    public void putAnnotation(String topicID, String annotationText){
        annotationsMap.put(topicID, annotationText);
    }

    public void removeAnnotation(String topicID){
        annotationsMap.remove(topicID);
    }

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


    @Override
    public int compareTo(Segment o) {
        return Double.compare(this.getTimeStart(), o.getTimeStart());
    }
}
