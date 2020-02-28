package New.Observables;

import New.Interfaces.Observer.ParticipantObserver;
import New.Model.Entities.Participant;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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

    public int getNumberOfPages(){return inner.getPages().size();}

    public ObservablePage getPage(int index) throws IndexOutOfBoundsException{
        return new ObservablePage(inner.getPage(index));
    }

    public void notifyObservers() {
        for(ParticipantObserver obs : observers){
            obs.update(this);
        }
    }
}
