package New.Controllers;

import New.CustomControls.SegmentRectangles.MutableSegmentRectangle;
import New.CustomControls.SegmentationPanes.CustomSegmentationPane;
import New.CustomControls.Containers.SegmentationContainer;
import New.CustomControls.SegmentRectangles.SegmentRectangle;
import New.CustomControls.SegmentationPanes.SelectableSegmentationPane;
import New.Execptions.NoDotsSelectedException;
import New.Execptions.NoSegmentationSelectedException;
import New.Model.Entities.Segment;
import New.Model.Entities.Topic;
import New.Observables.*;

import java.util.*;
import java.util.stream.Collectors;

public class CustomSegmentationController {
    private ObservableSuperSet observableSuperSet;
    private ObservablePage observablePage;
    private ObservableSegmentation observableSegmentation;
    private SegmentationContainer parent;

    public CustomSegmentationController(ObservableSuperSet observableSuperSet, ObservablePage page, SegmentationContainer parent) {
        this.observableSuperSet = observableSuperSet;
        this.observablePage = page;
        this.parent = parent;
        this.observableSegmentation = parent.getSelectedSegmentation();
    }

    public void addAnnotation(Segment a){
        observablePage.addSegment(observableSuperSet.getTopicSetID(), a);
    }

    public void editTimeLine(){
        parent.editSegmentation(observableSuperSet);

    }

    /**
     * Forces the SegmentationContainer to remove this segmentation
     * @param segmentation segmentation that needs to be deleted (usually, this)
     */
    public void removeTimeLine(CustomSegmentationPane segmentation){
        if(parent.removeSegmentation(segmentation)){
            //observableSegmentation.removeObserver(segmentation);
        }
    }

    /**
     * Returns the dragbounds  for the given x value
     * @param x x position for which the drag bounds need to be determined
     * @return double array containing two values, where the first value is the left drag border and the second is the right drag border.
     */
    public double[] getDragBounds(double x){
        return observablePage.getBounds(x, observableSuperSet.getTopicSetID());
    }

    /**
     * Returns the combined boundaries of the currently selected segments.
     * @return a double array containing two double values. The first is the time start of the earliest selected segment, the second
     * is the time end of the latest selected segment on the timeline.
     */
    public double[] getCombinedAnnotationBoundaries() throws NoSegmentationSelectedException {
        TreeSet<ObservableSegment> selectedAnnotations = observableSegmentation.getSelectedSegments();
        double timeStart = selectedAnnotations.first().getTimeStart();
        double timeStop = selectedAnnotations.last().getTimeStop();
        return new double[]{timeStart, timeStop};
    }

    /**
     * Returns the total boundaries of the current canvas selection
     * @return a double array containing two double values. The first is the timestamp of the earliest selected dot
     * and the second is the timestamp of the latest selected dot.
     */
    public double[] getCombinedDotAnnotationBoundaries() throws NoDotsSelectedException {
        List<ObservableDot> l = observablePage.getObservableStrokes().stream()
                .flatMap(s -> s.getObservableDots().stream())
                .filter(d -> d.isSelected())
                .collect(Collectors.toList());
        if(l.isEmpty()){throw new NoDotsSelectedException();}
        return new double[]{l.get(0).getTimeStamp(), l.get(l.size() - 1).getTimeStamp()};
    }

    /**
     *
     * @return Returns a set of selected segment rectangles on the selected segmentation
     * @throws NoSegmentationSelectedException if no segmentation is selected
     */
    public Set<SegmentRectangle> getSelectedAnnotations() throws NoSegmentationSelectedException {
        return observableSegmentation.getSelectedSegmentRectangles();
    }




    /**
     * Checks if the segments contained in the given set collied with any preexisting segments
     * @param segments set of segments that needs to be checked for collisions on segmentation
     * @return true if at least one segment collides with a preexisting segment, false, if none of them collide.
     */
    public boolean setCollidesWithOtherAnnotations(Set<Segment> segments){
        return segments.stream().anyMatch(segment -> observablePage.collidesWithOtherElements(observableSuperSet.getTopicSetID(), segment.getTimeStart(), segment.getTimeStop()));
    }

    public boolean selectedAnnotationsCollideWithOtherAnnotations() {
        return observablePage.collectionCollidesWithOtherElements(observableSuperSet.getTopicSetID(), observableSegmentation.getSelectedSegmentRectangles());
    }


