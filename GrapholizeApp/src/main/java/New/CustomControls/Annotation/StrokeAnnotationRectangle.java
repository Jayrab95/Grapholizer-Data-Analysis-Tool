package New.CustomControls.Annotation;



import New.CustomControls.TimeLine.SelectableTimeLinePane;
import New.Observables.ObservableStroke;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.paint.Color;

public class StrokeAnnotationRectangle extends AnnotationRectangle {

    private ObservableStroke s;
    public StrokeAnnotationRectangle(ObjectProperty<Color> c, DoubleProperty scale, ObservableStroke s, SelectableTimeLinePane parent) {
        super(c, new SimpleStringProperty("Stroke"), scale, s.getDuration() * scale.get(), parent.getHeight(), s.getTimeStart() * scale.get(), parent);
        this.s = s;
        s.getSelectedBooleanProperty().bindBidirectional(this.selected);
    }



}
