package New.CustomControls.Annotation;



import New.CustomControls.TimeLine.SelectableTimeLinePane;
import New.Interfaces.Selector;
import New.Observables.ObservableStroke;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;

public class StrokeSegmentRectangle extends SelectableSegmentRectangle {

    private ObservableStroke s;
    public StrokeSegmentRectangle(ObjectProperty<Color> c, DoubleProperty scale, ObservableStroke s, SelectableTimeLinePane parent, Selector se) {
        super(c, new SimpleStringProperty("Duration: " + s.getDuration()), new SimpleStringProperty(), scale, s.getDuration(), s.getTimeStart(), parent, se);
        this.s = s;
        s.getSelectedBooleanProperty().bindBidirectional(this.selected);
    }


}
