package New.Model.ObservableModel;

import New.Interfaces.Observable;
import New.Interfaces.Observer.Observer;
import New.Model.Entities.Participant;

import java.util.LinkedList;
import java.util.List;

public class ObservableParticipant implements Observable {

    private Participant inner;
    private List<Observer>observers;

    public ObservableParticipant(Participant inner){
        this.inner = inner;
        this.observers = new LinkedList<>();
    }

    public void setParticipant(Participant p){
        this.inner = p;
        notifyObservers();
    }

    public void setParticipant(ObservableParticipant p){
        this.setParticipant(p.inner);
    }


    public ObservablePage getPage(int index) throws IndexOutOfBoundsException{
        return new ObservablePage(inner.getPage(index));
    }

    @Override
    public void addObserver(Observer obs) {
        observers.add(obs);
    }

    @Override
    public void removeObserver(Observer obs) {
        observers.remove(obs);
    }

    @Override
    public void notifyObservers() {
        for(Observer obs : observers){
            obs.update(this);
        }
    }
}
