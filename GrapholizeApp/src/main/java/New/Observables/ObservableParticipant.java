package New.Observables;

import New.Interfaces.Observer.ParticipantObserver;
import New.Model.Entities.Participant;

import java.util.LinkedList;
import java.util.List;

public class ObservableParticipant {

    private Participant innerParticipant;
    private List<ParticipantObserver>observers;

    public ObservableParticipant(Participant innerParticipant){
        this.innerParticipant = innerParticipant;
        this.observers = new LinkedList<>();
    }

    public void setParticipant(Participant p){
        this.innerParticipant = p;
        notifyObservers();
    }

    public void setParticipant(ObservableParticipant p){
        this.setParticipant(p.innerParticipant);
    }

    public int getNumberOfPages(){return innerParticipant.getPages().size();}

    public ObservablePage getPage(int index) throws IndexOutOfBoundsException{
        return new ObservablePage(innerParticipant.getPage(index));
    }

    public void notifyObservers() {
        for(ParticipantObserver obs : observers){
            obs.update(this);
        }
    }
}
