package Controls.Timeline.Pane;

import Controls.TimelineElement.TimeLineElement;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ContextMenu;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public abstract class TimeLinePane extends Pane {

    protected String timeLineName;
    protected ContextMenu contextMenu;
    protected double scale;
    //Todo: perhaps reference style from a style sheet.
    protected String style = "-fx-padding: 10; -fx-border-style: solid inside; -fx-border-width: 2; -fx-border-insets: 5; -fx-border-radius: 5; -fx-border-color: blue; -fx-background-color: grey";

    protected TimeLinePane(String timeLineName, double width, double height, double scale){
        this.timeLineName = timeLineName;
        this.scale = scale;
        setHeight(height);
        setPrefHeight(height);
        setWidth(width * scale);
        InitiateTimeLine();
        InitiateContextMenu();
    }

    private void InitiateTimeLine(){
        setStyle(style);
    }


    protected abstract void InitiateContextMenu();

    public String getTimeLineName(){
        return timeLineName;
    }

}
