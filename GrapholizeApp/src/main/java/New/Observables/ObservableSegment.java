package New.Observables;

import New.Model.Entities.Segment;
import New.Model.Entities.Topic;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.collections.ObservableList;

import java.util.Map;

public class ObservableSegment implements Comparable<ObservableSegment> {
    private Segment segment;

    private DoubleProperty timeStartProperty;
    private DoubleProperty timeStopProperty;
    private StringProperty mainTopicIDProperty;
    private StringProperty mainTopicAnnotationProperty;
    private StringProperty toolTipTextProperty;
    private BooleanProperty selectedProperty;
    private ObservableMap<String, String> observableAnnotationMap;

    private ObservableList<Topic> observableList;

    public ObservableSegment(Segment original, ObservableTopicSet observableTopicSet){
        this.segment = original;
        this.timeStartProperty = new SimpleDoubleProperty(segment.getTimeStart());
        this.timeStartProperty.addListener((observable, oldValue, newValue) -> {
            segment.setTimeStart(newValue.doubleValue());
        });
        this.timeStopProperty = new SimpleDoubleProperty(segment.getTimeStop());
        this.timeStopProperty.addListener((observable, oldValue, newValue) -> segment.setTimeStop(newValue.doubleValue()));
        this.mainTopicIDProperty = new SimpleStringProperty(observableTopicSet.getMainTopicID());
        this.mainTopicIDProperty.bind(observableTopicSet.getMainTopicIDProperty());

        this.mainTopicAnnotationProperty = new SimpleStringProperty(segment.getAnnotation(observableTopicSet.getMainTopicID()));

        this.mainTopicIDProperty.addListener((observable, oldValue, newValue) ->
                mainTopicAnnotationProperty.set(segment.getAnnotation(newValue))
        );

        this.observableList = FXCollections.unmodifiableObservableList(observableTopicSet.getTopicsObservableList());
        /*
        Eager removal of topics.
        observableTopicSet.getTopicsObservableList().addListener((ListChangeListener<Topic>) c -> {
            while(c.next()){
                for(Topic t : c.getRemoved()){
                    removeAnnotation(t.getTopicID());
                }
            }
        });

         */

        this.toolTipTextProperty = new SimpleStringProperty();
        this.observableAnnotationMap = FXCollections.observableMap(segment.getAnnotationsMap());

        this.observableAnnotationMap.addListener((MapChangeListener<String, String>) change -> {
            if(change.getKey().equals(mainTopicIDProperty.get())){
                mainTopicAnnotationProperty.set(segment.getAnnotation(mainTopicIDProperty.get()));
            }
            this.toolTipTextProperty.set(generateToolTipText());
        });
        this.toolTipTextProperty.set(generateToolTipText());

        this.selectedProperty = new SimpleBooleanProperty(false);
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

    public StringProperty getToolTipTextProperty(){
        return this.toolTipTextProperty;
    }

    public boolean isSelected(){
        return selectedProperty.get();
    }

    public void setSelected(boolean selected){
        this.selectedProperty.set(selected);
    }

    public void toggleSelected(){
        setSelected(!isSelected());
    }

    public BooleanProperty getSelectedProperty(){
        return this.selectedProperty;
    }

    public boolean isWithinTimeRange(double start, double stop){
        return this.getTimeStart() >= start && this.getTimeStop() <= stop;
    }

    public void putAnnotation(String topicID, String annotation){
        /*if(mainTopicIDProperty.get().equals(topicID)){
            mainTopicAnnotationProperty.set(annotation);
        }
         */
        observableAnnotationMap.put(topicID, annotation);
        //segment.putAnnotation(topicID, annotation);
    }

    private void removeAnnotation(String topicID){
        observableAnnotationMap.remove(topicID);
        //segment.removeAnnotation(topicID);
    }

    public String getAnnotation(String topicID){
        return observableAnnotationMap.get(topicID);
    }

    public String generateToolTipText(){
        StringBuilder builder = new StringBuilder();
        for(Topic t : observableList){
            builder.append(String.format("%s: %s\n", t.getTopicName(), segment.getAnnotation(t.getTopicID())));
        }
        builder.append(String.format("%s: %s%s", "Duration:", String.valueOf((long)getDuration()), " ms"));
        return builder.toString();
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

    public boolean fitsFilterCriteria(Map<String, String> topicFilters){
        for(String key : topicFilters.keySet()){
            if(!getInnerSegment().getAnnotation(key).equals(topicFilters.get(key))){
                return false;
            }
        }
        return true;
    }

    @Override
    public int compareTo(ObservableSegment o) {
        return Double.compare(getTimeStart(), o.getTimeStart());
    }
    //endregion
}
