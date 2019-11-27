package Controls.TimelineElement;

import Interfaces.Observable;
import Interfaces.Observer;
import Observables.ObservableStroke;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import util.Selector;


public class TimeLineElement extends Rectangle {

    protected double timeStart;
    protected double timeStop;
    protected Color c;
    protected BooleanProperty selected;
    protected String annotationText;
    //Reason for having comment in baseclass: When copying annotations, there first needs to be a type check to see if
    //There's also a comment

    public TimeLineElement(double tStart, double tEnd, double parentHeight, Color c, String annotationText){
        this.timeStart = tStart;
        this.timeStop = tEnd;
        this.c = c;
        this.selected = new SimpleBooleanProperty(false);
        this.annotationText = annotationText;


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

    public TimeLineElement(Color c, Rectangle r, String annotationText){
        this(r.getX(), r.getX() + r.getWidth(), r.getHeight(), c, annotationText);
    }


    public double getTimeStart(){return timeStart;}
    public double getTimeStop(){return timeStop;}
    public BooleanProperty getSelectedBooleanProperty(){return this.selected;}
    public Color getColor(){return c;}
    public String getAnnotationText(){
        return annotationText;
    }

    public boolean isSelected(){
        return selected.get();
    }
    public void setSelected(boolean selected){
        this.selected.set(selected);
    }
    public void toggleSelected(){
        this.selected.set(!selected.get());
    }
    public void setAnnotationText(String text){
        this.annotationText = text;
    }

    protected void handleMouseClick(MouseEvent e){
        System.out.println("HandleMouseClick called in TimeLineElement baseclass.");
        toggleSelected();
    }
}
