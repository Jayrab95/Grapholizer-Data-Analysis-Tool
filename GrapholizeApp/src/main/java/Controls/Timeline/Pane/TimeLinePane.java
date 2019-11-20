package Controls.Timeline.Pane;

import Controls.TimelineElement.TimeLineElement;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ContextMenu;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public abstract class TimeLinePane<T extends TimeLineElement> extends Pane {

    protected String timeLineName;
    //protected List<T> elements;
    protected ContextMenu contextMenu;
    protected double scale;
    //Todo: perhaps reference style from a style sheet.
    protected String style = "-fx-padding: 10; -fx-border-style: solid inside; -fx-border-width: 2; -fx-border-insets: 5; -fx-border-radius: 5; -fx-border-color: blue; -fx-background-color: grey";

    protected TimeLinePane(String timeLineName, double width, double height, double scale){
        this.timeLineName = timeLineName;
        this.scale = scale;
        setHeight(height);
        //setMinHeight(height);
        setPrefHeight(height);
        setWidth(width * scale);
        InitiateTimeLine();
        InitiateContextMenu();
    }

    private void InitiateTimeLine(){
        //setOnMouseClicked(e -> handleMouseClick(e));
        //elements = new ArrayList<>();
        setStyle(style);
        //draw();
    }

    //protected abstract void handleMouseClick(MouseEvent e);

    protected abstract void InitiateContextMenu();

    /*
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

     */

    /*
    public void add(T elem){
        elements.add(elem);
        draw();
    }

     */
}
