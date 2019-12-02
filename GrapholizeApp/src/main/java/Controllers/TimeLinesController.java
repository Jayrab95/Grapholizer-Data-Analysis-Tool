package Controllers;

import Model.Entities.Project;
import Model.TimeLinesModel;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class TimeLinesController {

    private Project project;
    private DoubleProperty scale;


    public TimeLinesController(Project p, double initialScale){
        this.project = p;
        scale = new SimpleDoubleProperty(initialScale);
    }

}
