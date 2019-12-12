package New.CustomControls.TimeLine;

import New.CustomControls.TimeLineElement.StrokeAnnotationRectangle;
import New.Interfaces.Observable;
import New.Interfaces.Observer;
import New.Model.ObservableModel.ObservablePage;
import New.Model.ObservableModel.ObservableStroke;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.List;

public class StrokeDurationTimeLinePane extends TimeLinePane implements Observer {

    public StrokeDurationTimeLinePane(double width, double height, DoubleProperty scaleProp, ObservablePage page) {
        super(width, height, scaleProp, new SimpleStringProperty("Stroke duration"));
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
    public void update(Observable sender) {
        getChildren().clear();
        setUpTimeLine(((ObservablePage)sender).getObservableStrokes());
    }
}
