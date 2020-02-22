package New.Observables;

import New.CustomControls.Annotation.AnnotationRectangle;
import New.CustomControls.Annotation.SelectableAnnotationRectangle;
import New.CustomControls.TimeLine.SelectableTimeLinePane;
import New.Interfaces.Observer.TimeLineObserver;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ObservableTimeLine {
    private SelectableTimeLinePane selectedTimeLine;
    private List<TimeLineObserver> observers;

    public ObservableTimeLine(){
        observers = new LinkedList<>();
    }

    public ObservableTimeLine(SelectableTimeLinePane timeLine){
        this.selectedTimeLine = timeLine;
        observers = new LinkedList<>();
    }

    public boolean timeLineSelected(){
        return selectedTimeLine != null;
    }

    //TODO: Perhaps this mechanism needs to be reworked a bit
    // Could be optimized by making the selection dependant on the observableSegment, rather than the children.
    // The rectangle's selectedProperty binds itself to the OSegment's selected property.
    // This way, you can just search through the list of selectable segments on the page, rather than the children.
    public List<AnnotationRectangle> getSelectedAnnotations(){
        //The children first need to be filtered to see whether or not they're actually rectangles (Can also be drag rect or label)
        //Then the nodes acquire the correct cast
        //finally, they're filtered for selection
        return selectedTimeLine.getChildren().stream()
                .filter(node -> node instanceof SelectableAnnotationRectangle)
                .map(node -> (SelectableAnnotationRectangle)node)
                .filter(SelectableAnnotationRectangle::isSelected)
                .collect(Collectors.toList());
    }

    public void setSelectedTimeLine(SelectableTimeLinePane timeLine){
        if(this.selectedTimeLine != timeLine){
            this.selectedTimeLine = timeLine;
            notifyObservers();
        }
    }

    public boolean equals(SelectableTimeLinePane timeLinePane){
        return selectedTimeLine == timeLinePane;
    }

    public void addObserver(TimeLineObserver obs) {
        this.observers.add(obs);
    }

    public void removeObserver(TimeLineObserver obs) {
        this.observers.remove(obs);
    }

    public void notifyObservers() {
        for(TimeLineObserver observer : observers){
            observer.update(this);
        }
    }
}
