package New.Model.ObservableModel;

import New.Interfaces.Observable;
import New.Interfaces.Observer;
import New.Model.Entities.SimpleColor;
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
 */
public class ObservableStroke implements Observable {

    //region Private fields
    //Idea: the strokes/Dots implement a method called draw. This way, they can be wrapped with different filters. => DEcorator DesignPattern
    private final List<Observer> observers;
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
        observers = new ArrayList<>();
        selected = new SimpleBooleanProperty(false);
    }

    /**
     * Constructor which wraps the given stroke and adds an initial observer to the observerList.
     * @param s Stroke to be wrapped in this object
     * @param o Initial Observer which should be added.
     */
    public ObservableStroke(Stroke s, Observer o){
        this(s);
        addObserver(o);
    }

    //Not sure if these are necessary yet, but they're nice to have.
    public ObservableStroke(Stroke s, Color c){
        this(s);
        this.color = new SimpleObjectProperty<>(c);
    }

    public ObservableStroke(Stroke s, Observer o, Color c){
        this(s, o);
        this.color = new SimpleObjectProperty<>(c);
    }
    //endregion

    //region Stroke Attribute getters
    //Todo: Perhaps return a clone of dots. Dots should not be modifiable. Depending on the size of the list, this could hurt the performance however.
    public List<Dot> getDots() {
        return stroke.getDots();
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
            notifyObservers();
        }
    }

    public void toggleSelected(){
        this.selected.set(!selected.get());
        notifyObservers();
    }
    //endregion

    //region Observable logic

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
        for(Observer o : observers){
            o.update(this);
        }
    }
    //endregion
}
