package Model;

import Observables.ObservableStroke;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.util.List;

//Manages Information that is required for all timelines
public class TimeLinesModel {

    private DoubleProperty totalLength;
    private DoubleProperty scale;
    private List<ObservableStroke> strokes;

    public TimeLinesModel(double totalLength, double scale, List<ObservableStroke> strokes){
        this.totalLength = new SimpleDoubleProperty(totalLength);
        this.scale = new SimpleDoubleProperty(scale);
        this.strokes = strokes;
    }
}
