package Controls.TimelineElement;

import Model.Stroke;
import javafx.geometry.Insets;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
//http://www.java2s.com/Tutorials/Java/JavaFX/0040__JavaFX_Line.htm perhaps worth looking at. Another way to display lines.
public class StrokeTimeLineElement extends TimeLineElement {
    Stroke s;
    public StrokeTimeLineElement(long tStart, long tEnd, double parentHeight, Stroke s) {
        super(tStart, tEnd, parentHeight);
        this.s = s;
        colorCanvas();
    }
    //This could be moved to base class. Pass color object as parameter.
    private void colorCanvas(){

        setFill(new Color(s.getColor().getR(), s.getColor().getG(), s.getColor().getB(), 1));
        /*
        GraphicsContext gc = getGraphicsContext2D();
        gc.setFill(new Color(s.getColor().getR(), s.getColor().getG(), s.getColor().getB(), 1));
        gc.fillRect(gc.getCanvas().getLayoutX(),
                gc.getCanvas().getLayoutY(),
                gc.getCanvas().getWidth(),
                gc.getCanvas().getHeight());
         */
    }
}
