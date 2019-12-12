package New.CustomControls.TimeLine;


import New.Controllers.TimeLineController;
import New.CustomControls.TimeLineContainer;
import New.CustomControls.TimeLineElement.AnnotationRectangle;
import New.Model.Entities.SimpleColor;
import New.Model.Entities.TimeLineTag;

import New.Model.ObservableModel.ObservablePage;
import New.Model.ObservableModel.ObservableTimeLineTag;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

//Idea: make Timeline an interface? This way timeline operations can be called without referencing the actual control.
//TODO: Separate TimeLinePane into view and controller.
public abstract class TimeLinePane extends Pane {

    protected DoubleProperty scale;
    protected StringProperty timeLinename;
    protected ObjectProperty<SimpleColor> timeLineColor;
    protected TimeLineController timeLineController;

    //Todo: perhaps reference style from a style sheet.
    protected String style = "-fx-padding: 10; -fx-border-style: solid inside; -fx-border-width: 2; -fx-border-insets: 5; -fx-border-radius: 5; -fx-border-color: blue; -fx-background-color: grey";

    protected TimeLinePane(double width, double height, DoubleProperty scaleProp, ObservableTimeLineTag tag, ObservablePage p, TimeLineContainer parent){
        this.scale = new SimpleDoubleProperty(scale.get());
        this.scale.bind(scaleProp);
        this.scale.addListener((observable, oldValue, newValue) -> onScaleValueChange());
        this.timeLineController = new TimeLineController(tag, p, parent);

        setHeight(height);
        setPrefHeight(height);
        setWidth(width * scale.get());
        setPrefWidth(width * scale.get());

        InitiateTimeLine();
    }

    protected void onScaleValueChange(){
        setWidth(getWidth() * scale.get());
    }



    private void InitiateTimeLine() {
        setStyle(style);
    }


    /**
     * Sets the bounds for the drag function.
     * @param xPosition Current xPosition on MousePress.
     */
    public double[] getBounds(double xPosition){
        //TODO: Move into controller
        double lowerBounds = 0;
        double upperBounds = getWidth();
        for(Node n : getChildren()){
            AnnotationRectangle rect = (AnnotationRectangle)n;
            double nTimeStart = rect.getX();
            double nTimeStop = rect.getX() + rect.getWidth();
            if(nTimeStop < xPosition && nTimeStop > lowerBounds) {
                lowerBounds = nTimeStop;
            }
            if(nTimeStart > xPosition && nTimeStart < upperBounds) {
                upperBounds = nTimeStart;
            }
        }
        return new double[]{lowerBounds, upperBounds};
    }
    /*
    public void deselectTimeLine(){
        for(Node n : getChildren()){
            ((TimeLineElementRect)n).setSelected(false);
        }
    }

    public void addTimeLineElement(TimeLineElementRect tle){
        getChildren().add(tle);
        //Controller needs to add the timeLineElement to the page.
    }
     */


}
