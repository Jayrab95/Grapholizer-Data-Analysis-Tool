package Model;

import Interfaces.Observable;
import Interfaces.Observer;
import Model.Entities.Page;
import Model.Entities.Participant;
import Observables.ObservableStroke;

import java.util.LinkedList;
import java.util.List;

public class ObservableActiveState implements Observable {

    private Participant activeParticipant;
    private Page activePage;
    private List<Observer> observerList;
    private List<ObservableStroke> observableStrokes;

    public ObservableActiveState(Participant p) {
        this.activeParticipant = p;
        this.activePage = activeParticipant.getPage(0);
        this.observableStrokes = activePage.getObservableStrokes();
        this.observerList = new LinkedList<>();
    }

    @Override
    public void addObserver(Observer obs) {
        observerList.add(obs);
    }

    @Override
    public void removeObserver(Observer obs) {
        observerList.remove(obs);
    }

    @Override
    public void notifyObservers() {
        for(Observer obs : observerList){
            obs.update(this);
        }
    }

    public Participant getActiveParticipant() {
        return activeParticipant;
    }

    public void setActiveParticipant(Participant activeParticipant) {
        this.activeParticipant = activeParticipant;
        setActivePage(0);
    }

    public Page getActivePage() {
        return activePage;
    }

    public void setActivePage(int index) {
        this.activePage = activePage;
        this.observableStrokes = activePage.getObservableStrokes();
        notifyObservers();
    }

    public List<ObservableStroke> getObservableStrokes(){return this.observableStrokes;}
}
