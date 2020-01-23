package New.Observables;

import New.Model.Entities.Segment;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ObservableAnnotation {
    private Segment segment;

    private StringProperty annotationTextProperty;
    private DoubleProperty timeStartProperty;
    private DoubleProperty timeStopProperty;

    public ObservableAnnotation(Segment original){
        this.segment = original;
        annotationTextProperty = new SimpleStringProperty(original.getAnnotationText());
        annotationTextProperty.addListener((observable, oldValue, newValue) -> segment.setAnnotationText(newValue));
        timeStartProperty = new SimpleDoubleProperty(original.getTimeStart());
        timeStartProperty.addListener((observable, oldValue, newValue) -> segment.setTimeStart(newValue.doubleValue()));
        timeStopProperty = new SimpleDoubleProperty(original.getTimeStop());
        timeStopProperty.addListener((observable, oldValue, newValue) -> segment.setTimeStop(newValue.doubleValue()));
    }

    //region Getters and Setters
    public Segment getSegment() {
        return segment;
    }

    public ObservableAnnotation setSegment(Segment segment) {
        this.segment = segment;
        return this;
    }

    public String getAnnotationText() {
        return annotationTextProperty.get();
    }

    public StringProperty getAnnotationTextProperty() {
        return annotationTextProperty;
    }

    public void setAnnotationText(String annotationTextProperty) {
        this.annotationTextProperty.set(annotationTextProperty);
    }

    public double getTimeStart() {
        return timeStartProperty.get();
    }

    public DoubleProperty getTimeStartProperty() {
        return timeStartProperty;
    }

    public void setTimeStart(double timeStartProperty) {
        this.timeStartProperty.set(timeStartProperty);
    }

    public double getTimeStop() {
        return timeStopProperty.get();
    }

    public DoubleProperty getTimeStopProperty() {
        return timeStopProperty;
    }

    public void setTimeStop(double timeStopProperty) {
        this.timeStopProperty.set(timeStopProperty);
    }
    //endregion

    //region annotation accessors
    public double getDuration(){
        return segment.getDuration();
    }
    public boolean collidesWith(double otherTimeStart, double otherTimeStop){
        return segment.collidesWith(otherTimeStart, otherTimeStop);
    }
    public boolean timeStampWithinTimeRange(double timeStamp){
        return segment.timeStampWithinTimeRange(timeStamp);
    }
    public void move(double delta){
        segment.move(delta);
    }
    //endregion
}
