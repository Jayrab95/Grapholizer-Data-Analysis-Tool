package New.Model.ObservableModel;

import New.Interfaces.Observable;
import New.Interfaces.Observer;
import New.Model.Entities.Page;
import New.Model.Entities.Participant;
import New.Model.Entities.Project;
import New.Model.ObservableModel.ObservableStroke;

import java.util.LinkedList;
import java.util.List;

public class ObservableActiveState implements Observable {

    private ObservableProject activeProject;
    private ObservableParticipant activeParticipant;
    private ObservablePage activePage;
    private List<Observer> observerList;
    private List<ObservableStroke> observableStrokes;

    public ObservableActiveState(Project p) {
        this.activeProject = new ObservableProject(p);
        this.activeParticipant = new ObservableParticipant(p.getParticipant(p.getParticipantIDs().iterator().next()));
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

    public ObservableProject getActiveProject(){
        return this.activeProject;
    }

    public ObservableParticipant getActiveParticipant() {
        return activeParticipant;
    }

    public void setActiveParticipant(String participantID) {
        this.activeParticipant = activeProject.getParticipant(participantID);
        setActivePage(0);
    }

    public ObservablePage getActivePage() {
        return activePage;
    }

    public void setActivePage(int index) {
        this.activePage = activePage;
        this.observableStrokes = activePage.getObservableStrokes();
        notifyObservers();
    }

    public List<ObservableStroke> getObservableStrokes(){return this.observableStrokes;}
}
