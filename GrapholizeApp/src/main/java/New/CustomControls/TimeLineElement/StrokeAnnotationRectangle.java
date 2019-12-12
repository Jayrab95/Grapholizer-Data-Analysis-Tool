package New.CustomControls.TimeLineElement;



import New.CustomControls.TimeLine.TimeLinePane;
import New.Model.Entities.Annotation;
import New.Model.ObservableModel.ObservableStroke;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.scene.paint.Color;

public class StrokeAnnotation extends AnnotationRectangle {

    ObservableStroke s;
    public StrokeAnnotation(ObjectProperty<Color> c, DoubleProperty scale, Annotation tle, TimeLinePane tlp, ObservableStroke s) {
        super(c, scale, tle, tlp);
        this.s = s;
        s.getSelectedBooleanProperty().bindBidirectional(selected);
    }

    public ObservableStroke getElementStroke(){
        return this.s;
    }



}
