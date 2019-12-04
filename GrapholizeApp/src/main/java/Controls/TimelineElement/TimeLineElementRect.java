package Controls.TimelineElement;

import Controllers.TimeLineElementController;
import Model.Entities.TimeLineElement;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class TimeLineElementRect extends Rectangle {


    protected Color color;
    protected BooleanProperty selected;
    protected TimeLineElement timeLineElement;
    protected DoubleProperty scale;

    protected TimeLineElementController timeLineElementController;
    //Reason for having comment in baseclass: When copying annotations, there first needs to be a type check to see if
    //There's also a comment

    public TimeLineElementRect(double tStart, double tEnd, double parentHeight, Color c, String annotationText, DoubleProperty scale){
        this.color = c;
        this.selected = new SimpleBooleanProperty(false);

        this.scale = new SimpleDoubleProperty(scale.get());
        this.scale.bind(scale);

        this.timeLineElementController = new TimeLineElementController(new TimeLineElement(annotationText, tStart, tEnd));


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

    public TimeLineElementRect(Color c, Rectangle r, String annotationText, DoubleProperty scale){
        this(r.getX(), r.getX() + r.getWidth(), r.getHeight(), c, annotationText, scale);
    }

    protected void setUpElement(){
        setX(timeLineElement.getTimeStart() * scale.get());
        setWidth((timeLineElement.getTimeStop() - timeLineElement.getTimeStart()) * scale.get());
    }

    protected void onValueChange(){
        setUpElement();
    }

    //TODO: Consider moving these fields solely to the Model timelineElement
    public double getTimeStart(){return timeLineElement.getTimeStart();}

    public void setTimeStart(double start){
        timeLineElement.setTimeStart(start);
    }

    public double getTimeStop(){return timeLineElement.getTimeStop();}

    public void setTimeStop(double stop){
        timeLineElement.setTimeStop(stop);
    }

    public Color getColor(){return color;}
    public void setColor(Color c){this.color = c;}

    public String getAnnotationText(){
        return timeLineElement.getAnnotationText();
    }
    public void setAnnotationText(String text){
        this.timeLineElement.setAnnotationText(text);
    }

    public TimeLineElement getTimeLineElement(){return this.timeLineElement;}

    public BooleanProperty getSelectedBooleanProperty(){return this.selected;}
    public boolean isSelected(){
        return selected.get();
    }
    public void setSelected(boolean selected){
        this.selected.set(selected);
    }
    public void toggleSelected(){
        this.selected.set(!selected.get());
    }

    public boolean collidesWith(TimeLineElementRect other){
        return timeLineElementController.getTimeLineElement().collidesWith(other.getTimeLineElement());
    }

    public boolean timeStampWithinTimeRange(double timeStamp){
        return timeLineElement.getTimeStart() <= timeStamp && timeLineElement.getTimeStop() >= timeStamp;
    }

    public void move(double newTimeStart){
        setTimeStart(newTimeStart);
        setTimeStop(newTimeStart + getWidth());
        setX(newTimeStart);
    }

    protected void handleMouseClick(MouseEvent e){
        toggleSelected();
    }
}
