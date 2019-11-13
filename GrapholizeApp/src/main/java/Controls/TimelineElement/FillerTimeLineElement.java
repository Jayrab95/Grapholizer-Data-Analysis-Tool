package Controls.TimelineElement;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

//http://www.java2s.com/Tutorials/Java/JavaFX/0040__JavaFX_Line.htm perhaps worth looking at. Another way to display lines.
public class FillerTimeLineElement extends TimeLineElement {
    public FillerTimeLineElement(long tStart, long tEnd, double parentHeight, Parent p) {
        super(tStart, tEnd, parentHeight, p);
        if(this.getWidth() > 0) {
            setFill(Color.WHITESMOKE);
        }
    }
}
