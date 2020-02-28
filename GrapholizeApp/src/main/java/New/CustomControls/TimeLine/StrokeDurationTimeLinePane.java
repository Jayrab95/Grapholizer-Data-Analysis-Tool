package New.CustomControls.TimeLine;

import New.CustomControls.Annotation.StrokeSegmentRectangle;
import New.CustomControls.Containers.TimeLineContainer;
import New.Observables.ObservablePage;
import New.Observables.ObservableStroke;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.List;

public class StrokeDurationTimeLinePane extends SelectableTimeLinePane {

    private ObservablePage p;

    public StrokeDurationTimeLinePane(double width, double height, DoubleProperty scaleProp, ObservablePage page, TimeLineContainer parent) {
        super(width, height, scaleProp, new SimpleStringProperty("Stroke duration"), parent, "Stroke duration");
        this.p = page;
        setUpTimeLine(page.getObservableStrokes());
    }

    //TODO: Adjust witdh via binding?
    private void setUpTimeLine(List<ObservableStroke> strokes){
        for(ObservableStroke s : strokes){
            StrokeSegmentRectangle sa = new StrokeSegmentRectangle(s.getColorProperty(), scale, s, this, p);
            getChildren().add(sa);
        }
    }

}
