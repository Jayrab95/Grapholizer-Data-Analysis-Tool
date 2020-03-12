package New.Controllers;

import New.CustomControls.Annotation.MutableSegmentRectangle;
import New.CustomControls.TimeLine.CustomSegmentationPane;
import New.CustomControls.Containers.TimeLineContainer;
import New.CustomControls.Annotation.SegmentRectangle;
import New.CustomControls.TimeLine.SelectableSegmentationPane;
import New.Execptions.NoTimeLineSelectedException;
import New.Model.Entities.Segment;
import New.Model.Entities.Topic;
import New.Observables.*;

import java.util.*;
import java.util.stream.Collectors;

public class CustomTimeLineController {
    private ObservableTopicSet observableTopicSet;
    private ObservablePage page;
    private ObservableSegmentation observableSegmentation;
    private TimeLineContainer parent;

    public CustomTimeLineController(ObservableTopicSet observableTopicSet, ObservablePage page, TimeLineContainer parent) {
        this.observableTopicSet = observableTopicSet;
        this.page = page;
        this.parent = parent;
        this.observableSegmentation = parent.getSelectedTimeLine();
    }

    public void addAnnotation(Segment a){
        page.addSegment(observableTopicSet.getTopicSetID(), a);
    }

    public void editTimeLine(){
        parent.editTimeLine(observableTopicSet);
    }

    public void removeTimeLine(CustomSegmentationPane timeLine){
        if(parent.removeTimeLine(timeLine)){
            //observableSegmentation.removeObserver(timeLine);
        }
    }


    public double[] getCombinedAnnotationBoundaries() {
        //Todo: ugly solution...(However the set of observable segments on the segmentationpanes is always a treeset)
        // Unsafe though, in case the collection type is changed..
        TreeSet<ObservableSegment> selectedAnnotations = (TreeSet)observableSegmentation.getSelectedSegments();
        double timeStart = selectedAnnotations.first().getTimeStart();
        double timeStop = selectedAnnotations.last().getTimeStop();
        return new double[]{timeStart, timeStop};
    }

    public double[] getCombinedDotAnnotationBoundaries(){
        List<ObservableDot> l = page.getObservableStrokes().stream()
                .flatMap(s -> s.getObservableDots().stream())
                .filter(d -> d.isSelected())
                .collect(Collectors.toList());
        return new double[]{l.get(0).getTimeStamp(), l.get(l.size() - 1).getTimeStamp()};
    }

    public Set<SegmentRectangle> getSelectedAnnotations() throws NoTimeLineSelectedException {
        return observableSegmentation.getSelectedSegmentRectangles();
    }

    public boolean copiedAnnotationsCollideWithOtherAnnotations(Optional<double[]> combined) {
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

    public boolean negativeSegmentsCollideWithOtherAnnotations(){
        return setCollidesWithOtherAnnotations(getNegativeSegmentsFromSelectedSegmentation());
    }

    public boolean setCollidesWithOtherAnnotations(Set<Segment> segments){
        return segments.stream().anyMatch(segment -> page.collidesWithOtherElements(observableTopicSet.getTopicSetID(), segment.getTimeStart(), segment.getTimeStop()));
    }

    public boolean selectedAnnotationsCollideWithOtherAnnotations() {
        return page.collectionCollidesWithOtherElements(observableTopicSet.getTopicSetID(), observableSegmentation.getSelectedSegmentRectangles());
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

    public void createNewTimeLineOutOfSet(Set<Segment> set){
        parent.createNewTimeLineOutOfSet(set);
    }

    public void createNewTimeLine(Optional<Segment> combined, boolean selected) throws NoTimeLineSelectedException {
        if(combined.isPresent()){
            parent.createNewTimeLineOutOfSet(Set.of(combined.get()));
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

    public Set<Segment> getNegativeSegmentsFromSelectedSegmentation() {
        Optional<ObservableTopicSet> optional = observableSegmentation.getSelectedSegmentationTopicSet();
        if(optional.isPresent()){
            return page.getNegativeSegmentation(optional.get().getTopicSetID());
        }
        return Set.of();
    }

    public void removeAnnotation(ObservableSegment a){
        page.removeAnnotation(observableTopicSet.getTopicSetID(), a.getSegment());
    }

    public void filterSelect(SelectableSegmentationPane caller, Map<String, String> topicFilters, List<MutableSegmentRectangle> segments){
        observableSegmentation.setSelectedTimeLine(caller);
        segments.stream()
                .filter(s -> s.fitsCriteria(topicFilters))
                .forEach(s -> s.setSelected(true));
    }

    public void addMissingTopics(List<Topic> topics){
        observableSegmentation.getMissingTopics(observableTopicSet).forEach(topic -> observableTopicSet.addTopic(topic));
    }

    public Set<Segment> generateMissingSegments(){
        return observableSegmentation.generateMissingSegments(observableTopicSet.getTopicsObservableList());
    }

    public void deleteSelectedRectangles(){
        for(SegmentRectangle sr : observableSegmentation.getSelectedSegmentRectangles()){
            ((MutableSegmentRectangle)sr).deleteSegment();
        }
    }

}
