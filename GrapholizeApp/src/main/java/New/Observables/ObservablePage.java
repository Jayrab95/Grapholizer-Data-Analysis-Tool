package New.Observables;

import New.CustomControls.SegmentRectangles.SegmentRectangle;
import New.Interfaces.Selector;
import New.Model.Entities.*;
import New.util.PageUtil;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The ObservablePage class is a Observable Singleton object. The goal is to only have one active ObservablePage during the runtime.
 * The ObservablePage class holds a reference to a page object as an ObjectProperty
 * The listeners of this property are notified if the reference changes.
 */
public class ObservablePage implements Selector {

    private List<ObservableStroke> strokes;

    private final ObjectProperty<Page> inner;

    public ObservablePage(Page inner){
        strokes = new ArrayList(generateStrokes(inner));
        this.inner = new SimpleObjectProperty();
        this.inner.set(inner);

    }

    public ObservablePage(ObservablePage p){
        this(p.inner.get());
    }


    /**
     * Returns the object property which wrapps the inner page
     * @return
     */
    public ObjectProperty<Page> getPageProperty(){
        return inner;
    }

    /**
     * Sets the new active page. This will notify all listeners of the Object property.
     * @param newPage new page object
     */
    public void setPage(Page newPage){
        strokes = FXCollections.observableList(generateStrokes(newPage));
        this.inner.set(newPage);
    }

    /**
     * Returns the dot sections of the segments for the given superset id.
     * @param topicSetID super set id of the segmentation
     * @return all corresponding dot sections.
     */
    public List<List<Dot>> getAllDotSectionsForTopicSet(String topicSetID){
        if(topicSetID.equals("Stroke duration"))
        {
            return inner.get().getStrokes().stream()
                    .map(stroke -> stroke.getDots())
                    .collect(Collectors.toList());
        }
        return PageUtil.getDotSectionsForAnnotations(inner.get().getSegmentation(topicSetID), inner.get().getStrokes());
    }

    public List<Stroke> getAllStrokes(){
        return inner.get().getStrokes();
    }

    public void setPage(ObservablePage p){
        this.setPage(p.inner.get());
    }

    public void addSegment(String topicSetKey, Segment a){
        inner.get().putSegmentIntoSegmentation(topicSetKey, a);
    }

    public void removeAnnotation(String topicSetKey, Segment a){
        inner.get().removeSegmentFromSegmentation(topicSetKey, a);
    }

    /**
     * Checks if the segmentation map contains an entry for the given ID.
     * @param setID id that needs to be checked
     * @return true if the map contains the key, false if not.
     */
    public boolean containsSetID(String setID){
        return inner.get().getSegmentationsMap().containsKey(setID);
    }

    /**
     * Returns an optional segmentation (Set of segments) that is stored under the given ID.
     * If no segmentation is stored under the given id, the optional is empty.
     * @param superSetID superSetID of the segmentation
     * @return Optional of a set of segments if a segmentation is present under the given key, or an empty optional if no segmentation is present.
     */
    public Optional<Set<Segment>> getAnnotationSet(String superSetID){
        if(containsSetID(superSetID)){
            return Optional.of(inner.get().getSegmentation(superSetID));
        }
        return Optional.empty();
    }

    /**
     * Checks if the given timeframe collides with any segments in the given segmentation.
     * @param timeLineKey supersetID of the segmentation
     * @param timeStart start of the timeframe
     * @param timeStop end of the timeFrame
     * @return true if a collision has been detected and false if no collision was detected.
     */
    public boolean collidesWithOtherElements(String timeLineKey, double timeStart, double timeStop){
        return inner.get().getSegmentation(timeLineKey).stream()
                .filter(annotation -> annotation.collidesWith(timeStart, timeStop))
                .count() > 0;
    }

    /**
     * Checks if the given set of Segments collides with any other segments in the given super set
     * @param key superSet id of the segmentation
     * @param annotations set of annotationRectangles that need to be checked for collision
     * @return true if collision was detected, false if not.
     */
    public boolean collectionCollidesWithOtherElements(String key, Collection<SegmentRectangle> annotations){
        return annotations.stream()
                .filter(a -> collidesWithOtherElements(key, a.getTimeStart(), a.getTimeStop()))
                .count() > 0;
    }


    /**
     * Creates a list of all selected dot segments.
     * @return a list of segments of dots which are currently selected.
     */
    public List<List<ObservableDot>> getSelectedDotSections(){
        List<List<ObservableDot>> res = new LinkedList<>();
        for (ObservableStroke s : getObservableStrokes()){
            //foundSegment marks whether the loop is currently within a segment of selected dots.
            boolean foundSegment = false;
            List<ObservableDot> seg = new LinkedList<>();

            for(int i = 0; i < s.getObservableDots().size(); i++){
                if(s.getObservableDots().get(i).isSelected()){
                    if(!foundSegment){
                        //The first selecte dot of a segment has been found.
                        seg = new LinkedList<>();
                        foundSegment = true;
                    }
                    seg.add(s.getObservableDots().get(i));
                    if(i == (s.getObservableDots().size() - 1) && seg.size() > 1){
                        //Add the segment to the result list if the end of the loop has been reached.
                        res.add(new LinkedList(seg));
                    }
                }
                else{
                    if(foundSegment){
                        //The end of a segment has been reached
                        foundSegment = false;
                        //This dot marks the end of a segment and needs to be added.
                        //This way, there can't be segments which only consist of 1 dot.
                        seg.add(s.getObservableDots().get(i));
                        //A segment needs to consist of at least 2 dots, otherwise, the segment will not be visible.
                        if(seg.size() > 1){
                            res.add(new LinkedList(seg));
                        }
                    }
                }
            }
        }
        return res;
    }

