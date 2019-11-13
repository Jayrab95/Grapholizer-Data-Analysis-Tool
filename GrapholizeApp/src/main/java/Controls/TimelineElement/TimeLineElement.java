package Controls.TimelineElement;

import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

public abstract class TimeLineElement extends Rectangle {

    protected long timeStart;
    protected long timeStop;
    protected Parent parent;

    //Parent not necessary anymore. can be removed from constructor
    public TimeLineElement(long tStart, long tEnd, double parentHeight, Parent p){
        this.timeStart = tStart;
        this.timeStop = tEnd;
        this.parent = p;

        setHeight(parentHeight);
        long width = tEnd - tStart;
        setWidth(width);
    }
}
