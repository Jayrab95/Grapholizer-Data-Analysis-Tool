package New.Observables;

import New.CustomControls.Annotation.AnnotationRectangle;
import New.Interfaces.Observer.PageObserver;
import New.Interfaces.Observer.StrokeObserver;
import New.Model.Entities.*;
import javafx.beans.property.DoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ObservablePage{
    private Page inner;
    private ObservableList<ObservableStroke> strokes;
    private List<PageObserver> observers;

    public ObservablePage(Page inner){
        this.inner = inner;
        strokes = FXCollections.observableList(generateStrokes());
        observers = new LinkedList<>();
    }

    public void setPage(Page newPage){
        this.inner = newPage;
        strokes = FXCollections.observableList(generateStrokes());
        notifyObservers();
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

    public boolean collidesWithOtherElements(String timeLineKey, double timestart, double timeStop){
        return inner.getTimeLine(timeLineKey).stream()
                .filter(annotation -> annotation.collidesWith(timestart, timeStop))
                .count() > 0;
    }

    public boolean listCollidesWithOtherElements(String key, List<AnnotationRectangle> annotations){
        return annotations.stream()
                .filter(a -> collidesWithOtherElements(key, a.getTimeStart(), a.getTimeStop()))
                .count() > 0;
    }

    /**
     * Returns a list of individual dot sections that lie within each of the given Timeline elements. These dot sections are
     * required for the parameter timelines to only display the requierd values within each of the timeline element's timestamps.
     * @param elements the timeline elements for which the relevant dots should be searched.
     * @return A list of dot sections or an empty list if no suitable dots were found.
     */
    public List<List<Dot>> getDotSectionsForElements(List<Annotation> elements){
        //Only proceed with filtering if even necessary.
        if(inner.getStrokes().size() > 0 && elements.size() > 0){

            //Define the time range for which to filter for.
            double lowerBound = elements.get(0).getTimeStart();
            double upperBound = elements.get(elements.size()-1).getTimeStop();

            /*
             * The Lambda statement first filters for the relevant strokes.
             * Then it creates a flatmap of all dots within the strokes and filters for dots within the time range.
             */
            List<Dot> allRequiredDots = inner.getStrokes().stream()
                    .filter(observableStroke -> observableStroke.getTimeStart() >= lowerBound && observableStroke.getTimeEnd() <= upperBound)
                    .map(observableStroke -> observableStroke.getDots())
                    .flatMap(dots -> dots.stream()
                            .filter(dot -> dot.getTimeStamp() >= lowerBound && dot.getTimeStamp() <= upperBound))
                    .collect(Collectors.toList());

            //If no dots were found, abort and return an empty list.
            if(allRequiredDots.size() > 0){
                List<List<Dot>> dotSections = new LinkedList<>();
                for(Annotation elem : elements){
                    /* This lambda statement filters for all dots within the total dot list that lie within
                     * the iterated element's time range and puts them into a new list (dot section for this element)
                     */
                    List<Dot> dotsForElement = allRequiredDots.stream()
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
        return Collections.emptyList();
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
}
