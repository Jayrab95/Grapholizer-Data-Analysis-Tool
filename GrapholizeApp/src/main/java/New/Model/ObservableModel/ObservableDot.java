package New.Model.ObservableModel;

import New.Interfaces.Observable;
import New.Interfaces.Observer;
import New.Model.Entities.Dot;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import New.util.Import.CompressedDot;

import java.util.ArrayList;
import java.util.List;

public class ObservableDot extends Dot implements Observable {

    private BooleanProperty selected = new SimpleBooleanProperty(false);
    private List<Observer> observers = new ArrayList<>();

    public ObservableDot(float x, float y, float force, long timeStamp) {
        super(x, y, force, timeStamp);
    }

    public ObservableDot(int tiltX, int tiltY, int twist, int force, long timeStamp, DotType dotType, float x, float y) {
        super(tiltX, tiltY, twist, force, timeStamp, dotType, x, y);
    }

    public ObservableDot(CompressedDot cd, long timeStamp) {
        super(cd, timeStamp);
    }

    public boolean isSelected(){return selected.get();}
    public void setSelected(boolean select){selected.set(select);}

    public BooleanProperty getSelectedProperty() {
        return selected;
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
