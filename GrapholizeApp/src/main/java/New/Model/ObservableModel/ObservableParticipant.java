package New.Model.ObservableModel;

import New.Interfaces.Observable;
import New.Interfaces.Observer;
import New.Model.Entities.Participant;

import java.util.LinkedList;
import java.util.List;

public class ObservableParticipant implements Observable {

    private Participant inner;
    private ObservablePage activePage;
    private List<Observer>observers;

    public ObservableParticipant(Participant inner){
        this.inner = inner;
        activePage = new ObservablePage(inner.getPage(0));
        this.observers = new LinkedList<>();
    }

    public void setParticipant(Participant p){
        this.inner = p;
        activePage.setPage(p.getPage(0));
        notifyObservers();
    }

    public ObservablePage getActivePage(){
        return this.activePage;
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
