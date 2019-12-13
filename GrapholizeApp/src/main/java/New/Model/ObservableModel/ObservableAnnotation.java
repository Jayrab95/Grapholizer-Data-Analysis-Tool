package New.Model.ObservableModel;

import New.Model.Entities.Annotation;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ObservableAnnotation {
    private Annotation annotation;

    private StringProperty annotationTextProperty;
    private DoubleProperty timeStartProperty;
    private DoubleProperty timeStopProperty;

    public ObservableAnnotation(Annotation original){
        this.annotation = original;
        annotationTextProperty = new SimpleStringProperty(original.getAnnotationText());
        annotationTextProperty.addListener((observable, oldValue, newValue) -> annotation.setAnnotationText(newValue));
        timeStartProperty = new SimpleDoubleProperty(original.getTimeStart());
        timeStartProperty.addListener((observable, oldValue, newValue) -> annotation.setTimeStart(newValue.doubleValue()));
        timeStopProperty = new SimpleDoubleProperty(original.getTimeStop());
        timeStopProperty.addListener((observable, oldValue, newValue) -> annotation.setTimeStop(newValue.doubleValue()));
    }

    //region Getters and Setters
    public Annotation getAnnotation() {
        return annotation;
    }

    public ObservableAnnotation setAnnotation(Annotation annotation) {
        this.annotation = annotation;
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
        return annotation.getDuration();
    }
    public boolean collidesWith(double otherTimeStart, double otherTimeStop){
        return annotation.collidesWith(otherTimeStart, otherTimeStop);
    }
    public boolean timeStampWithinTimeRange(double timeStamp){
        return annotation.timeStampWithinTimeRange(timeStamp);
    }
    public void move(double delta){
        annotation.move(delta);
    }
    //endregion
}
