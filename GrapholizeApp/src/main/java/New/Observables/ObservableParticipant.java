package New.Observables;

import New.Interfaces.Observer.ParticipantObserver;
import New.Model.Entities.Participant;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.LinkedList;
import java.util.List;

/**
 * The ObservableParticipant is an Observable Singleton object that holds a reference to a participant.
 * It notifies its observers if a reference change on the object property happens.
 *
 */
public class ObservableParticipant {

    private ObjectProperty<Participant> inner;

    public ObservableParticipant(Participant innerParticipant){
        this.inner = new SimpleObjectProperty<>(innerParticipant);
    }

    public void setParticipant(Participant p){
        this.inner.set(p);
    }

    public void setParticipant(ObservableParticipant p){
        this.setParticipant(p.getActiveParticipant());
    }

    public int getNumberOfPages(){return inner.get().getPages().size();}

    public ObjectProperty<Participant> getInner(){
        return inner;
    }

    public Participant getActiveParticipant(){
        return inner.get();
    }

    public ObservablePage getPage(int index) throws IndexOutOfBoundsException{
        return new ObservablePage(inner.get().getPage(index));
    }

}
