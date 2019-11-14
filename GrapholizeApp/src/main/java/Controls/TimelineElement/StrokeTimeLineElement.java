package Controls.TimelineElement;

import Model.Entities.Stroke;
import Observables.ObservableStroke;
import javafx.scene.Parent;
import javafx.scene.paint.Color;

public class StrokeTimeLineElement extends TimeLineElement {
    ObservableStroke s;
    public StrokeTimeLineElement(double tStart, double tEnd, double parentHeight, Parent p, ObservableStroke s) {
        super(tStart, tEnd, parentHeight, p);
        this.s = s;
        setFill(new Color(s.getColor().getR(), s.getColor().getG(), s.getColor().getB(), 1));

    }

    public ObservableStroke getElementStroke(){
        return this.s;
    }

    private void timeLineClick(){
        s.setSelected(!s.isSelected());
        /*
        Highlight the stroke
        => Set selected on stroke element to true.
        => Cause a redraw of teh canvas.
        Perhaps also change the color of the element a bit.

         */

    }

}
