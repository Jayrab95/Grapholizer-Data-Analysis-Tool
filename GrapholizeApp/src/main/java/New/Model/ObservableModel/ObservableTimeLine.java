package New.Model.ObservableModel;

import New.CustomControls.Annotation.AnnotationRectangle;
import New.CustomControls.TimeLine.SelectableTimeLinePane;
import New.Interfaces.Observable;
import New.Interfaces.Observer;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ObservableTimeLine implements Observable {
    private SelectableTimeLinePane selectedTimeLine;
    private List<Observer> observers;

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

    @Override
    public void addObserver(Observer obs) {
        this.observers.add(obs);
    }

    @Override
    public void removeObserver(Observer obs) {
        this.observers.remove(obs);
    }

    @Override
    public void notifyObservers() {
        for(Observer observer : observers){
            observer.update(this);
        }
    }
}