    /**
     * Checks if the the segments that would be created out of the selected dots would collide with any preexisting segments on this segmentation.
     * @return true if a collision is detected, false if no colliction was detected.
     */
    public boolean selectedDotsCollideWithOtherAnnotations(){
        //collide is set to true as soon as any collision is detected.
        //It also serves to end the while loops preemptively if a collision is detected;
        boolean collide = false;

        //Iterate through all strokes.
        //Each stroke needs to be checked for selected dots.
        Iterator<ObservableStroke> it = observablePage.getObservableStrokes().iterator();
        while(!collide && it.hasNext()){
            ObservableStroke s = it.next();
            Iterator<ObservableDot> itDot = s.getObservableDots().iterator();

            //Within a stroke, multiple stroke sections can be selected.
            //(stroke section = a number of adjacent selected dots.
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

    /**
     * Checks if the segmentation has any segments within the given timeframe
     * @param timeStart start of the timeframe
     * @param timeStop stop of the timeframe
     * @return true, if the timeframe contains segments, false if no segment collides with the time frame.
     */
    public boolean timeRangeCollidesWithOtherAnnotations(double timeStart, double timeStop){
        return observablePage.collidesWithOtherElements(observableSuperSet.getTopicSetID(), timeStart, timeStop);
    }

    /**
     * Creates new segmentation out of selected segments
     * @throws NoSegmentationSelectedException if no segmentation is selected.
     */
    public void createNewSegmentationOutOfSelectedSegments() throws NoSegmentationSelectedException {
        parent.createNewSegmentationOutOfSelectedElements();
    }

    /**
     * Creates a new segmentation out of a set of segments
     * @param set set of segments that are to be added to new segmentation
     */
    public void createNewTimeLineOutOfSet(Set<Segment> set){
        parent.createNewSegmentationOutOfSet(set);
    }


    /**
     * create new segmentation out of selected dot sections
     */
    public void createNewSegmentationOutOfDots() {
        parent.createNewSegmentationOutOfSelectedDots();
    }

    /**
     * Creates a new segmentation out of a single segment
     * @param s segment which will be added to new segmentation
     */
    public void createSegmentationOutOfSegment(Segment s){
        parent.createNewSegmentationOutOfSet(Set.of(s));
    }

    /**
     * creates negative segmentation (pause segmentation) out of this segmentation
     */
    public void createNegativeTimeLine(){
        parent.createNegativeSegmentation(observableSuperSet.getTopicSetID());
    }

    /**
     * Creates a set of negative segments from the selected segmentation
     * @return set of negative segments from the selected segmentation
     */
    public Set<Segment> getNegativeSegmentsFromSelectedSegmentation() {
        Optional<ObservableSuperSet> optional = observableSegmentation.getSelectedSegmentationTopicSet();
        if(optional.isPresent()){
            return observablePage.getNegativeSegmentation(optional.get().getTopicSetID());
        }
        return Set.of();
    }

    /**
     * Removes the given segmentation from the page.
     * @param a segment to delete
     */
    public void removeAnnotation(ObservableSegment a){
        observablePage.removeAnnotation(observableSuperSet.getTopicSetID(), a.getSegment());
    }

    /**
     * Selects all segments which adhere to the filter. Note that this action will also select the segmentation who calls it.
     * @param caller segmentation who calls action (usually segmentation who holds this controller)
     * @param topicFilters Map of filter criteria where the key is the topic id and the value is the filter
     * @param segments list of segment rectangles which need to be checked for filter criteria
     */
    public void filterSelect(SelectableSegmentationPane caller, Map<String, String> topicFilters, List<MutableSegmentRectangle> segments){
        observableSegmentation.setSelectedTimeLine(caller);
        segments.stream()
                .filter(s -> s.fitsCriteria(topicFilters))
                .forEach(s -> s.setSelected(true));
    }

    /**
     * Adds any topics that are in the selected segmentation but not in thi segmentation into the topic set
     * @param topics
     */
    public void addMissingTopics(List<Topic> topics){
        observableSegmentation.getMissingTopics(observableSuperSet).forEach(topic -> observableSuperSet.addTopic(topic));
    }

    /**
     * Creates a set of new segments, that are copies of the selected segments but also copy the annotations
     * @return
     */
    public Set<Segment> generateMissingSegments(){
        return observableSegmentation.generateMissingSegments(observableSuperSet.getTopicsObservableList());
    }

}
