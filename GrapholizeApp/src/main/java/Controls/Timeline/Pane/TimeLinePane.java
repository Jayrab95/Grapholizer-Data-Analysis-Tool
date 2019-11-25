package Controls.Timeline.Pane;

import Controls.TimelineElement.TimeLineElement;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import javax.naming.Context;
import java.util.ArrayList;
import java.util.List;

//Idea: make Timeline an interface? This way timeline operations can be called without referencing the actual control.
public abstract class TimeLinePane extends Pane {

    protected String timeLineName;
    protected double scale;
    protected ContextMenu contextMenu;
    //Todo: perhaps reference style from a style sheet.
    protected String style = "-fx-padding: 10; -fx-border-style: solid inside; -fx-border-width: 2; -fx-border-insets: 5; -fx-border-radius: 5; -fx-border-color: blue; -fx-background-color: grey";

    protected TimeLinePane(String timeLineName, double width, double height, double scale){
        this.timeLineName = timeLineName;
        this.scale = scale;
        this.contextMenu = new ContextMenu();

        setHeight(height);
        setPrefHeight(height);
        setWidth(width * scale);
        setPrefWidth(width * scale);
        InitiateTimeLine();
    }

    private void InitiateTimeLine(){
        setStyle(style);
        setOnContextMenuRequested(contextMenuEvent -> contextMenu.show(this, contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY()));
    }


    public String getTimeLineName(){
        return timeLineName;
    }

    public ContextMenu getContextMenu(){
        return this.contextMenu;
    }


}
