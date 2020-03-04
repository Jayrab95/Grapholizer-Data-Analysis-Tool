package New.Observables;

import New.CustomControls.Annotation.SegmentRectangle;
import New.CustomControls.Annotation.MutableSegmentRectangle;
import New.CustomControls.Annotation.SelectableSegmentRectangle;
import New.CustomControls.TimeLine.CustomTimeLinePane;
import New.CustomControls.TimeLine.SelectableTimeLinePane;
import New.CustomControls.TimeLine.TimeLinePane;
import New.Interfaces.Observer.TimeLineObserver;
import New.Model.Entities.Segment;
import New.Model.Entities.Topic;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;

import java.util.*;
import java.util.stream.Collectors;

public class ObservableSegmentation {

    private ObjectProperty<SelectableTimeLinePane> selectedTimeLine;

    public ObservableSegmentation(){
        this.selectedTimeLine = new SimpleObjectProperty<>();
    }

    public ObservableSegmentation(SelectableTimeLinePane timeLine){
        this.selectedTimeLine = new SimpleObjectProperty<>(timeLine);
    }

    public ObjectProperty<SelectableTimeLinePane> getSelectedTimeLineProperty(){
        return this.selectedTimeLine;
    }

    public SelectableTimeLinePane getSelectedTimeLine() {
        return selectedTimeLine.get();
    }

    public boolean timeLineSelected(){
        return selectedTimeLine.isNotNull().get();
    }

    //TODO: Perhaps this mechanism needs to be reworked a bit
    // Could be optimized by making the selection dependant on the observableSegment, rather than the children.
    // The rectangle's selectedProperty binds itself to the OSegment's selected property.
    // This way, you can just search through the list of selectable segments on the page, rather than the children.
    public List<SegmentRectangle> getSelectedAnnotations(){
        //The children first need to be filtered to see whether or not they're actually rectangles (Can also be drag rect or label)
        //Then the nodes acquire the correct cast
        //finally, they're filtered for selection
        return selectedTimeLine.get().getChildren().stream()
                .filter(node -> node instanceof SelectableSegmentRectangle)
                .map(node -> (SelectableSegmentRectangle)node)
                .filter(SelectableSegmentRectangle::isSelected)
                .collect(Collectors.toList());
    }

    public void setSelectedTimeLine(SelectableTimeLinePane timeLine){
        if(this.selectedTimeLine.get() != timeLine){
            this.selectedTimeLine.set(timeLine);
        }
    }

    public String getSegmentationName(){
        return selectedTimeLine.get().getTimeLineName();
    }

    public Optional<ObservableTopicSet> getSelectedSegmentationTopicSet(){
        return selectedTimeLine.get() instanceof CustomTimeLinePane ? Optional.of(((CustomTimeLinePane)selectedTimeLine.get()).getObservableTopicSet()) : Optional.empty();
    }

    public boolean equals(SelectableTimeLinePane timeLinePane){
        return selectedTimeLine.get() == timeLinePane;
    }

    public List<Topic> getMissingTopics(ObservableTopicSet targetTopicSet){
        Optional<ObservableTopicSet> optional = getSelectedSegmentationTopicSet();
        if(optional.isPresent()){
            //If the selected segmentation has a topic set (ie. it is not the stroke timeline), then
            //return a list of all topics that are missing from the target set (topics, whose >name< does
            //not appear in the target topicSet.
            return getSelectedSegmentationTopicSet().get().getTopicsObservableList().stream()
                    .filter(originTopic -> targetTopicSet.getTopicsObservableList().stream().noneMatch(targetTopic -> targetTopic.getTopicName().equals(originTopic.getTopicName())))
                    .map(originTopic -> new Topic(originTopic.getTopicName(), targetTopicSet.generateTopicId(originTopic.getTopicName())))
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    public Set<Segment> generateMissingSegments(List<Topic> targetTopics){
        List<SegmentRectangle> selectedAnnotations = getSelectedAnnotations();
        Set<Segment> res = new TreeSet<>();
        for(int i = 0; i < selectedAnnotations.size(); i++){
            SegmentRectangle a = selectedAnnotations.get(i);
            Segment newSegment = new Segment(a.getTimeStart(), a.getTimeStop());
            if(a instanceof MutableSegmentRectangle){
                for(Topic t : ((MutableSegmentRectangle)a).getObservableTopicSet().getTopicsObservableList()){
                    Optional<Topic> optionalTopic = targetTopics.stream().filter(top -> top.getTopicName().equals(t.getTopicName())).findFirst();
                    if(optionalTopic.isPresent()){
                        newSegment.putAnnotation(optionalTopic.get().getTopicID(), ((MutableSegmentRectangle)a).getObservableSegment().getAnnotation(t.getTopicID()));
                    }
                }
            }
            res.add(newSegment);
        }
        return res;
    }

    public boolean selectedSegmentationIsCustom(){
        return selectedTimeLine.get() instanceof CustomTimeLinePane;
    }

    public void deleteSelectedSegments(){
        if(selectedSegmentationIsCustom()){
            for(SegmentRectangle sr : getSelectedAnnotations()){
                MutableSegmentRectangle msr = (MutableSegmentRectangle)sr;
                ((CustomTimeLinePane)selectedTimeLine.get()).deleteSegment(msr, msr.getObservableSegment());
            }

        }
    }
}
