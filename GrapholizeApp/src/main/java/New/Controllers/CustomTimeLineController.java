package New.Controllers;

import New.CustomControls.Annotation.MutableSegmentRectangle;
import New.CustomControls.TimeLine.CustomTimeLinePane;
import New.CustomControls.Containers.TimeLineContainer;
import New.CustomControls.Annotation.SegmentRectangle;
import New.CustomControls.TimeLine.SelectableTimeLinePane;
import New.Execptions.NoTimeLineSelectedException;
import New.Model.Entities.Segment;
import New.Model.Entities.Topic;
import New.Observables.*;

import java.util.*;
import java.util.stream.Collectors;

public class CustomTimeLineController {
    private ObservableTopicSet observableTopicSet;
    private ObservablePage page;
    private ObservableTimeLine observableTimeLine;
    private TimeLineContainer parent;

    public CustomTimeLineController(ObservableTopicSet observableTopicSet, ObservablePage page, TimeLineContainer parent) {
        this.observableTopicSet = observableTopicSet;
        this.page = page;
        this.parent = parent;
        this.observableTimeLine = parent.getSelectedTimeLine();
    }

    public void addAnnotation(Segment a){
        page.addSegment(observableTopicSet.getTopicSetID(), a);
    }

    public void editTimeLine(){
        parent.editTimeLine(observableTopicSet);
    }

    public void removeTimeLine(CustomTimeLinePane timeLine){
        if(parent.removeTimeLine(timeLine)){
            observableTimeLine.removeObserver(timeLine);
        }
    }


    public double[] getCombinedAnnotationBoundaries(){
        List<SegmentRectangle> selectedAnnotations = observableTimeLine.getSelectedAnnotations();
        Comparator<SegmentRectangle> comp = Comparator.comparing(SegmentRectangle::getTimeStart);
        Collections.sort(selectedAnnotations, comp);
        double timeStart = selectedAnnotations.get(0).getTimeStart();
        double timeStop = selectedAnnotations.get(selectedAnnotations.size() - 1).getTimeStop();
        return new double[]{timeStart, timeStop};
    }

    public double[] getCombinedDotAnnotationBoundaries(){
        List<ObservableDot> l = page.getObservableStrokes().stream()
                .flatMap(s -> s.getObservableDots().stream())
                .filter(d -> d.isSelected())
                .collect(Collectors.toList());
        return new double[]{l.get(0).getTimeStamp(), l.get(l.size() - 1).getTimeStamp()};
    }

    public List<SegmentRectangle> getSelectedAnnotations(){
        return observableTimeLine.getSelectedAnnotations();
    }

    public boolean copiedAnnotationsCollideWithOtherAnnotations(Optional<double[]> combined){
        //Reason for passing an optional: Boundaries only need to be calculated once. The caller (CustomTimeLinePane)
        //needs the boundaries to create the annotation.
        if(combined.isPresent()){
            return timeRangeCollidesWithOtherAnnotations(combined.get()[0], combined.get()[1]);
        }
        return selectedAnnotationsCollideWithOtherAnnotations();
    }

    public boolean dotSegmentsCollideWithOtherAnnotations(Optional<double[]> combined){
        //Reason for passing an option: Boundaries only need to be calculated once. The caller (CustomTimeLinePane)
        //needs the boundaries to create the annotation.
        if(combined.isPresent()){
            return timeRangeCollidesWithOtherAnnotations(combined.get()[0], combined.get()[1]);
        }
        return selectedDotsCollideWithOtherAnnotations();
    }



    public boolean selectedAnnotationsCollideWithOtherAnnotations(){
        return page.listCollidesWithOtherAnnotations(observableTopicSet.getTopicSetID(), observableTimeLine.getSelectedAnnotations());
    }


