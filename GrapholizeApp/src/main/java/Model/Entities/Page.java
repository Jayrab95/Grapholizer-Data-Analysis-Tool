package Model.Entities;

import Observables.ObservableStroke;
import javafx.scene.paint.Color;
import util.ColorConverter;
import util.Import.CompressedPage;
import util.Import.CompressedStroke;

import java.util.*;
import java.util.stream.Collectors;

public class Page {
    private final PageMetaData pageMetaData;
    private final List<Stroke> strokes;

    private Map<String, List<TimeLineElement>> timeLines;

    public Page (PageMetaData pageMetaData, List<Stroke> strokes){
        this.pageMetaData = pageMetaData;
        this.strokes = strokes;

    }

    public Page (CompressedPage cp) {
        //TODO: Lukas Width und Height müssen durch die Book Nummber bestimmt werden
        long initialTimestamp = cp.Strokes.get(0).TimeStart;
        this.pageMetaData = new PageMetaData(0, cp.Number, cp.Book
                , 63.273216f, 88.582596f
                , initialTimestamp, initialTimestamp,0, cp.Strokes.size());
        strokes = new LinkedList<>();
        for (CompressedStroke cstroke : cp.Strokes) {
            strokes.add(new Stroke(cstroke, initialTimestamp));
        }
    }

    public PageMetaData getPageMetaData() {
        return pageMetaData;
    }

    public List<Stroke> getStrokes() {
        return strokes;
    }

    public Map<String, List<TimeLineElement>> getTimeLines(){return this.timeLines;}

    /**
     * Returns a list of individual dot sections that lie within each of the given Timeline elements. These dot sections are
     * required for the parameter timelines to only display the requierd values within each of the timeline element's timestamps.
     * @param elements the timeline elements for which the relevant dots should be searched.
     * @return A list of dot sections or an empty list if no suitable dots were found.
     */
    public List<List<Dot>> getDotSectionsForElements(List<TimeLineElement> elements){
        //Only proceed with filtering if even necessary.
        if(strokes.size() > 0 && elements.size() > 0){

            //Define the time range for which to filter for.
            double lowerBound = elements.get(0).getTimeStart();
            double upperBound = elements.get(elements.size()-1).getTimeStop();

            /*
             * The Lambda statement first filters for the relevant strokes.
             * Then it creates a flatmap of all dots within the strokes and filters for dots within the time range.
             */
            List<Dot> allRequiredDots = strokes.stream()
                    .filter(observableStroke -> observableStroke.getTimeStart() >= lowerBound && observableStroke.getTimeEnd() <= upperBound)
                    .map(observableStroke -> observableStroke.getDots())
                    .flatMap(dots -> dots.stream()
                            .filter(dot -> dot.getTimeStamp() >= lowerBound && dot.getTimeStamp() <= upperBound))
                    .collect(Collectors.toList());

            //If no dots were found, abort and return an empty list.
            if(allRequiredDots.size() > 0){
                List<List<Dot>> dotSections = new LinkedList<>();
                for(TimeLineElement elem : elements){
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
    public List<ObservableStroke> getObservableStrokes(){
        List<ObservableStroke> observableStrokes = new ArrayList<>();
        for(Stroke s : strokes){
            observableStrokes.add(new ObservableStroke(s, ColorConverter.convertJavaFXColorToModelColor(Color.BLACK)));
        }
        return observableStrokes;
    }
}
