package New.Observables;

import New.CustomControls.Annotation.SegmentRectangle;
import New.CustomControls.Annotation.MutableSegmentRectangle;
import New.CustomControls.Annotation.SelectableSegmentRectangle;
import New.CustomControls.TimeLine.CustomTimeLinePane;
import New.CustomControls.TimeLine.SelectableTimeLinePane;
import New.CustomControls.TimeLine.UnmodifiableSelectableTimeLinePane;
import New.Execptions.NoTimeLineSelectedException;
import New.Model.Entities.Segment;
import New.Model.Entities.Topic;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

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
    public Set<SegmentRectangle> getSelectedSegmentRectangles() {
        //The children first need to be filtered to see whether or not they're actually rectangles (Can also be drag rect or label)
        //Then the nodes acquire the correct cast
        //finally, they're filtered for selection
        if(timeLineSelected()){
            return selectedTimeLine.get().getChildren().stream()
                    .filter(node -> node instanceof SelectableSegmentRectangle)
                    .map(node -> (SelectableSegmentRectangle)node)
                    .filter(SelectableSegmentRectangle::isSelected)
                    .collect(Collectors.toSet());
        }
        return Set.of();
    }

    public Set<ObservableSegment> getSelectedSegments() {
        if(timeLineSelected()){
            return selectedTimeLine.get().getObservableSegments().stream()
                    .filter(observableSegment -> observableSegment.isSelected())
                    .collect(Collectors.toSet());
        }
        return Set.of();
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
        //TODO: Unclean solution. Perhaps there needs to be some other class or interface that defines that the segmentationPane has a topic set
        // (Which is currently not a given if it is a selecteableSegmentationPane)
        if(selectedTimeLine.get() instanceof CustomTimeLinePane){
            return Optional.of(((CustomTimeLinePane)selectedTimeLine.get()).getObservableTopicSet());
        }
        else if(selectedTimeLine.get() instanceof UnmodifiableSelectableTimeLinePane){
            return Optional.of(((UnmodifiableSelectableTimeLinePane)selectedTimeLine.get()).getObservableTopicSet());
        }
        return Optional.empty();
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
        Set<SegmentRectangle> selectedAnnotations = getSelectedSegmentRectangles();
        Set<Segment> res = new TreeSet<>();
        Iterator<SegmentRectangle> it = selectedAnnotations.iterator();
        while(it.hasNext()){
            SegmentRectangle a = it.next();
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
            for(SegmentRectangle sr : getSelectedSegmentRectangles()){
                MutableSegmentRectangle msr = (MutableSegmentRectangle)sr;
                ((CustomTimeLinePane)selectedTimeLine.get()).deleteSegment(msr, msr.getObservableSegment());
            }

        }
    }
}
