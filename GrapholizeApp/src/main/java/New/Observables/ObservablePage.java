package New.Observables;

import New.CustomControls.Annotation.AnnotationRectangle;
import New.Interfaces.Observer.PageObserver;
import New.Interfaces.Observer.StrokeObserver;
import New.Interfaces.Selector;
import New.Model.Entities.*;
import New.util.PageUtil;
import javafx.beans.property.DoubleProperty;
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
    private Page inner;

    private ObservableList<ObservableStroke> strokes;
    private List<PageObserver> observers;

    private ObjectProperty<Page> innerPage;
    private ObservableList<Dot> selectedDots;

    public ObservablePage(Page inner){
        this.inner = inner;
        strokes = FXCollections.observableList(generateStrokes());
        this.innerPage = new SimpleObjectProperty();
        this.innerPage.set(inner);

        observers = new LinkedList<>();
    }

    public ObservablePage(ObservablePage p){
        this(p.inner);
    }

    public ObjectProperty getPageProperty(){
        return innerPage;
    }

    public void setPage(Page newPage){
        System.out.println("setPage in ObservabelPage called");
        this.inner = newPage;
        strokes = FXCollections.observableList(generateStrokes());
        this.innerPage.set(newPage);
        notifyObservers();
    }

    public List<List<Dot>> getDotSectionsForAnnotations(String topic){
        return PageUtil.getDotSectionsForAnnotations(inner.getTimeLine(topic), inner.getStrokes());
    }

    public void registerStrokeObserver(StrokeObserver strokeObserver){
        for(ObservableStroke s : strokes){
            s.addObserver(strokeObserver);
        }
    }

    public void setPage(ObservablePage p){
        this.setPage(p.inner);
    }

    public void addAnnotation(String key, Annotation a){
        inner.getTimeLine(key).add(a);
    }

    public void removeAnnotation(String key, Annotation a){
        inner.getTimeLines().get(key).remove(a);
    }

    public boolean containsTag(String tag){
        return inner.getTimeLines().containsKey(tag);
    }

    public Optional<List<Annotation>> getAnnotationSet(String tag){
        if(containsTag(tag)){
            return Optional.of(inner.getTimeLine(tag));
        }
        return Optional.empty();
    }

    public boolean collidesWithOtherElements(String timeLineKey, double timeStart, double timeStop){
        List debug = inner.getTimeLine(timeLineKey).stream()
                .filter(annotation -> annotation.collidesWith(timeStart, timeStop))
                .collect(Collectors.toList());
        return inner.getTimeLine(timeLineKey).stream()
                .filter(annotation -> annotation.collidesWith(timeStart, timeStop))
                .count() > 0;
    }

    public boolean listCollidesWithOtherAnnotations(String key, List<AnnotationRectangle> annotations){
        List debug = annotations.stream()
                .filter(a -> collidesWithOtherElements(key, a.getTimeStart(), a.getTimeStop()))
                .collect(Collectors.toList());
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
    public List<List<Dot>> getDotSectionsForAnnotations(List<AnnotationRectangle> rects){
        List<List<Dot>> res = new LinkedList<>();

        for(AnnotationRectangle ar : rects){
            //reqStrokes contains all strokes that overlap with the bounds of this annotation.
            List<Stroke> reqStrokes = inner.getStrokes().stream()
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
    public List<List<Dot>> getDotSectionsForElements(List<AnnotationRectangle> elements){
        //Only proceed with filtering if even necessary.
        if(inner.getStrokes().size() > 0 && elements.size() > 0){

            double lowerBound = elements.get(0).getTimeStart();
            double upperBound = elements.get(elements.size()-1).getTimeStop();

            /*
             * The Lambda statement first filters for the relevant strokes.
             * Then it creates a flatmap of all dots within the strokes and filters for dots within the time range.
             */
            List<Dot> allDotsInTimeRange = inner.getStrokes().stream()
                    .filter(observableStroke -> observableStroke.getTimeStart() >= lowerBound && observableStroke.getTimeEnd() <= upperBound)
                    .map(observableStroke -> observableStroke.getDots())
                    .flatMap(dots -> dots.stream()
                            .filter(dot -> dot.getTimeStamp() >= lowerBound && dot.getTimeStamp() <= upperBound))
                    .collect(Collectors.toList());

            //If no dots were found, abort and return an empty list.
            if(allDotsInTimeRange.size() > 0){
                List<List<Dot>> dotSections = new LinkedList<>();
                for(AnnotationRectangle elem : elements){
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

    public List<List<Dot>> replaceMe(){
        System.out.println("REPLACE ME");
        return List.of();
    }


    /**
     * Creates a list of all selected dot segments.
     * @return a list of segments of dots which are currently selected.
     */
    public List<List<ObservableDot>> getSelectedDotSegments(){
        List<List<ObservableDot>> res = new LinkedList<>();
        for (ObservableStroke s : getObservableStrokes()){
            List<ObservableDot> dotSegment = s.getObservableDots().stream()
                    .filter(d -> d.isSelected())
                    .collect(Collectors.toList());
            if(!dotSegment.isEmpty()){
                res.add(dotSegment);
            }
        }
        return res;
    }

    public List<ObservableStroke> generateStrokes(){
        List<ObservableStroke> observableStrokes = new LinkedList<>();
        for(Stroke s : inner.getStrokes()){
            observableStrokes.add(new ObservableStroke(s, Color.BLACK));
        }
        return observableStrokes;
    }

    public ObservableList<ObservableStroke> getObservableStrokes(){
        return this.strokes;
    }

    public List<ObservableAnnotation>getTimeLineAnnotations(String timeLineKey){
        List<ObservableAnnotation> res = new LinkedList<>();
        for(Annotation a : inner.getTimeLine(timeLineKey)){
            res.add(new ObservableAnnotation(a));
        }
        return res;
    }
    public PageMetaData getPageMetaData(){
        return inner.getPageMetaData();
    }

    public double getDuration(){return inner.getDuration();}


    public void addObserver(PageObserver obs) {
        this.observers.add(obs);
    }


    public void removeObserver(PageObserver obs) {
        this.observers.remove(obs);
    }


    public void notifyObservers() {
        for(PageObserver obs : this.observers){
            obs.update(this);
        }
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
