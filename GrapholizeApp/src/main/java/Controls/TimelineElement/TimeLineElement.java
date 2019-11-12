package Controls.TimelineElement;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

public abstract class TimeLineElement extends Rectangle {
    protected long timeStart;
    protected long timeStop;
    //protected Canvas canvas;

    public TimeLineElement(long tStart, long tEnd, double parentHeight){
        this.timeStart = tStart;
        this.timeStop = tEnd;
        setHeight(parentHeight);
        long width = tEnd - tStart;
        setWidth(width);
    }
}
