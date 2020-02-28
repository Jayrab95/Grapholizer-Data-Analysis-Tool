package New.Observables;

import New.CustomControls.Annotation.SegmentRectangle;
import New.Interfaces.Selector;
import New.Model.Entities.*;
import New.util.PageUtil;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ObservablePage implements Selector {
    //private Page inner;

    private ObservableList<ObservableStroke> strokes;

    private final ObjectProperty<Page> inner;
    private ObservableList<Dot> selectedDots;

    public ObservablePage(Page inner){
        strokes = FXCollections.observableList(generateStrokes(inner));
        this.inner = new SimpleObjectProperty();
        this.inner.set(inner);

    }

    public ObservablePage(ObservablePage p){
        this(p.inner.get());
    }



    public ObjectProperty<Page> getPageProperty(){
        return inner;
    }

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
        inner.get().getSegmentation(topicSetKey).add(a);
    }

    public void removeAnnotation(String topicSetKey, Segment a){
        inner.get().getSegmentation(topicSetKey).remove(a);
    }

    public boolean containsTag(String tag){
        return inner.get().getTimeLines().containsKey(tag);
    }

    public Optional<List<Segment>> getAnnotationSet(String topicSetID){
        if(containsTag(topicSetID)){
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

    public boolean listCollidesWithOtherAnnotations(String key, List<SegmentRectangle> annotations){
        return annotations.stream()
                .filter(a -> collidesWithOtherElements(key, a.getTimeStart(), a.getTimeStop()))
                .count() > 0;
    }

    /**
     * Returns a list of dot lists that represent the strokes which overlap with the given list of annotations.
     * These dots are required for the detail timelines, in order to calculate the different characteristics for each timneline.
     * @param rects the annotation rectangles of the timeline.
     * @return a list of dot lists (dot sections)
     */
    public List<List<Dot>> getAllDotSectionsForTopicSet(List<SegmentRectangle> rects){
        List<List<Dot>> res = new LinkedList<>();

        for(SegmentRectangle ar : rects){
            //reqStrokes contains all strokes that overlap with the bounds of this annotation.
            List<Stroke> reqStrokes = inner.get().getStrokes().stream()
                    .filter(observableStroke -> observableStroke.getTimeEnd() >= ar.getTimeStart() && observableStroke.getTimeStart() <= ar.getTimeStop())
                    .collect(Collectors.toList());
            for(Stroke s : reqStrokes){
                res.add(s.getDotsWithinTimeRange(ar.getTimeStart(), ar.getTimeStop()));
            }
        }
        return res;
    }

    /**
     * Returns a list of individual dot sections that lie within each of the given Timeline elements. These dot sections are
     * required for the parameter timelines to only display the requierd values within each of the timeline element's timestamps.
     * @param elements the timeline elements for which the relevant dots should be searched.
     * @return A list of dot sections or an empty list if no suitable dots were found.
     */
    @Deprecated
    public List<List<Dot>> getDotSectionsForElements(List<SegmentRectangle> elements){
        //Only proceed with filtering if even necessary.
        if(inner.get().getStrokes().size() > 0 && elements.size() > 0){

            double lowerBound = elements.get(0).getTimeStart();
            double upperBound = elements.get(elements.size()-1).getTimeStop();

            /*
             * The Lambda statement first filters for the relevant strokes.
             * Then it creates a flatmap of all dots within the strokes and filters for dots within the time range.
             */
            List<Dot> allDotsInTimeRange = inner.get().getStrokes().stream()
                    .filter(observableStroke -> observableStroke.getTimeStart() >= lowerBound && observableStroke.getTimeEnd() <= upperBound)
                    .map(observableStroke -> observableStroke.getDots())
                    .flatMap(dots -> dots.stream()
                            .filter(dot -> dot.getTimeStamp() >= lowerBound && dot.getTimeStamp() <= upperBound))
                    .collect(Collectors.toList());

            //If no dots were found, abort and return an empty list.
            if(allDotsInTimeRange.size() > 0){
                List<List<Dot>> dotSections = new LinkedList<>();
                for(SegmentRectangle elem : elements){
                    /* This lambda statement filters for all dots within the total dot list that lie within
                     * the iterated element's time range and puts them into a new list (dot section for this element)
                     */
                    List<Dot> dotsForElement = allDotsInTimeRange.stream()
                            .filter(dot -> dot.getTimeStamp() >= elem.getTimeStart() && dot.getTimeStamp() <= elem.getTimeStop())
                            .collect(Collectors.toList());
                    //Add the filtered dot list to the result. (if there are any)
                    if(dotsForElement.size() > 0){
                        dotSections.add(dotsForElement);
                    }
                }
                return dotSections;
            }
        }
        //The page contains no strokes whatsoever
        return Collections.emptyList();
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

    public ObservableList<ObservableStroke> getObservableStrokes(){
        return this.strokes;
    }

    public PageMetaData getPageMetaData(){
        return inner.get().getPageMetaData();
    }

    public double getDuration(){return inner.get().getDuration();}

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
    public void selectRectUnscaled(double x, double y, double width, double height, double scale) {
        selectRect(x/scale, y/scale, width/scale, height/scale);
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
