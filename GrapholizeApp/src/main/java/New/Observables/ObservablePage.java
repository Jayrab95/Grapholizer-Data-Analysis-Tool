package New.Observables;

import New.CustomControls.Annotation.SegmentRectangle;
import New.Interfaces.Selector;
import New.Model.Entities.*;
import New.util.PageUtil;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The ObservablePage class is a Observable Singleton object. The goal is to only have one active ObservablePage during the runtime.
 * The ObservablePage class holds
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



    public ObjectProperty<Page> getPageProperty(){
        return inner;
    }

    //TODO: Potential memory leak?
    // Are strokes/dots still in memory somehow after page switch??
    public void setPage(Page newPage){
        System.out.println("setPage in ObservabelPage called");
        strokes = FXCollections.observableList(generateStrokes(newPage));
        this.inner.set(newPage);
    }

    public List<List<Dot>> getAllDotSectionsForTopicSet(String topicSetID){
        //TODO: DEfine a static string for this timeline.
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

    public boolean containsSetID(String setID){
        return inner.get().getSegmentationsMap().containsKey(setID);
    }

    public Optional<Set<Segment>> getAnnotationSet(String topicSetID){
        if(containsSetID(topicSetID)){
            return Optional.of(inner.get().getSegmentation(topicSetID));
        }
        return Optional.empty();
    }

    public boolean collidesWithOtherElements(String timeLineKey, double timeStart, double timeStop){
        List debug = inner.get().getSegmentation(timeLineKey).stream()
                .filter(annotation -> annotation.collidesWith(timeStart, timeStop))
                .collect(Collectors.toList());
        return inner.get().getSegmentation(timeLineKey).stream()
                .filter(annotation -> annotation.collidesWith(timeStart, timeStop))
                .count() > 0;
    }

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
