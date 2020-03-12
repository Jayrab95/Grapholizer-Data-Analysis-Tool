package New.CustomControls.TimeLine;


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.Pane;

//Idea: make Timeline an interface? This way timeline operations can be called without referencing the actual control.
//TODO: Separate TimeLinePane into view and controller.
public abstract class SegmentationPane extends Pane {

    protected DoubleProperty scale;
    protected DoubleProperty totalLength;
    protected StringProperty timeLineName;
    protected final String topicSetID;

    //Todo: perhaps reference style from a style sheet.
    protected String defaultStyle = "-fx-padding: 10; -fx-border-style: solid inside; -fx-border-width: 2; -fx-border-insets: 5; -fx-border-radius: 5; -fx-background-color: grey";

    protected SegmentationPane(double totalLength, double height, DoubleProperty scaleProp, StringProperty name, String id){
        this.totalLength = new SimpleDoubleProperty(totalLength);

        this.scale = new SimpleDoubleProperty(scaleProp.get());
        this.scale.bind(scaleProp);
        this.scale.addListener((observable, oldValue, newValue) -> resizeTimeLine());


        this.timeLineName = new SimpleStringProperty(name.get());
        this.timeLineName.bind(name);
        this.topicSetID = id;

        setHeight(height);
        setPrefHeight(height);
        setWidth(this.totalLength.get() * scale.get());
        setPrefWidth(this.totalLength.get() * scale.get());

        InitiateTimeLine();
    }

    public String getTimeLineName(){
        return timeLineName.get();
    }

    public String getTopicSetID() {
        return topicSetID;
    }

    public StringProperty getTimeLineNameProperty(){
        return this.timeLineName;
    }

    public double getTotalLength() { return totalLength.get(); }

    public DoubleProperty totalLengthProperty() {
        return totalLength;
    }

    protected void resizeTimeLine(){
        setWidth(totalLength.get() * scale.get());
        setPrefWidth(totalLength.get() * scale.get());
        setStyle(defaultStyle);
    }

    private void InitiateTimeLine() {
        setStyle(defaultStyle);
    }

    public void cleanUp(){
        scale.unbind();
        totalLength.unbind();
        timeLineName.unbind();
        timeLineName.unbind();
    }


}
