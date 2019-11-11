package Controls.TimelineElement;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

//http://www.java2s.com/Tutorials/Java/JavaFX/0040__JavaFX_Line.htm perhaps worth looking at. Another way to display lines.
public class FillerTimeLineElement extends TimeLineElement {
    public FillerTimeLineElement(long tStart, long tEnd, double parentHeight) {
        super(tStart, tEnd, parentHeight);
        colorCanvas();
    }
    private void colorCanvas(){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITESMOKE);
        gc.fillRect(gc.getCanvas().getLayoutX(),
                gc.getCanvas().getLayoutY(),
                gc.getCanvas().getWidth(),
                gc.getCanvas().getHeight());
    }
}
