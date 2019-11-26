package Controls.Timeline.Pane;

import Controls.TimelineElement.StrokeTimeLineElement;
import Controls.TimelineElement.TimeLineElement;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;

//Idea: make Timeline an interface? This way timeline operations can be called without referencing the actual control.
public abstract class TimeLinePane extends Pane {

    protected String timeLineName;
    protected double scale;
    protected ContextMenu contextMenuTimeLine;
    protected ContextMenu contextMenuTimeLineElement;
    //Todo: perhaps reference style from a style sheet.
    protected String style = "-fx-padding: 10; -fx-border-style: solid inside; -fx-border-width: 2; -fx-border-insets: 5; -fx-border-radius: 5; -fx-border-color: blue; -fx-background-color: grey";

    protected TimeLinePane(String timeLineName, double width, double height, double scale){
        this.timeLineName = timeLineName;
        this.scale = scale;
        this.contextMenuTimeLine = new ContextMenu();
        this.contextMenuTimeLineElement = new ContextMenu();

        setHeight(height);
        setPrefHeight(height);
        setWidth(width * scale);
        setPrefWidth(width * scale);
        InitiateTimeLine();
    }

    private void InitiateTimeLine(){
        setStyle(style);
        //setOnContextMenuRequested(contextMenuEvent -> contextMenuTimeLine.show(this, contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY()));
    }


    public String getTimeLineName(){
        return timeLineName;
    }

    public void setTimeLineName(String newName){
        this.timeLineName = newName;
    }

    public ContextMenu getContextMenuTimeLine(){
        return this.contextMenuTimeLine;
    }

    public ContextMenu getContextMenuTimeLineElements(){
        return this.contextMenuTimeLineElement;
    }

    public void deselectTimeLine(){
        for(Node n : getChildren()){
            ((TimeLineElement)n).setSelected(false);
        }
    }

    public void addTimeLineElement(TimeLineElement tle){
        getChildren().add(tle);
        //Assign contextmenu as ContextMenuRequest action
    }

    private ContextMenu getElementSpecificContextMenu(TimeLineElement tle){
        MenuItem menuItem_EditTLE = new MenuItem("Edit annotation");
        menuItem_EditTLE.setOnAction(event -> handleEditTimeLineElementClick(tle));


        MenuItem menuItem_DeleteTLE = new MenuItem("Delete annotation");
        menuItem_DeleteTLE.setOnAction(event -> handleDeleteTimeLineElementClick(tle));

        if(tle.getClass() == StrokeTimeLineElement.class){
            menuItem_EditTLE.setDisable(true);
            menuItem_DeleteTLE.setDisable(true);
        }
        return new ContextMenu(menuItem_EditTLE, menuItem_DeleteTLE);
    }

    private void handleEditTimeLineElementClick(TimeLineElement tle){
        //OPen dialogue. if successful => Edit.
    }

    private void handleDeleteTimeLineElementClick(TimeLineElement tle){
        //Confirmation
        //Delete
    }


}
