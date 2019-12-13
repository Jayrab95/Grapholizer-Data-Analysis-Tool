package New.Model.ObservableModel;

import New.CustomControls.TimeLine.SelectableTimeLinePane;
import New.Interfaces.Observable;
import New.Interfaces.Observer;

import java.util.List;

public class ObservableTimeLine implements Observable {
    private SelectableTimeLinePane selectableTimeLine;
    private List<Observer> observers;
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