    private List<ObservableStroke> generateStrokes(Page p){
        List<ObservableStroke> observableStrokes = new LinkedList<>();
        for(Stroke s : p.getStrokes()){
            observableStrokes.add(new ObservableStroke(s, Color.BLACK));
        }
        return observableStrokes;
    }

    public List<ObservableStroke> getObservableStrokes(){
        return this.strokes;
    }

    public PageMetaData getPageMetaData(){
        return inner.get().getPageMetaData();
    }

    public double getDuration(){return inner.get().getPageDuration();}

    /**
     * Returns a set of segments which represents unmarked spaces within the segmentation defined under the given key.
     * @param superSetKey the super set ID of the segmentation which needs to be inverted
     * @return a set of inverted segments
     */
    public Set<Segment> getNegativeSegmentation(String superSetKey){
        Set<Segment> res = new TreeSet<>();
        Set<Segment> segmentation = inner.get().getSegmentation(superSetKey);
        Iterator<Segment> it = segmentation.iterator();
        double lastStop = 0;
        while(it.hasNext()){
            Segment s = it.next();
            Segment resSegment = new Segment(lastStop, s.getTimeStart());
            if(resSegment.getDuration() > 0){
                res.add(resSegment);
            }
            lastStop = s.getTimeStop();
        }
        //Create a last segment that fills up the space between the last segment and right border.
        Segment lastSeg = new Segment(lastStop, getDuration());
        if(lastSeg.getDuration() > 0){
            res.add(lastSeg);
        }
        return res;
    }

    /**
     * Returns the bounds that a segment or selection rectangle is allowed to move in.
     * The bounds are defined on the left side as the closes border or closest's segment's timeEnd,
     * and on the right side as the closest border or closest segment's timeStart.
     * @param xPosition current xPosition from which the bounds should be calculated
     * @param id superSet id of the segmentation
     * @return a double array containing 2 values. The first value is the left border and the second value is the right border
     */
    public double[] getBounds(double xPosition, String id){
        double lowerBounds = 0;
        double upperBounds = getDuration();
        //The children list is not sorted and can also include handles of annotations

        for(Segment s : inner.get().getSegmentation(id)) {
            double nTimeStart = s.getTimeStart();
            double nTimeStop = s.getTimeStop();
            if(nTimeStop < xPosition && nTimeStop > lowerBounds) {
                lowerBounds = nTimeStop;
            }
            if(nTimeStart > xPosition && nTimeStart < upperBounds) {
                upperBounds = nTimeStart;
            }
        }
        return new double[]{lowerBounds, upperBounds};
    }

    @Override
    public void select(double timeStart, double timeEnd) {
        strokes.stream()
                .flatMap(s -> s.getObservableDots().stream())
                .forEach(d -> {
                    if(d.getTimeStamp() >= timeStart && d.getTimeStamp() <= timeEnd){
                        d.setSelected(true);
                    }
                });
    }

    @Override
    public void selectOnlyTimeFrame(double timeStart, double timeEnd) {
        strokes.stream()
                .flatMap(s -> s.getObservableDots().stream())
                .forEach(d -> {
                    if(d.getTimeStamp() >= timeStart && d.getTimeStamp() <= timeEnd){
                        d.setSelected(true);
                    }
                    else{
                        d.setSelected(false);
                    }
                });
    }

    @Override
    public void selectRect(double x, double y, double width, double height) {
        Rectangle rect = new Rectangle(x, y, width, height);
        strokes.stream()
                .flatMap(s -> s.getObservableDots().stream())
                .filter(d -> rect.contains(d.getX(), d.getY()))
                .forEach(d -> d.setSelected(true));
    }

    @Override
    public void selectOnlyRect(double x, double y, double width, double height) {
        Rectangle rect = new Rectangle(x, y, width, height);
        strokes.stream()
                .flatMap(s -> s.getObservableDots().stream())
                .forEach(d -> {
                    if (rect.contains(d.getX(), d.getY())) {
                        d.setSelected(true);
                    } else {
                        d.setSelected(false);
                    }
                });
    }

    @Override
    public void selectRectUnscaled(double x, double y, double width, double height, double scale) {
        selectRect(x/scale, y/scale, width/scale, height/scale);
    }

    @Override
    public void selectOnlyRectUnscaled(double x, double y, double width, double height, double scale) {
        selectOnlyRect(x/scale, y/scale, width/scale, height/scale);
    }

    @Override
    public void deselect(double timeStart, double timeEnd) {
        strokes.stream()
                .flatMap(s -> s.getObservableDots().stream())
                .filter(d -> d.getTimeStamp() >= timeStart && d.getTimeStamp() <= timeEnd)
                .forEach(d -> d.setSelected(false));
    }

    @Override
    public void deselectAll() {
        strokes.stream()
                .flatMap(s -> s.getObservableDots().stream())
                .forEach(d -> d.setSelected(false));
    }

    @Override
    public void deselectRect(double x, double y, double width, double height) {
        Rectangle rect = new Rectangle(x, y, width, height);
        strokes.stream()
                .flatMap(s -> s.getObservableDots().stream())
                .filter(d -> d.isSelected())
                .forEach(d -> {
                    if(!rect.contains(d.getX(), d.getY())){
                        d.setSelected(false);
                    }
                });
    }

    @Override
    public void deselectRectUnscaled(double x, double y, double width, double height, double scale) {
        deselectRect(x/scale, y/scale, width/scale, height/scale);
    }
}
