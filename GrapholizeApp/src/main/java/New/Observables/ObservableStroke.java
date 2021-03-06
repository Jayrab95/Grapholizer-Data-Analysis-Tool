package New.Observables;

import New.Model.Entities.Dot;
import New.Model.Entities.Stroke;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;


/**
 * ObservableStroke is a wrapper of the Stroke class that implements the Observable interface and can be assigned observers.
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
    private final Stroke innerStroke;
    private BooleanProperty selected;
    private ObjectProperty<Color> color;
    //endregion

    //region Constructors

    /**
     * Basic constructor.
     * @param s The stroke to be wrapped in this object
     */
    private ObservableStroke(Stroke s){
        this.innerStroke = s;
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
    public List<Dot> getDots() {
        return innerStroke.getDots();
    }

    private List<ObservableDot> generateObservableDots(){
        List<ObservableDot> res = new ArrayList<>(innerStroke.getDots().size());
        for(Dot d : innerStroke.getDots()){
            res.add(new ObservableDot(d));
        }
        return res;
    }

    public List<ObservableDot> getObservableDots(){
        return observableDots;
    }

    public long getTimeStart() {
        return innerStroke.getTimeStart();
    }

    public long getTimeEnd() {
        return innerStroke.getTimeEnd();
    }

    public long getDuration(){ return innerStroke.getTimeEnd() - innerStroke.getTimeStart();}
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
