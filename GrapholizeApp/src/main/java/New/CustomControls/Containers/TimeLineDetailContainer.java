package New.CustomControls.Containers;

import New.Characteristics.Characteristic;
import New.Characteristics.CharacteristicAverageAccelaration;
import New.Characteristics.CharacteristicAveragePressure;
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
import javafx.scene.control.ScrollPane;


import New.Observables.ObservablePage;

import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class TimeLineDetailContainer extends ScrollPane {

    private SelectableTimeLinePane inspectedTimeLine;
    ObservablePage activePage;
    DoubleProperty detaiLScale;

    VBox subTimeLines;

    public TimeLineDetailContainer(TimeLinePane inspectedTimeLine, ObservablePage activePage){
        this.inspectedTimeLine = (SelectableTimeLinePane)inspectedTimeLine;
        this.activePage = activePage;
        detaiLScale = new SimpleDoubleProperty(0.05);
        generateDetailContainer();

    }

    private void generateDetailContainer(){
        subTimeLines = new VBox();
        subTimeLines.getChildren().add(copy());
        subTimeLines.getChildren().addAll(pressure(), velocity());
        subTimeLines.getChildren().addAll(characteristicTimeLines(CharacteristicList.characteristics()));
        this.setContent(subTimeLines);
    }

    private SimpleTimeLineWrapper copy(){
        AnnotationTimeLinePane a = new AnnotationTimeLinePane(
                getLength(),
                inspectedTimeLine.getHeight(),
                detaiLScale,
                inspectedTimeLine.getTimeLineNameProperty(),
                new SimpleObjectProperty<Color>(Color.BLACK),
                inspectedTimeLine.getAnnotations(),
                inspectedTimeLine.getTopicSetID()
        );
        return new SimpleTimeLineWrapper(a);
    }

    private SimpleTimeLineWrapper pressure(){
        PressureTimeLine a = new PressureTimeLine(
                getLength(),
                inspectedTimeLine.getHeight(),
                detaiLScale,
                new SimpleStringProperty("Pressure"),
                activePage,
                inspectedTimeLine,
                inspectedTimeLine.getTopicSetID()
        );
        return new SimpleTimeLineWrapper(a);
    }

    private SimpleTimeLineWrapper velocity(){
        VelocityTimeLine a = new VelocityTimeLine(
                getLength(),
                inspectedTimeLine.getHeight(),
                detaiLScale,
                new SimpleStringProperty("Velocity"),
                activePage,
                inspectedTimeLine.getTopicSetID()
        );
        return new SimpleTimeLineWrapper(a);
    }


    private List<SimpleTimeLineWrapper> characteristicTimeLines(List<Characteristic> characteristics){
        return characteristics.stream()
                .map(numberCharacteristic -> new SimpleTimeLineWrapper(
                        new DetailCharacteristicTimeLine(
                                getLength(),
                                inspectedTimeLine.getHeight(),
                                detaiLScale,
                                new SimpleStringProperty(numberCharacteristic.getName()),
                                activePage,
                                inspectedTimeLine.getTopicSetID(),
                                numberCharacteristic
                        )
                ))
                .collect(Collectors.toList());
    }

    private double getLength(){
        double end = 0;
        for(AnnotationRectangle r : inspectedTimeLine.getAnnotations()){
            if(end < r.getTimeStop()){end = r.getTimeStop();}
        }
        return end;
    }
}
