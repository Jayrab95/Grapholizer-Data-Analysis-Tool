package New.CustomControls.TimeLine;

import New.CustomControls.Annotation.StrokeAnnotationRectangle;
import New.CustomControls.Containers.TimeLineContainer;
import New.Interfaces.Observer.PageObserver;
import New.Observables.ObservablePage;
import New.Observables.ObservableStroke;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.List;

public class StrokeDurationTimeLinePane extends SelectableTimeLinePane implements PageObserver {

    private ObservablePage p;

    public StrokeDurationTimeLinePane(double width, double height, DoubleProperty scaleProp, ObservablePage page, TimeLineContainer parent) {
        super(width, height, scaleProp, new SimpleStringProperty("Stroke duration"), parent);
        page.addObserver(this);
        this.p = page;
        setUpTimeLine(page.getObservableStrokes());
    }

    //TODO: Adjust witdh via binding?
    private void setUpTimeLine(List<ObservableStroke> strokes){
        for(ObservableStroke s : strokes){
            StrokeAnnotationRectangle sa = new StrokeAnnotationRectangle(s.getColorProperty(), scale, s, this, p);
            getChildren().add(sa);
        }
    }

    @Override
    public void update(ObservablePage sender) {
        getChildren().clear();
        totalLength.set(sender.getDuration());
        setUpTimeLine(sender.getObservableStrokes());
    }
}
