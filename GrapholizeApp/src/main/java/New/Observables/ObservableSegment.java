package New.Observables;

import New.Model.Entities.Segment;
import New.Model.Entities.Topic;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.collections.ObservableList;

import java.util.Map;

/**
 * The ObservableSegment is an Observable Model Object which wraps a superSet and exposes some of its
 * attributes as Object properties.
 * Any changes made to the properties are also made to the underlying wrapped object
 */
public class ObservableSegment implements Comparable<ObservableSegment> {
    private Segment innerSegment;

    private DoubleProperty timeStartProperty;
    private DoubleProperty timeStopProperty;
    private StringProperty mainTopicIDProperty;
    private StringProperty mainTopicAnnotationProperty;
    private StringProperty toolTipTextProperty;
    private BooleanProperty selectedProperty;
    private ObservableMap<String, String> observableAnnotationMap;

    private ObservableList<Topic> observableList;

    public ObservableSegment(Segment original, ObservableSuperSet observableSuperSet){
        this.innerSegment = original;
        this.timeStartProperty = new SimpleDoubleProperty(innerSegment.getTimeStart());
        this.timeStartProperty.addListener((observable, oldValue, newValue) -> {
            innerSegment.setTimeStart(newValue.doubleValue());
        });
        this.timeStopProperty = new SimpleDoubleProperty(innerSegment.getTimeStop());
        this.timeStopProperty.addListener((observable, oldValue, newValue) -> innerSegment.setTimeStop(newValue.doubleValue()));
        this.mainTopicIDProperty = new SimpleStringProperty(observableSuperSet.getMainTopicID());
        this.mainTopicIDProperty.bind(observableSuperSet.getMainTopicIDProperty());

        this.mainTopicAnnotationProperty = new SimpleStringProperty(innerSegment.getAnnotation(observableSuperSet.getMainTopicID()));

        this.mainTopicIDProperty.addListener( observable->
                mainTopicAnnotationProperty.set(innerSegment.getAnnotation(mainTopicIDProperty.get()))
        );

        this.observableList = FXCollections.unmodifiableObservableList(observableSuperSet.getTopicsObservableList());
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
        this.observableAnnotationMap = FXCollections.observableMap(innerSegment.getAnnotationsMap());

        this.observableAnnotationMap.addListener((MapChangeListener<String, String>) change -> {
            if(change.getKey().equals(mainTopicIDProperty.get())){
                mainTopicAnnotationProperty.set(innerSegment.getAnnotation(mainTopicIDProperty.get()));
            }
            this.toolTipTextProperty.set(generateToolTipText());
        });
        this.toolTipTextProperty.set(generateToolTipText());

        this.selectedProperty = new SimpleBooleanProperty(false);
    }

    //region Getters and Setters
    public Segment getSegment() {
        return innerSegment;
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
        return innerSegment;
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

    /**
     * Checks if this segment lies within the given timeframe
     * @param start start of timeframe
     * @param stop stop of timeFrame
     * @return true if this segment lies within timeframe, false otherwise
     */
    public boolean isWithinTimeRange(double start, double stop){
        return this.getTimeStart() >= start && this.getTimeStop() <= stop;
    }

    /**
     * Puts the given annotation into the annotation map. (This will also add it to the
     * underlying model object)
     * @param topicID id of the topic for the annotation (key)
     * @param annotation annotation for the given topic (value)
     */
    public void putAnnotation(String topicID, String annotation){
        observableAnnotationMap.put(topicID, annotation);
    }

    /**
     * Removes the annotation with the given id from the map
     * @param topicID topic id of the annotation which will be removed
     */
    public void removeAnnotation(String topicID){
        observableAnnotationMap.remove(topicID);
    }

    public String getAnnotation(String topicID){
        return observableAnnotationMap.get(topicID);
    }

    /**
     * Generates the tooltipText.
     * The tool tip displays all annotations of the segment, as well as its duration.
     * @return String which contains the generated toolTiptext
     */
    public String generateToolTipText(){
        StringBuilder builder = new StringBuilder();
        for(Topic t : observableList){
            builder.append(String.format("%s: %s\n", t.getTopicName(), innerSegment.getAnnotation(t.getTopicID())));
        }
        builder.append(String.format("%s: %s%s", "Duration:", String.valueOf((long)getDuration()), " ms"));
        return builder.toString();
    }

    //endregion

    /**
     * Returns the duration of the segment
     * @return
     */
    public double getDuration(){
        return innerSegment.getDuration();
    }

    /**
     * Checks if the segment fits the given filter criteria
     * @param topicFilters map containing filter criteria where the key is the topic id and the value is the filter value
     * @return true if the segment fits all filter criteria, false if it does not fit all criteria.
     */
    public boolean fitsFilterCriteria(Map<String, String> topicFilters){
        for(String key : topicFilters.keySet()){
            if(!getInnerSegment().getAnnotation(key).equals(topicFilters.get(key))){
                return false;
            }
        }
        return true;
    }

    /**
     * Compares this segment to another segment by comparing their timeStart attribute
     * @param o the other ObservableSegment
     * @return 1 if this Segment's timestart is larger than the other's timeStart, -1 if it is smaller, 0 if they are equal.
     */
    @Override
    public int compareTo(ObservableSegment o) {
        return Double.compare(getTimeStart(), o.getTimeStart());
    }
}
