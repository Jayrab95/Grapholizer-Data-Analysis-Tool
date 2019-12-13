package New.Model.Entities;

public class Annotation {
    private String annotationText;
    private double timeStart;
    private double timeStop;

    public Annotation(String annotationText, double timeStart, double timeStop){
        this.annotationText = annotationText;
        this.timeStart = timeStart;
        this.timeStop = timeStop;
    }

    public Annotation(Annotation annotation){
        this(annotation.annotationText, annotation.timeStart, annotation.timeStop);
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
                otherTimeStart >= getTimeStop()
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
