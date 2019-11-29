package Controls.Timeline.Pane;

import Controls.TimelineElement.TimeLineElementRect;
import Model.Entities.TimeLineElement;
import Model.Entities.Timeline;
import Model.StrokesModel;
import Model.TimeLinesModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import util.ColorConverter;

//Idea: make Timeline an interface? This way timeline operations can be called without referencing the actual control.
//TODO: Separate TimeLinePane into view and controller.
public abstract class TimeLinePane extends Pane {

    protected TimeLinesModel timeLinesModel;

    protected Timeline timeline;
    protected StringProperty timeLineName;
    protected double scale;
    protected Color timeLineColor;
    //Todo: perhaps reference style from a style sheet.
    protected String style = "-fx-padding: 10; -fx-border-style: solid inside; -fx-border-width: 2; -fx-border-insets: 5; -fx-border-radius: 5; -fx-border-color: blue; -fx-background-color: grey";

    protected TimeLinePane(String timeLineName, double width, double height, double scale, Color c){
        this.timeLineName = new SimpleStringProperty(timeLineName);
        this.scale = scale;
        this.timeLineColor = c;

        this.timeline = new Timeline(timeLineName, ColorConverter.convertJavaFXColorToModelColor(c));

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
        this.timeline.setTimeLineName(newName);
    }

    public Color getTimeLineColor(){return timeLineColor;}
    public void setTimeLineColor(Color c){

        this.timeLineColor = c;
        this.timeline.setTimeLineColor(ColorConverter.convertJavaFXColorToModelColor(c));
    }

    public Timeline getTimeline(){return this.timeline;}

    public void deselectTimeLine(){
        for(Node n : getChildren()){
            ((TimeLineElementRect)n).setSelected(false);
        }
    }

    public void addTimeLineElement(TimeLineElementRect tle){
        getChildren().add(tle);
        timeline.addTimeLineElementInOrder(new TimeLineElement(tle.getAnnotationText(), tle.getTimeStart() / scale, tle.getTimeStop() / scale));
        //TODO: Add the TimeLineElemet entity to the TimeLine Entity lis
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
