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
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;


import New.Observables.ObservablePage;

import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.stream.Collectors;

public class TimeLineDetailContainer extends ScrollPane {

    private SelectableTimeLinePane inspectedTimeLine;
    private ScrollPane timelineScrollPane;
    private GridPane subTimelines;
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
        generateDetailContainer();
    }

    private void generateDetailContainer(){
        timelineScrollPane = new ScrollPane();
        subTimelines = new GridPane();
        subTimelines.autosize();
        subTimelines.setVgap(10d);
        copy(0);
        pressure(1);
        velocity(2);
        characteristicTimeLines(CharacteristicList.characteristics(),3);

        /*detailScale.addListener((obs, oldVal, newVal) -> {
            subTimelines.setPrefWidth(totalLength * newVal.doubleValue());
        });*/
        //timelineScrollPane.setContent(subTimelines);
        this.setContent(new VBox(scaleSlider, subTimelines));
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

    private void copy(int rowIndex){
        subTimelines.add(new Label("Strokes"),0, rowIndex);
        subTimelines.add(new Label("Unit"),1, rowIndex);
        subTimelines.add(new AnnotationTimeLinePane(
                getLength(),
                inspectedTimeLine.getHeight(),
                detailScale,
                inspectedTimeLine.getTimeLineNameProperty(),
                new SimpleObjectProperty<Color>(Color.BLACK),
                inspectedTimeLine.getAnnotations(),
                inspectedTimeLine.getTopicSetID()
        ), 2 ,rowIndex);
    }

    private void pressure(int rowIndex){
        subTimelines.add(new Label("Pressure"),0, rowIndex);
        subTimelines.add(new Label("some Unit"),1, rowIndex);
        subTimelines.add(new PressureTimeLine(
                getLength(),
                inspectedTimeLine.getHeight(),
                detailScale,
                new SimpleStringProperty("Pressure"),
                activePage,
                inspectedTimeLine,
                inspectedTimeLine.getTopicSetID()
        ), 2, rowIndex);
    }

    private void velocity(int rowIndex){
         subTimelines.add(new Label("Velocity"),0, rowIndex);
         subTimelines.add(new Label("mm/ms"),1, rowIndex);
         subTimelines.add(new VelocityTimeLine(
                getLength(),
                inspectedTimeLine.getHeight(),
                detailScale,
                new SimpleStringProperty("Velocity"),
                activePage,
                inspectedTimeLine.getTopicSetID()
        ), 2, rowIndex);
    }


    private void characteristicTimeLines(List<Characteristic> characteristics, int rowIndex){
        for (int i = 0; i < characteristics.size() ; i++) {
            Characteristic characteristic = characteristics.get(i);
            int rowPosition = rowIndex + i;
            subTimelines.add(new Label(characteristic.getName()),0, rowPosition);
            subTimelines.add(new Label(characteristic.getUnitName()),1, rowPosition);
            subTimelines.add(new DetailCharacteristicTimeLine(
                    getLength(),
                    inspectedTimeLine.getHeight(),
                    detailScale,
                    new SimpleStringProperty(characteristic.getName()),
                    activePage,
                    inspectedTimeLine.getTopicSetID(),
                    characteristics.get(i)
            ), 2, rowPosition);
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
