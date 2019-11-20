package Controls.Timeline.Canvas;

import Controls.TimelineElement.TimeLineElement;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ContextMenu;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

//Revamped version of timelines, in which the timeline extends from canvas. This
//Could make operating on it and placing elements on it easier
//Investigate: Use Group instead of Canvas?
//https://docs.oracle.com/javase/8/javafx/api/javafx/scene/Group.html
//https://stackoverflow.com/questions/40729967/drawing-shapes-on-javafx-canvas
//https://www.tutorialspoint.com/javafx/2dshapes_rectangle.htm

//Idea: Make Baseclass Generic. The List contains generic items. The concrete classes extend this class while defining a concrete TimelineElement that they want to contain.
//Example: class TimelineCanvas<T extends TimeLineElement> => CommentTimeLineCanvas extends TimeLineCanvas<CommentTimeLineElement>
public abstract class TimelineCanvas<T extends TimeLineElement> extends Canvas {



    protected String timeLineName;
    protected List<T> elements;
    protected ContextMenu contextMenu;
    protected double scale;
    //Todo: perhaps reference style from a style sheet.
    protected String style = "-fx-padding: 10; -fx-border-style: solid inside; -fx-border-width: 2; -fx-border-insets: 5; -fx-border-radius: 5; -fx-border-color: blue;";

    protected TimelineCanvas(String timeLineName, double width, double height, double scale){
        this.timeLineName = timeLineName;
        this.scale = scale;
        setHeight(height);
        setWidth(width * scale);
        InitiateTimeLine();
        InitiateContextMenu();
    }

    private void InitiateTimeLine(){
        setOnMouseClicked(e -> handleMouseClick(e));
        elements = new ArrayList<>();
        setStyle(style);
        draw();
    }

    protected abstract void handleMouseClick(MouseEvent e);

    protected abstract void InitiateContextMenu();

    //https://www.geeksforgeeks.org/javafx-canvas-class/
    protected void draw(){
        for(T tle : elements){
            // graphics context
            GraphicsContext graphics_context = getGraphicsContext2D();

            // set fill for rectangle
            graphics_context.setFill(tle.getColor());
            //Adjust coordinates
            graphics_context.fillRect(tle.getTimeStart(), 0, tle.getTimeStop() - tle.getTimeStart(), getHeight());
        }
    }

    public void add(T elem){
        elements.add(elem);
        draw();
    }
}
