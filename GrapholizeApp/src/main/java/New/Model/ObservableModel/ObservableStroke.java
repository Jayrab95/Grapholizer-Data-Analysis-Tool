package New.Model.ObservableModel;

import New.Interfaces.Observable;
import New.Interfaces.Observer;
import New.Model.Entities.SimpleColor;
import New.Model.Entities.Dot;
import New.Model.Entities.Stroke;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

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
    private SimpleColor simpleColor;
    //endregion

    //region Constructors

    /**
     * Basic constructor.
     * @param s The stroke to be wrapped in this object
     */
    private ObservableStroke(Stroke s){
        this.stroke = s;
        this.simpleColor = new SimpleColor();
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
    public ObservableStroke(Stroke s, SimpleColor c){
        this(s);
        this.simpleColor = c;
    }

    public ObservableStroke(Stroke s, Observer o, SimpleColor c){
        this(s, o);
        this.simpleColor = c;
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
    //endregion

    //region ObservableStroke specific getters and setters
    public SimpleColor getSimpleColor() {
        return simpleColor;
    }

    public void setSimpleColor(SimpleColor simpleColor) {
        this.simpleColor = simpleColor;
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
