package New.CustomControls.TimeLine.SubTimeLines;

import New.Characteristics.Characteristic;
import New.CustomControls.Annotation.AnnotationRectangle;
import New.CustomControls.TimeLine.TimeLinePane;
import New.Model.Entities.Segment;
import New.Observables.ObservablePage;
import New.util.PageUtil;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;

import java.util.List;

public class DetailCharacteristicTimeLine extends TimeLinePane {
    Characteristic<Number> characteristic;
    ObservablePage page;


    public DetailCharacteristicTimeLine(double totalLength, double height, DoubleProperty scaleProp, StringProperty name, ObservablePage p, String topicSetID, Characteristic<Number> characteristic) {
        //Note: Cannot extend from DetailTimeLine because the characteristic needs to be initialized before super is executed.
        //(The constructor of detailTimeLine executes setUp, which requires the characteristic in the case of this class)
        super(totalLength, height, scaleProp, name, topicSetID);
        this.page = p;
        this.characteristic = characteristic;
        setUp();
    }

    protected void setUp() {
        for(Segment s : page.getPageProperty().get().getSegmentation(topicSetID)){
            Number d = characteristic.calculate(PageUtil.getDotSectionsForAnnotation(s, page.getAllStrokes()));
            AnnotationRectangle rect = new AnnotationRectangle(
                    new SimpleObjectProperty<>(Color.PAPAYAWHIP),
                    new SimpleStringProperty(String.valueOf(d)),
                    scale,
                    s.getDuration(),
                    50,
                    s.getTimeStart());
            getChildren().addAll(rect, rect.getDisplayedText());
        }
    }
}
