package Controls.TimelineElement;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.VBox;

public abstract class TimeLineElement extends VBox {
    protected long timeStart;
    protected long timeStop;
    protected Canvas canvas;

    public TimeLineElement(long tStart, long tEnd, double parentHeight){
        this.timeStart = tStart;
        this.timeStop = tEnd;
        canvas = new Canvas();
        canvas.setHeight(parentHeight);
        canvas.setWidth(tEnd -tStart);
    }
}
