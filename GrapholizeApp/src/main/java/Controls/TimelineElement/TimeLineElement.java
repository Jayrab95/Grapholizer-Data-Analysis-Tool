package Controls.TimelineElement;

import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import util.Selector;


public abstract class TimeLineElement extends Rectangle {

    protected double timeStart;
    protected double timeStop;
    protected Color c;

    //Parent not necessary anymore. can be removed from constructor
    public TimeLineElement(double tStart, double tEnd, double parentHeight, Color c){
        this.timeStart = tStart;
        this.timeStop = tEnd;
        this.c = c;

        setHeight(parentHeight);
        double width = tEnd - tStart;
        setWidth(width);
        setX(tStart);
        setY(0);

        setOnMouseClicked(e -> handleMouseClick(e));

        if(this.getWidth() > 0) {
            setFill(c);
        }
    }

    public TimeLineElement(Color c, Rectangle r){
        timeStart = r.getX();
        timeStop = r.getX() + r.getWidth();
        setWidth(r.getWidth());
        setHeight(r.getHeight());
        setX(timeStart);
        setY(0);
        setOnMouseClicked(e -> handleMouseClick(e));

        this.c = c;
        if(this.getWidth() > 0) {
            setFill(c);
        }
    }

    public double getTimeStart(){return timeStart;}
    public double getTimeStop(){return timeStop;}
    public Color getColor(){return c;}

    protected abstract void handleMouseClick(MouseEvent e);
}
