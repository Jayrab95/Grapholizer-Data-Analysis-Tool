package New.CustomControls.TimeLine.SubTimeLines;

import New.Characteristics.Characteristic;
import New.CustomControls.Annotation.SegmentRectangle;
import New.CustomControls.TimeLine.TimeLinePane;
import New.Model.Entities.Segment;
import New.Observables.ObservablePage;
import New.util.PageUtil;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;

public class DetailCharacteristicTimeLine extends DetailTimeLine {
    Characteristic<Number> characteristic;
    ObservablePage page;


    public DetailCharacteristicTimeLine(double totalLength, double height, DoubleProperty scaleProp, StringProperty name, ObservablePage p, String topicSetID, Characteristic<Number> characteristic) {
        super(totalLength, height, scaleProp, name, p, topicSetID);
        this.page = p;
        this.characteristic = characteristic;
        setUp();
    }

    protected void setUp() {
        for(Segment s : page.getPageProperty().get().getSegmentation(topicSetID)){
            Number d = characteristic.calculate(PageUtil.getDotSectionsForAnnotation(s, page.getAllStrokes()));
            SegmentRectangle rect = new SegmentRectangle(
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
