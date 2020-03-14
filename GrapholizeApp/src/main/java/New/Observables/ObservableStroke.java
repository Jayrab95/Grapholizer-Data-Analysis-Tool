package New.Observables;

import New.Interfaces.Observable;
import New.Interfaces.Observer.Observer;
import New.Interfaces.Observer.StrokeObserver;
import New.Model.Entities.Dot;
import New.Model.Entities.Stroke;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

//TODO: What makes more sense here? Have ObservableStroke be a Wrapper or an extension of the StrokeClass.
//Argument for extension: ObservableStrokes can be passed over as strokes, if only the base functionality of the stroke is required.
//Since all the fields are final anyway, the extension doesn't "hurt" the structure, as data cannot be changed anyway.

/**
 * ObservableStroke is a wrapper fo the Stroke class that implements the Observable interface and can be assigned observers.
 * Additionally, the ObservableStroke is used for drawing and therefore manages the stroke color.
 * The Observers become notified if the selected property is changed or a filter is applied.
 *
 * Note: Currently, the class has become somewhat obsolete, as the original intention (being able to decorate and select
 * strokes) has been nullified. Selection now only happens on dot level.
 * The main purpose of the obserbale stroke now is to hold the observable dots
 */
public class ObservableStroke{

    //region Private fields
    private List<ObservableDot> observableDots;
    private final Stroke stroke;
    private BooleanProperty selected;
    private ObjectProperty<Color> color;
    //endregion

    //region Constructors

    /**
     * Basic constructor.
     * @param s The stroke to be wrapped in this object
     */
    private ObservableStroke(Stroke s){
        this.stroke = s;
        this.color = new SimpleObjectProperty<>(Color.BLACK);
        this.selected = new SimpleBooleanProperty(false);
        this.observableDots = generateObservableDots();
    }

    //Not sure if these are necessary yet, but they're nice to have.
    public ObservableStroke(Stroke s, Color c){
        this(s);
        this.color = new SimpleObjectProperty<>(c);
    }

    //endregion

    //region Stroke Attribute getters
    //Todo: Perhaps return a clone of dots. Dots should not be modifiable. Depending on the size of the list, this could hurt the performance however.
    public List<Dot> getDots() {
        return stroke.getDots();
    }

    private List<ObservableDot> generateObservableDots(){
        List<ObservableDot> res = new ArrayList<>(stroke.getDots().size());
        for(Dot d : stroke.getDots()){
            res.add(new ObservableDot(d));
        }
        return res;
    }

    public List<ObservableDot> getObservableDots(){
        return observableDots;
    }

    public long getTimeStart() {
        return stroke.getTimeStart();
    }

    public long getTimeEnd() {
        return stroke.getTimeEnd();
    }

    public long getDuration(){ return stroke.getTimeEnd() - stroke.getTimeStart();}
    //endregion

    //region ObservableStroke specific getters and setters
    public Color getColor() {
        return color.get();
    }

    public ObjectProperty<Color> getColorProperty(){
        return this.color;
    }

    public void setSimpleColor(Color newColor) {
        this.color.set(newColor);
    }

    public BooleanProperty getSelectedBooleanProperty(){
        return selected;
    }

    public boolean isSelected(){
        return selected.get();
    }

    public void setSelected(boolean select){
        if(select != selected.get()){
            this.selected.set(select);
        }
    }

    public void toggleSelected(){
        this.selected.set(!selected.get());
    }
    //endregion

}
