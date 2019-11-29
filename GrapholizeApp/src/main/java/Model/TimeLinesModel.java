package Model;

import Observables.ObservableStroke;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.util.List;

//Manages Information that is required for all timelines
public class TimeLinesModel {

    private StrokesModel strokesModel;
    private DoubleProperty scale;

    public TimeLinesModel(StrokesModel strokesModel, double scale){
        this.strokesModel = strokesModel;
        this.scale = new SimpleDoubleProperty(scale);
    }

    public StrokesModel getStrokesModel(){return strokesModel;}
    public DoubleProperty getScaleProperty(){return scale;}
    public double getScale(){return scale.get();}
    public void setSacle(double newScale){scale.set(newScale);}

}
