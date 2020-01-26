package New.Observables;

import New.Model.Entities.Segment;
import New.Model.Entities.Topic;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ListChangeListener;

public class ObservableSegment {
    private Segment segment;

    private DoubleProperty timeStartProperty;
    private DoubleProperty timeStopProperty;
    private StringProperty mainTopicIDProperty;
    private StringProperty mainTopicAnnotationProperty;

    public ObservableSegment(Segment original, ObservableTopicSet observableTopicSet){
        this.segment = original;
        this.timeStartProperty = new SimpleDoubleProperty(original.getTimeStart());
        this.timeStartProperty.addListener((observable, oldValue, newValue) -> segment.setTimeStart(newValue.doubleValue()));
        this.timeStopProperty = new SimpleDoubleProperty(original.getTimeStop());
        this.timeStopProperty.addListener((observable, oldValue, newValue) -> segment.setTimeStop(newValue.doubleValue()));
        this.mainTopicAnnotationProperty = new SimpleStringProperty(segment.getAnnotation(observableTopicSet.getMainTopicID()));

        observableTopicSet.getMainTopicIDProperty().addListener((observable, oldValue, newValue) ->
                mainTopicAnnotationProperty.set(segment.getAnnotation(newValue))
        );
        observableTopicSet.getTopicsObservableList().addListener((ListChangeListener<Topic>) c -> {
            for(Topic t : c.getRemoved()){
                removeAnnotation(t.getTopicID());
            }
        });
    }

    //region Getters and Setters
    public Segment getSegment() {
        return segment;
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

    public Segment getInnerSegment(){
        return segment;
    }

    public StringProperty getMainTopicAnnotationProperty(){
        return this.mainTopicAnnotationProperty;
    }

    public void putAnnotation(String topicID, String annotation){
        if(mainTopicIDProperty.get().equals(topicID)){
            mainTopicAnnotationProperty.set(annotation);
        }
        segment.putAnnotation(topicID, annotation);
    }

    private void removeAnnotation(String topicID){
        segment.removeAnnotation(topicID);
    }

    public String getAnnotation(String topicID){
        return segment.getAnnotation(topicID);
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
