package New.Observables;

import New.CustomControls.Annotation.SegmentRectangle;
import New.CustomControls.Annotation.MutableSegmentRectangle;
import New.CustomControls.Annotation.SelectableSegmentRectangle;
import New.CustomControls.TimeLine.CustomTimeLinePane;
import New.CustomControls.TimeLine.SelectableTimeLinePane;
import New.Interfaces.Observer.TimeLineObserver;
import New.Model.Entities.Segment;
import New.Model.Entities.Topic;

import java.util.*;
import java.util.stream.Collectors;

public class ObservableTimeLine {
    private SelectableTimeLinePane selectedTimeLine;
    private List<TimeLineObserver> observers;

    public ObservableTimeLine(){
        observers = new LinkedList<>();
    }

    public ObservableTimeLine(SelectableTimeLinePane timeLine){
        this.selectedTimeLine = timeLine;
        observers = new LinkedList<>();
    }

    public boolean timeLineSelected(){
        return selectedTimeLine != null;
    }

    //TODO: Perhaps this mechanism needs to be reworked a bit
    // Could be optimized by making the selection dependant on the observableSegment, rather than the children.
    // The rectangle's selectedProperty binds itself to the OSegment's selected property.
    // This way, you can just search through the list of selectable segments on the page, rather than the children.
    public List<SegmentRectangle> getSelectedAnnotations(){
        //The children first need to be filtered to see whether or not they're actually rectangles (Can also be drag rect or label)
        //Then the nodes acquire the correct cast
        //finally, they're filtered for selection
        return selectedTimeLine.getChildren().stream()
                .filter(node -> node instanceof SelectableSegmentRectangle)
                .map(node -> (SelectableSegmentRectangle)node)
                .filter(SelectableSegmentRectangle::isSelected)
                .collect(Collectors.toList());
    }

    public void setSelectedTimeLine(SelectableTimeLinePane timeLine){
        if(this.selectedTimeLine != timeLine){
            this.selectedTimeLine = timeLine;
            notifyObservers();
        }
    }

    public Optional<ObservableTopicSet> getSelectedSegmentationTopicSet(){
        return selectedTimeLine instanceof CustomTimeLinePane ? Optional.of(((CustomTimeLinePane)selectedTimeLine).getObservableTopicSet()) : Optional.empty();
    }

    public boolean equals(SelectableTimeLinePane timeLinePane){
        return selectedTimeLine == timeLinePane;
    }

    public void addObserver(TimeLineObserver obs) {
        this.observers.add(obs);
    }

    public void removeObserver(TimeLineObserver obs) {
        this.observers.remove(obs);
    }

    public void notifyObservers() {
        for(TimeLineObserver observer : observers){
            observer.update(this);
        }
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
}
