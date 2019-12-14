package New.Model.ObservableModel;

import New.CustomControls.Annotation.AnnotationRectangle;
import New.CustomControls.TimeLine.SelectableTimeLinePane;
import New.Interfaces.Observable;
import New.Interfaces.Observer.Observer;
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

    public List<AnnotationRectangle> getSelectedElements(){
        return selectedTimeLine.getChildren().stream()
                .map(node -> (AnnotationRectangle)node)
                .filter(AnnotationRectangle::isSelected)
                .collect(Collectors.toList());
    }

    public void setSelectedTimeLine(SelectableTimeLinePane timeLine){
        this.selectedTimeLine = timeLine;
        notifyObservers();
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
