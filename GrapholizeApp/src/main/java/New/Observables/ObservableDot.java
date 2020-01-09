package New.Observables;

import New.Interfaces.Observable;
import New.Interfaces.Observer.DotObserver;
import New.Interfaces.Observer.Observer;
import New.Model.Entities.Dot;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import New.util.Import.CompressedDot;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ObservableDot extends Dot implements Comparable<Dot>{

    //TODO: Scrap this class and make an observable object that has the same structure as the other observables
    private BooleanProperty selected = new SimpleBooleanProperty(false);
    private ObjectProperty<Color>  color = new SimpleObjectProperty(Color.BLACK);

    public ObservableDot(float x, float y, float force, long timeStamp) {
        super(x, y, force, timeStamp);
    }

    public ObservableDot(int tiltX, int tiltY, int twist, float force, long timeStamp, DotType dotType, float x, float y) {
        super(tiltX, tiltY, twist, force, timeStamp, dotType, x, y);
    }

    public ObservableDot(Dot d){
        this(d.getTiltX(), d.getTiltY(), d.getTwist(), d.getForce(), d.getTimeStamp(), d.getDotType(), d.getX(), d.getY());
    }

    public ObservableDot(CompressedDot cd, long timeStamp) {
        super(cd, timeStamp);
    }

    public boolean isSelected(){return selected.get();}
    public void setSelected(boolean select){selected.set(select);}

    public BooleanProperty getSelectedProperty() {
        return selected;
    }

    public ObjectProperty<Color> getColorProperty() {return color;}

    @Override
    public int compareTo(Dot o) {
        long res = this.getTimeStamp() - o.getTimeStamp();
        if(res > Integer.MAX_VALUE){
            return Integer.MAX_VALUE;
        }
        else if(res < Integer.MIN_VALUE){
            return Integer.MIN_VALUE;
        }
        else{
            return (int)res;
        }
    }
}
