package New.CustomControls.Containers;

import New.Characteristics.Characteristic;
import New.CustomControls.SegmentRectangles.SegmentRectangle;
import New.CustomControls.SegmentationPanes.*;
import New.CustomControls.SegmentationPanes.DetailSegmentations.*;
import New.util.CharacteristicList;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;


import New.Observables.ObservablePage;

import javafx.scene.control.Slider;
import javafx.scene.layout.*;

import java.util.List;

public class SegmentationDetailContainer extends ScrollPane {

    private SelectableSegmentationPane inspectedSegmentation;
    private ScrollPane timelineScrollPane;
    private VBox subTimelines;
    private GridPane subTimelineTags;
    private Slider scaleSlider;
    private TimeUnitPane unitPane;

    ObservablePage activePage;
    DoubleProperty detailScale;

    public SegmentationDetailContainer(SegmentationPane inspectedSegmentation, ObservablePage activePage){
        this.inspectedSegmentation = (SelectableSegmentationPane) inspectedSegmentation;
        this.activePage = activePage;
        this.setHbarPolicy(ScrollBarPolicy.NEVER);
        //Initialize Scaler;
        detailScale = new SimpleDoubleProperty(0.05);
        scaleSlider = initializeSlider(detailScale.get());
        detailScale.bind(scaleSlider.valueProperty());

        subTimelines = new VBox();
        timelineScrollPane = new ScrollPane();
        subTimelineTags = new GridPane();
        unitPane = new TimeUnitPane(detailScale,20, inspectedSegmentation.totalLengthProperty());

        generateDetailContainer();
    }

    private void generateDetailContainer(){
        subTimelineTags.getColumnConstraints().add(new ColumnConstraints(180)); //first column width
        subTimelineTags.getColumnConstraints().add(new ColumnConstraints(60)); //second column width
        subTimelineTags.setVgap(10d); //vertical space between elements in container
        subTimelineTags.setPadding(new Insets(70,5,5,10));
        subTimelines.setSpacing(10d);
        subTimelines.setPadding(new Insets(25,5,5,5));

        subTimelines.getChildren().add(unitPane); //add time scale
        initializeSegmentTimeLine(0);
        initializeStrokeDetailTimeLine(1);
        initializePressureTimeline(2);
        initializeVelocityTimeline(3);
        initializeCharacteristicTimeLines(CharacteristicList.characteristics(),5);

        timelineScrollPane.setContent(subTimelines);
        HBox timelinecontainer = new HBox(subTimelineTags, timelineScrollPane);
        timelinecontainer.setMaxWidth(1500);
        this.setContent(new VBox(scaleSlider, timelinecontainer));
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


    private void initializeSegmentTimeLine(int rowIndex){
        subTimelineTags.add(new Label("Segments"),0, rowIndex);
        subTimelineTags.add(new Label("Maintopic"),1, rowIndex);
        subTimelineTags.getRowConstraints().add(new RowConstraints(inspectedSegmentation.getHeight()));
        subTimelines.getChildren().add(new DetailSegmentSegmentation(
                getLength(),
                inspectedSegmentation.getHeight()
                , detailScale,
                new SimpleStringProperty("Segments"),
                activePage,
                inspectedSegmentation.getTopicSetID(),
                inspectedSegmentation instanceof CustomSegmentationPane ? ((CustomSegmentationPane) inspectedSegmentation).getObservableSuperSet().getMainTopicID() : ""
        ));
    }

    private void initializeStrokeDetailTimeLine(int rowIndex){
        subTimelineTags.add(new Label("Strokes"),0, rowIndex);
        subTimelineTags.add(new Label("Milliseconds"),1, rowIndex);
        subTimelineTags.getRowConstraints().add(new RowConstraints(inspectedSegmentation.getHeight()));
        subTimelines.getChildren().add(new DetailStrokesSegmentation(
                getLength(),
                inspectedSegmentation.getHeight()
                , detailScale,
                new SimpleStringProperty("Segments"),
                activePage,
                inspectedSegmentation.getTopicSetID()
        ));
    }


    private void initializePressureTimeline(int rowIndex){
        subTimelineTags.add(new Label("Pressure"),0, rowIndex);
        subTimelineTags.add(new Label("some Unit"),1, rowIndex);
        subTimelineTags.getRowConstraints().add(new RowConstraints(inspectedSegmentation.getHeight()));
        subTimelines.getChildren().add(new PressureSegmentation(
                getLength(),
                inspectedSegmentation.getHeight(),
                detailScale,
                new SimpleStringProperty("Pressure"),
                activePage,
                inspectedSegmentation,
                inspectedSegmentation.getTopicSetID()
        ));
    }

    private void initializeVelocityTimeline(int rowIndex){
         subTimelineTags.add(new Label("Velocity"),0, rowIndex);
         subTimelineTags.add(new Label("mm/ms"),1, rowIndex);
         subTimelineTags.getRowConstraints().add(new RowConstraints(inspectedSegmentation.getHeight()));
         subTimelines.getChildren().add(new VelocitySegmentation(
                getLength(),
                inspectedSegmentation.getHeight(),
                detailScale,
                new SimpleStringProperty("Velocity"),
                activePage,
                inspectedSegmentation.getTopicSetID()
        ));

        subTimelineTags.add(new Label("Velocity Buttered"),0, rowIndex + 1);
        subTimelineTags.add(new Label("mm/ms"),1, rowIndex + 1);
        subTimelineTags.getRowConstraints().add(new RowConstraints(inspectedSegmentation.getHeight()));
        subTimelines.getChildren().add(new VelocityButteredSegmentation(
                getLength(),
                inspectedSegmentation.getHeight(),
                detailScale,
                new SimpleStringProperty("Velocity"),
                activePage,
                inspectedSegmentation.getTopicSetID()
        ));
    }

    private void initializeCharacteristicTimeLines(List<Characteristic> characteristics, int rowIndex){
        for (int i = 0; i < characteristics.size() ; i++) {
            Characteristic characteristic = characteristics.get(i);
            int rowPosition = rowIndex + i;
            subTimelineTags.add(new Label(characteristic.getName()),0, rowPosition);
            subTimelineTags.add(new Label(characteristic.getUnitName()),1, rowPosition);
            subTimelineTags.getRowConstraints().add(new RowConstraints(inspectedSegmentation.getHeight()));
            subTimelines.getChildren().add(new DetailCharacteristicSegmentation(
                    getLength(),
                    inspectedSegmentation.getHeight(),
                    detailScale,
                    new SimpleStringProperty(characteristic.getName()),
                    activePage,
                    inspectedSegmentation.getTopicSetID(),
                    characteristics.get(i)
            ));
        }
    }

    private double getLength(){
        double end = 0;
        for(SegmentRectangle r : inspectedSegmentation.getSegmentRectangles()){
            if(end < r.getTimeStop()){end = r.getTimeStop();}
        }
        return end;
    }
}
