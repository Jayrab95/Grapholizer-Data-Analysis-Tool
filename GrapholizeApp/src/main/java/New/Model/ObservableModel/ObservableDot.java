package New.Model.ObservableModel;

import New.Interfaces.Observable;
import New.Interfaces.Observer.DotObserver;
import New.Interfaces.Observer.Observer;
import New.Model.Entities.Dot;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import New.util.Import.CompressedDot;

import java.util.ArrayList;
import java.util.List;

public class ObservableDot extends Dot {

    private BooleanProperty selected = new SimpleBooleanProperty(false);
    private List<DotObserver> observers = new ArrayList<>();

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

    public void addObserver(DotObserver obs) {
        observers.add(obs);
    }

    public void removeObserver(DotObserver obs) {
        observers.remove(obs);
    }

    public void notifyObservers() {
        for(DotObserver obs : observers){
            obs.update(this);
        }
    }
}
