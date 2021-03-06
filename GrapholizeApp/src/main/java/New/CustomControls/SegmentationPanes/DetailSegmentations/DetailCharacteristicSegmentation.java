package New.CustomControls.SegmentationPanes.DetailSegmentations;

import New.Characteristics.Characteristic;
import New.CustomControls.SegmentRectangles.SegmentRectangle;
import New.Model.Entities.Segment;
import New.Observables.ObservablePage;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;

public class DetailCharacteristicSegmentation extends DetailSegmentation {

    Characteristic<Number> characteristic;


    public DetailCharacteristicSegmentation(double totalLength, double height, DoubleProperty scaleProp, StringProperty name, ObservablePage p, String topicSetID, Characteristic<Number> characteristic) {
        super(totalLength, height, scaleProp, name, p, topicSetID);
        this.characteristic = characteristic;
        setUp();
    }

    protected void setUp() {
        for(Segment s : page.getPageProperty().get().getSegmentation(topicSetID)){

            Number d = characteristic.calculate(s, page.getAllStrokes());
            SimpleStringProperty prop = new SimpleStringProperty(String.valueOf(d));

            SegmentRectangle rect = new SegmentRectangle(
                    new SimpleObjectProperty<>(Color.PAPAYAWHIP),
                    prop,
                    prop,
                    scale,
                    s.getDuration(),
                    50,
                    s.getTimeStart());
            getChildren().addAll(rect, rect.getDisplayedTextLabel());
        }
    }
}
