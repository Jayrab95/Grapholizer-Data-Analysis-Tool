package New.CustomControls.TimeLine;

import New.CustomControls.Annotation.StrokeAnnotationRectangle;
import New.CustomControls.TimeLineContainer;
import New.Interfaces.Observable;
import New.Interfaces.Observer.Observer;
import New.Interfaces.Observer.PageObserver;
import New.Model.ObservableModel.ObservablePage;
import New.Model.ObservableModel.ObservableStroke;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.List;

public class StrokeDurationTimeLinePane extends SelectableTimeLinePane implements PageObserver {

    public StrokeDurationTimeLinePane(double width, double height, DoubleProperty scaleProp, ObservablePage page, TimeLineContainer parent) {
        super(width, height, scaleProp, new SimpleStringProperty("Stroke duration"), parent);
        page.addObserver(this);
        setUpTimeLine(page.getObservableStrokes());
    }

    private void setUpTimeLine(List<ObservableStroke> strokes){
        for(ObservableStroke s : strokes){
            StrokeAnnotationRectangle sa = new StrokeAnnotationRectangle(s.getColorProperty(), scale, s, this);
            getChildren().add(sa);
        }
    }

    @Override
    public void update(ObservablePage sender) {
        if(sender.getClass() == ObservablePage.class){
            getChildren().clear();
            ObservablePage p = (ObservablePage)sender;
            setUpTimeLine(p.getObservableStrokes());
        }

    }
}
