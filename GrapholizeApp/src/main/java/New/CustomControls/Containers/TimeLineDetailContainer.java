package New.CustomControls.Containers;

import New.Characteristics.Characteristic;
import New.CustomControls.Annotation.AnnotationRectangle;
import New.CustomControls.TimeLine.AnnotationTimeLinePane;
import New.CustomControls.TimeLine.SelectableTimeLinePane;
import New.CustomControls.TimeLine.SubTimeLines.DetailCharacteristicTimeLine;
import New.CustomControls.TimeLine.SubTimeLines.PressureTimeLine;
import New.CustomControls.TimeLine.SubTimeLines.VelocityTimeLine;
import New.CustomControls.TimeLine.TimeLinePane;
import New.util.CharacteristicList;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;


import New.Observables.ObservablePage;

import javafx.scene.control.Slider;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.List;

public class TimeLineDetailContainer extends Pane {

    private SelectableTimeLinePane inspectedTimeLine;
    private ScrollPane timelineScrollPane;
    private VBox timelines;
    private GridPane subTimelineTags;
    private Slider scaleSlider;
    private double totalLength;

    ObservablePage activePage;
    DoubleProperty detailScale;

    public TimeLineDetailContainer(TimeLinePane inspectedTimeLine, ObservablePage activePage){
        this.inspectedTimeLine = (SelectableTimeLinePane)inspectedTimeLine;
        this.activePage = activePage;
        this.totalLength = inspectedTimeLine.getTotalLength();
        //Initialize Scaler;
        detailScale = new SimpleDoubleProperty(0.05);
        scaleSlider = initializeSlider(detailScale.get());
        detailScale.bind(scaleSlider.valueProperty());

        timelines = new VBox();
        timelineScrollPane = new ScrollPane();
        subTimelineTags = new GridPane();

        generateDetailContainer();
    }

    private void generateDetailContainer(){
        subTimelineTags.getColumnConstraints().add(new ColumnConstraints(200)); //first column width
        subTimelineTags.getColumnConstraints().add(new ColumnConstraints(50)); //second column width
        subTimelineTags.setVgap(10d); //vertical space between elements in container
        subTimelineTags.setPadding(new Insets(5,5,5,10));
        timelines.setSpacing(10d);
        timelines.setPadding(new Insets(5,5,5,5));

        initializeStrokeTimeline(0);
        initializePressureTimeline(1);
        initializeVelocityTimeline(2);
        initializecharacteristicTimeLines(CharacteristicList.characteristics(),3);

        /*detailScale.addListener((obs, oldVal, newVal) -> {
            subTimelines.setPrefWidth(totalLength * newVal.doubleValue());
        });*/
        timelineScrollPane.setContent(timelines);
        HBox timelinecontainer = new HBox(subTimelineTags, timelineScrollPane);
        timelinecontainer.setMaxWidth(1500);
        this.getChildren().add(new VBox(scaleSlider, timelinecontainer));
    }

    private Slider initializeSlider(double initScale){
        Slider slider = new Slider(0.0d, 1, initScale);
        slider.setMajorTickUnit(0.05);
        slider.setMinorTickCount(0);
        slider.setShowTickMarks(true);
        slider.setSnapToTicks(true);

        slider.setPrefWidth(1000);
        slider.setMaxWidth(1000);
        return slider;
    }

    private void initializeStrokeTimeline(int rowIndex){
        subTimelineTags.add(new Label("Strokes"),0, rowIndex);
        subTimelineTags.add(new Label("Unit"),1, rowIndex);
        subTimelineTags.getRowConstraints().add(new RowConstraints(inspectedTimeLine.getHeight()));
        timelines.getChildren().add(new AnnotationTimeLinePane(
                getLength(),
                inspectedTimeLine.getHeight(),
                detailScale,
                inspectedTimeLine.getTimeLineNameProperty(),
                new SimpleObjectProperty<Color>(Color.BLACK),
                inspectedTimeLine.getAnnotations(),
                inspectedTimeLine.getTopicSetID()
        ));
    }

    private void initializePressureTimeline(int rowIndex){
        subTimelineTags.add(new Label("Pressure"),0, rowIndex);
        subTimelineTags.add(new Label("some Unit"),1, rowIndex);
        subTimelineTags.getRowConstraints().add(new RowConstraints(inspectedTimeLine.getHeight()));
        timelines.getChildren().add(new PressureTimeLine(
                getLength(),
                inspectedTimeLine.getHeight(),
                detailScale,
                new SimpleStringProperty("Pressure"),
                activePage,
                inspectedTimeLine,
                inspectedTimeLine.getTopicSetID()
        ));
    }

    private void initializeVelocityTimeline(int rowIndex){
         subTimelineTags.add(new Label("Velocity"),0, rowIndex);
         subTimelineTags.add(new Label("mm/ms"),1, rowIndex);
         subTimelineTags.getRowConstraints().add(new RowConstraints(inspectedTimeLine.getHeight()));
         timelines.getChildren().add(new VelocityTimeLine(
                getLength(),
                inspectedTimeLine.getHeight(),
                detailScale,
                new SimpleStringProperty("Velocity"),
                activePage,
                inspectedTimeLine.getTopicSetID()
        ));
    }


    private void initializecharacteristicTimeLines(List<Characteristic> characteristics, int rowIndex){
        for (int i = 0; i < characteristics.size() ; i++) {
            Characteristic characteristic = characteristics.get(i);
            int rowPosition = rowIndex + i;
            subTimelineTags.add(new Label(characteristic.getName()),0, rowPosition);
            subTimelineTags.add(new Label(characteristic.getUnitName()),1, rowPosition);
            subTimelineTags.getRowConstraints().add(new RowConstraints(inspectedTimeLine.getHeight()));
            timelines.getChildren().add(new DetailCharacteristicTimeLine(
                    getLength(),
                    inspectedTimeLine.getHeight(),
                    detailScale,
                    new SimpleStringProperty(characteristic.getName()),
                    activePage,
                    inspectedTimeLine.getTopicSetID(),
                    characteristics.get(i)
            ));
        }
    }

    private double getLength(){
        double end = 0;
        for(AnnotationRectangle r : inspectedTimeLine.getAnnotations()){
            if(end < r.getTimeStop()){end = r.getTimeStop();}
        }
        return end;
    }
}
