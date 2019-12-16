package New.Model.ObservableModel;

import New.Interfaces.Observable;
import New.Interfaces.Observer.Observer;
import New.Interfaces.Observer.ParticipantObserver;
import New.Model.Entities.Participant;

import java.util.LinkedList;
import java.util.List;

public class ObservableParticipant {

    private Participant inner;
    private List<ParticipantObserver>observers;

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

    public void addObserver(ParticipantObserver obs) {
        observers.add(obs);
    }

    public void removeObserver(ParticipantObserver obs) {
        observers.remove(obs);
    }

    public void notifyObservers() {
        for(ParticipantObserver obs : observers){
            obs.update(this);
        }
    }
}
