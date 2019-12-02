package Model;

import Model.Entities.Project;
import Observables.ObservableStroke;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.util.List;

//Manages Information that is required for all timelines
public class TimeLinesModel {

    private Project project;
    private DoubleProperty scale;
    private StrokesModel stroeksModel;

    public TimeLinesModel(StrokesModel sm, double scale){
        //this.project = project;
        stroeksModel = sm;
        this.scale = new SimpleDoubleProperty(scale);
    }

    public Project getProject(){return project;}
    public DoubleProperty getScaleProperty(){return scale;}
    public double getScale(){return scale.get();}
    public void setScale(double newScale){scale.set(newScale);}
    public StrokesModel getStrokesModel(){return stroeksModel;}

}
