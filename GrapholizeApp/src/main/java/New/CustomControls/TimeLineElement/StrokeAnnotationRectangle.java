package New.CustomControls.TimeLineElement;



import New.CustomControls.TimeLine.TimeLinePane;
import New.Model.ObservableModel.ObservableStroke;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.paint.Color;

public class StrokeAnnotationRectangle extends AnnotationRectangle {

    private ObservableStroke s;
    public StrokeAnnotationRectangle(ObjectProperty<Color> c, DoubleProperty scale, ObservableStroke s, TimeLinePane parent) {
        super(c, new SimpleStringProperty("Stroke"), scale, s.getDuration() * scale.get(), parent.getHeight(), s.getTimeStart() * scale.get());
        this.s = s;
        s.getSelectedBooleanProperty().bindBidirectional(selected);
    }



}
