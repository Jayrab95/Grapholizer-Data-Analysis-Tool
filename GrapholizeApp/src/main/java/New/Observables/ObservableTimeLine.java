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

    public List<AnnotationRectangle> getSelectedElements(){
        return selectedTimeLine.getChildren().stream()
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
