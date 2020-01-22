package New.CustomControls.TimeLine;


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.Pane;

//Idea: make Timeline an interface? This way timeline operations can be called without referencing the actual control.
//TODO: Separate TimeLinePane into view and controller.
public abstract class TimeLinePane extends Pane {

    protected DoubleProperty scale;
    protected DoubleProperty totalLength;
    protected StringProperty timeLineName;
    private final double height;

    //Todo: perhaps reference style from a style sheet.
    protected String style = "-fx-padding: 10; -fx-border-style: solid inside; -fx-border-width: 2; -fx-border-insets: 5; -fx-border-radius: 5; -fx-border-color: blue; -fx-background-color: grey";

    protected TimeLinePane(double totalLength, double height, DoubleProperty scaleProp, StringProperty name){
        this.totalLength = new SimpleDoubleProperty(totalLength);
        this.totalLength.addListener((observable, oldValue, newValue) -> resizeTimeLine());
        this.scale = new SimpleDoubleProperty(scaleProp.get());
        this.scale.bind(scaleProp);
        this.scale.addListener((observable, oldValue, newValue) -> resizeTimeLine());

        this.timeLineName = new SimpleStringProperty(name.get());
        this.timeLineName.bind(name);
        this.height = height;
        setHeight(height);
        setPrefHeight(height);
        setWidth(this.totalLength.get() * scale.get());
        setPrefWidth(this.totalLength.get() * scale.get());

        InitiateTimeLine();
    }

    public String getTimeLineName(){
        return timeLineName.get();
    }

    public StringProperty getTimeLineNameProperty(){
        return this.timeLineName;
    }

    protected void resizeTimeLine(){
        setWidth(totalLength.get() * scale.get());
        setPrefWidth(totalLength.get() * scale.get());
        setStyle(style);
    }

    private void InitiateTimeLine() {
        setStyle(style);
    }
}
