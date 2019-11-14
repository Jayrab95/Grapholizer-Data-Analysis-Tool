package Observables;

import Interfaces.Observable;
import Interfaces.Observer;
import Model.Entities.Color;
import Model.Entities.Dot;
import Model.Entities.Stroke;

import java.util.ArrayList;
import java.util.List;

//TODO: What makes more sense here? Have ObservableStroke be a Wrapper or an extension of the StrokeClass.
//Argument for extension: ObservableStrokes can be passed over as strokes, if only the base functionality of the stroke is required.
//Since all the fields are final anyway, the extension doesn't "hurt" the structure, as data cannot be changed anyway.
public class ObservableStroke extends Stroke implements Observable {

    //region Private fields
    //Idea: the strokes/Dots implement a method called draw. This way, they can be wrapped with different filters. => DEcorator DesignPattern
    private final List<Observer> observers;
    private final Stroke stroke;
    private boolean selected = false;
    private Color color;
    //endregion

    //region Constructors
    private ObservableStroke(Stroke s){
        super(s);
        this.stroke = s;
        this.color = new Color();
        observers = new ArrayList<>();
    }

    public ObservableStroke(Stroke s, Observer o){
        this(s);
        addListener(o);
    }

    //Not sure if these are necessary yet, but they're nice to have.
    public ObservableStroke(Stroke s, Color c){
        this(s);
        this.color = c;
    }

    public ObservableStroke(Stroke s, Observer o, Color c){
        this(s, o);
        this.color = c;
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
    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isSelected(){
        return this.selected;
    }

    public void setSelected(boolean select){
        if(select != this.selected){
            this.selected = select;
            notifyListeners();
        }
    }

    public void toggleSelected(){
        this.selected = !this.selected;
        notifyListeners();
    }
    //endregion

    //region Observable logic

    @Override
    public void addListener(Observer obs) {
        observers.add(obs);
    }

    @Override
    public void removeListener(Observer obs) {
        observers.remove(obs);
    }


    @Override
    public void notifyListeners() {
        for(Observer o : observers){
            o.update(this);
        }
    }
    //endregion
}
