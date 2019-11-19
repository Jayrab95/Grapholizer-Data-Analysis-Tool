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
    protected Selector s;
    protected Color c;

    //Parent not necessary anymore. can be removed from constructor
    public TimeLineElement(double tStart, double tEnd, double parentHeight, Color c){
        this.timeStart = tStart;
        this.timeStop = tEnd;
        this.c = c;
        s = new Selector();

        setHeight(parentHeight);
        double width = tEnd - tStart;
        setWidth(width);


        setOnMouseClicked(e -> setInitialMousePosition(e));
        setOnMouseDragged(e -> doDragSelection(e));
        setOnMouseReleased(e -> handleMouseRelease(e));

        if(this.getWidth() > 0) {
            setFill(c);
        }
    }

    public double getTimeStart(){return timeStart;}
    public double getTimeStop(){return timeStop;}
    public Color getColor(){return c;}


    protected void setInitialMousePosition(MouseEvent e){
        s.setStart(e.getX(), e.getY());
    }
    protected void doDragSelection(MouseEvent e){
        s.setEnd(e.getX(), e.getY());
        //Draw Selection rectangle
    }
    protected abstract void handleMouseRelease(MouseEvent e);
}
