package New.CustomControls.TimeLine;

import New.CustomControls.TimeLineContainer;
import New.CustomControls.TimeLineElement.StrokeAnnotation;
import New.Model.Entities.Annotation;
import New.Model.Entities.TimeLineTag;
import New.Model.ObservableModel.ObservableActiveState;
import New.Model.ObservableModel.ObservableStroke;
import New.util.ColorConverter;
import javafx.beans.property.DoubleProperty;

import java.util.List;

public class StrokeDurationTimeLinePane extends TimeLinePane {

    public StrokeDurationTimeLinePane(double width, double height, DoubleProperty scaleProp, ObservableActiveState state, TimeLineTag tag, TimeLineContainer parent) {
        super(width, height, scaleProp, state, tag, parent);
        setUpTimeLine(state.getObservableStrokes());
    }

    private void setUpTimeLine(List<ObservableStroke> strokes){
        for(ObservableStroke s : strokes){
            getChildren().add(new StrokeAnnotation(
                    ColorConverter.convertModelColorToJavaFXColor(s.getSimpleColor()),
                    scale,
                    new Annotation("Stroke", s.getTimeStart(), s.getTimeEnd()),
                    this,
                    s
            ));
        }
    }

}
