package Controls.TimelineElement;

import Model.Entities.Stroke;
import Observables.ObservableStroke;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class StrokeTimeLineElement extends TimeLineElement {
    ObservableStroke s;
    public StrokeTimeLineElement(double tStart, double tEnd, double parentHeight, Color c, ObservableStroke s) {
        super(tStart, tEnd, parentHeight, c);
        this.s = s;
        setFill(c);

    }

    public ObservableStroke getElementStroke(){
        return this.s;
    }

    @Override
    protected void handleMouseRelease(MouseEvent event){
        s.setSelected(!s.isSelected());
        System.out.println("handleMouseRelease from StrokeTimeLineElement");
        /*
        Highlight the stroke
        => Set selected on stroke element to true.
        => Cause a redraw of teh canvas.
        Perhaps also change the color of the element a bit.
         */
    }

}
