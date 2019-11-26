package Controls.TimelineElement;

import Interfaces.Observable;
import Interfaces.Observer;
import Model.Entities.Stroke;
import Observables.ObservableStroke;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class StrokeTimeLineElement extends TimeLineElement implements Observer {
    ObservableStroke s;
    public StrokeTimeLineElement(double tStart, double tEnd, double parentHeight, Color c, ObservableStroke s) {
        super(tStart, tEnd, parentHeight, c, "Stroke");
        this.s = s;
        s.getSelectedBooleanProperty().bindBidirectional(selected);
    }



    public ObservableStroke getElementStroke(){
        return this.s;
    }




    @Override
    public void update(Observable o){
        //this.selected = ((ObservableStroke) o).isSelected();
        //Change Element highlighting
    }
}