    /**
     * Checks if the the annotations that would be created out of the selected dots would collide with any preexisting annotations on this timeline.
     * @return true if a collision is detected, false if no colliction was detected.
     */
    public boolean selectedDotsCollideWithOtherAnnotations(){
        //collide is set to true as soon as any collision is detected.
        //It also serves to end the while loops preemptively if a collision is detected;
        boolean collide = false;

        //Iterate through all strokes.
        //Each stroke needs to be checked for selected dots.
        Iterator<ObservableStroke> it = page.getObservableStrokes().iterator();
        while(!collide && it.hasNext()){
            ObservableStroke s = it.next();
            Iterator<ObservableDot> itDot = s.getObservableDots().iterator();

            //Within a stroke, multiple time segments can be selected.
            //(Time segment = a number of adjacent selected dots.
            //[..............................Stroke................................]
            //   [..Selected..]     [..Selected..]         [..Selected..]

            //withinSelectedSegment determines which of the following values is assigned a value.
            boolean withinSelectedSegment = false;
            long start = 0;
            long stop = 0;
            long previousTimeStamp = 0;

            //The point of this loop is to find all selected time segments within a stroke.
            while(!collide && itDot.hasNext()){
                ObservableDot d = itDot.next();

                if(d.isSelected()){
                    if(!withinSelectedSegment){
                        //The first selected dot of a segment has been found.
                        start = d.getTimeStamp();
                        withinSelectedSegment = true;
                    }
                }
                else if(withinSelectedSegment){
                    //The current dot is not selected but withinSelectedSegment is active.
                    //The end of a dot segment has been found.
                    stop = previousTimeStamp;
                    collide = timeRangeCollidesWithOtherAnnotations(start, stop);
                    withinSelectedSegment = false;
                }
                //update previous timestamp.
                //This is necessary, since the "else if" clause is entered after the last selected dot of the segment has
                //been passed over.
                previousTimeStamp = d.getTimeStamp();
            }
            //In the case that only the last dot of a segment has been selected
            if(withinSelectedSegment){
                stop = start;
                collide = timeRangeCollidesWithOtherAnnotations(start, stop);
            }
        }
        return collide;
    }

    public boolean timeRangeCollidesWithOtherAnnotations(double timeStart, double timeStop){
        return page.collidesWithOtherElements(observableTopicSet.getTopicSetID(), timeStart, timeStop);
    }

    public void createNewTimeLine() throws NoTimeLineSelectedException {
        parent.createNewTimeLineOutOfSelectedElements();
    }
    public void createNewTimeLine(Optional<Segment> combined, boolean selected) throws NoTimeLineSelectedException {
        if(combined.isPresent()){
            parent.createNewTimeLineOutOfCombinedElement(combined.get());
        }
        else{
            if (selected) {
                parent.createNewTimeLineOutOfSelectedElements();
            } else {
                parent.createNewTimeLineOutOfSelectedDots();
            }
        }
    }

    public void createNegativeTimeLine(){
        parent.createNegativeTimeLine(observableTopicSet.getTopicSetID());
    }

    public void removeAnnotation(ObservableSegment a){
        page.removeAnnotation(observableTopicSet.getTopicSetID(), a.getSegment());
    }

    public void filterSelect(SelectableTimeLinePane caller, Map<String, String> topicFilters, List<MutableSegmentRectangle> segments){
        observableTimeLine.setSelectedTimeLine(caller);
        segments.stream()
                .filter(s -> s.fitsCriteria(topicFilters))
                .forEach(s -> s.setSelected(true));
    }

    public void addMissingTopics(List<Topic> topics){
        observableTimeLine.getMissingTopics(observableTopicSet).forEach(topic -> observableTopicSet.addTopic(topic));
        /*
        Optional<ObservableTopicSet> originTopicSet = observableTimeLine.getSelectedSegmentationTopicSet();
        if(originTopicSet.isPresent()){
            for(Topic originSuperSetTopic : originTopicSet.get().getTopicsObservableList()){
                if(observableTopicSet.getTopicsObservableList().stream().noneMatch(topic -> topic.getTopicName().equals(originSuperSetTopic.getTopicName()))){
                    observableTopicSet.addTopic(new Topic(originSuperSetTopic.getTopicName(), observableTopicSet.generateTopicId(originSuperSetTopic.getTopicName())));
                }
            }
        }
         */
    }

    public Set<Segment> generateMissingSegments(){
        return observableTimeLine.generateMissingSegments(observableTopicSet.getTopicsObservableList());
        /*
        List<AnnotationRectangle> selectedAnnotations = getSelectedAnnotations();
        Segment[] res = new Segment[selectedAnnotations.size()];
        for(int i = 0; i < selectedAnnotations.size(); i++){
            AnnotationRectangle a = selectedAnnotations.get(i);
            Segment newSegment = new Segment(a.getTimeStart(), a.getTimeStop());
            if(a instanceof MovableAnnotationRectangle){
                for(Topic t : ((MovableAnnotationRectangle)a).getObservableTopicSet().getTopicsObservableList()){
                    Optional<Topic> optionalTopic = observableTopicSet.getTopicsObservableList().stream().filter(top -> top.getTopicName().equals(t.getTopicName())).findFirst();
                    if(optionalTopic.isPresent()){
                        newSegment.putAnnotation(optionalTopic.get().getTopicID(), ((MovableAnnotationRectangle)a).getObservableSegment().getAnnotation(t.getTopicID()));
                    }
                }
            }
            res[i] = newSegment;
        }
        return res;

         */
    }

}
