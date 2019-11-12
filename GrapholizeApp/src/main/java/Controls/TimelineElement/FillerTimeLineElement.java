package Controls.TimelineElement;

import javafx.geometry.Insets;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

//http://www.java2s.com/Tutorials/Java/JavaFX/0040__JavaFX_Line.htm perhaps worth looking at. Another way to display lines.
public class FillerTimeLineElement extends TimeLineElement {
    public FillerTimeLineElement(long tStart, long tEnd, double parentHeight) {
        super(tStart, tEnd, parentHeight);
        if(this.getWidth() > 0) {
            colorCanvas();
        }
    }
    private void colorCanvas(){
        //setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));
        setFill(Color.WHITESMOKE);
        /*
        GraphicsContext gc = getGraphicsContext2D();
        gc.setFill(Color.WHITESMOKE);
        gc.fillRect(gc.getCanvas().getLayoutX(),
                gc.getCanvas().getLayoutY(),
                gc.getCanvas().getWidth(),
                gc.getCanvas().getHeight());

         */
    }
}
