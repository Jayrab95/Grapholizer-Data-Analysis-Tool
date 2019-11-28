package Controls.TimelineElement;

import Interfaces.Observable;
import Interfaces.Observer;
import Observables.ObservableStroke;
import javafx.scene.paint.Color;

public class StrokeTimeLineElementRect extends TimeLineElementRect implements Observer {
    ObservableStroke s;
    public StrokeTimeLineElementRect(double tStart, double tEnd, double parentHeight, Color c, ObservableStroke s) {
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