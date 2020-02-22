package New.Controllers;

import New.CustomControls.Annotation.MovableAnnotationRectangle;
import New.CustomControls.Annotation.SelectableAnnotationRectangle;
import New.CustomControls.TimeLine.CustomTimeLinePane;
import New.CustomControls.Containers.TimeLineContainer;
import New.CustomControls.Annotation.AnnotationRectangle;
import New.CustomControls.TimeLine.SelectableTimeLinePane;
import New.Execptions.NoTimeLineSelectedException;
import New.Model.Entities.Segment;
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
        List<AnnotationRectangle> selectedAnnotations = observableTimeLine.getSelectedAnnotations();
        Comparator<AnnotationRectangle> comp = Comparator.comparing(AnnotationRectangle::getTimeStart);
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

    public List<AnnotationRectangle> getSelectedAnnotations(){
        return observableTimeLine.getSelectedAnnotations();
    }

    public boolean copiedAnnotationsCollideWithOtherAnnotations(Optional<double[]> combined){
        //Reason for passing an option: Boundaries only need to be calculated once. The caller (CustomTimeLinePane)
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
    public void createNewTimeLine(Optional<Segment> combined) throws NoTimeLineSelectedException {
        if(combined.isPresent()){
            parent.createNewTimeLineOutOfCombinedElement(combined.get());
        }
        else{
            parent.createNewTimeLineOutOfSelectedElements();
        }
    }

    public void removeAnnotation(ObservableSegment a){
        page.removeAnnotation(observableTopicSet.getTopicSetID(), a.getSegment());
    }

    public void filterSelect(SelectableTimeLinePane caller, Map<String, String> topicFilters, List<MovableAnnotationRectangle> segments){
        observableTimeLine.setSelectedTimeLine(caller);
        segments.stream()
                .filter(s -> s.fitsCriteria(topicFilters))
                .forEach(s -> s.setSelected(true));
    }

}
