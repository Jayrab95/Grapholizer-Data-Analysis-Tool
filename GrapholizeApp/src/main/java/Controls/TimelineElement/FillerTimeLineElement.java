package Controls.TimelineElement;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

//http://www.java2s.com/Tutorials/Java/JavaFX/0040__JavaFX_Line.htm perhaps worth looking at. Another way to display lines.
@Deprecated
public class FillerTimeLineElement extends TimeLineElement {
    public FillerTimeLineElement(double tStart, double tEnd, double parentHeight, Color c) {
        super(tStart, tEnd, parentHeight, c, "");
    }

    @Override
    protected void handleMouseClick(MouseEvent e) {

    }



}
