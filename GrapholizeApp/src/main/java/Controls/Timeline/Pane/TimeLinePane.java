package Controls.Timeline.Pane;

import Controls.TimelineElement.StrokeTimeLineElement;
import Controls.TimelineElement.TimeLineElement;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

//Idea: make Timeline an interface? This way timeline operations can be called without referencing the actual control.
public abstract class TimeLinePane extends Pane {

    protected StringProperty timeLineName;
    protected double scale;
    protected Color timeLineColor;
    //Todo: perhaps reference style from a style sheet.
    protected String style = "-fx-padding: 10; -fx-border-style: solid inside; -fx-border-width: 2; -fx-border-insets: 5; -fx-border-radius: 5; -fx-border-color: blue; -fx-background-color: grey";

    protected TimeLinePane(String timeLineName, double width, double height, double scale, Color c){
        this.timeLineName = new SimpleStringProperty(timeLineName);
        this.scale = scale;
        this.timeLineColor = c;

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
        return timeLineName.get();
    }

    public StringProperty getTimeLineNameProperty(){return timeLineName;}

    public void setTimeLineName(String newName){
        this.timeLineName.setValue(newName);
    }

    public Color getTimeLineColor(){return timeLineColor;}
    public void setTimeLineColor(Color c){this.timeLineColor = c;}

    public void deselectTimeLine(){
        for(Node n : getChildren()){
            ((TimeLineElement)n).setSelected(false);
        }
    }

    public void addTimeLineElement(TimeLineElement tle){
        getChildren().add(tle);
        /*
        tle.setOnContextMenuRequested(event -> {
            getElementSpecificContextMenu(tle).show(this, event.getScreenX(), event.getScreenY());
            event.consume(); //Consume event so that the context menu of the Timelinepane doesn't also show up.
        });
        //Assign contextmenu as ContextMenuRequest action
         */
    }

    /*
    This code has been moved to CommentTimeLinePane. However, it could be useful to keep this around for when
    there are ContextMenu actions that need to be available for ALL timelines.

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

     */


}
