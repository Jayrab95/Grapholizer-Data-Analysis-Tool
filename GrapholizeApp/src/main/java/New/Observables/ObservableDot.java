package New.Observables;

import New.Model.Entities.Dot;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import New.util.Import.model.CompressedDot;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

/**
 * The ObservableDot is an observable model object.
 * The ObservableDot has a BooleanProperty, containing the current selection state, and
 * An ObjectProperty containining the color of the dot.
 */
public class ObservableDot extends Dot implements Comparable<Dot>{

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

    public void setColor(Color c){
        color.set(c);
    }

    @Override
    public int compareTo(Dot o) {
        return Long.compare(this.getTimeStamp(), o.getTimeStamp());
    }
}
