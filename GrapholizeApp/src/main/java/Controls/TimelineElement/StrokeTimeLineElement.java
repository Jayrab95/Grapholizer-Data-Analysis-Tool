package Controls.TimelineElement;

import Model.Stroke;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class StrokeTimeLineElement extends TimeLineElement {
    Stroke s;
    public StrokeTimeLineElement(long tStart, long tEnd, double parentHeight, Parent p, Stroke s) {
        super(tStart, tEnd, parentHeight, p);
        this.s = s;
        setFill(new Color(s.getColor().getR(), s.getColor().getG(), s.getColor().getB(), 1));

    }

    public Stroke getElementStroke(){
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
