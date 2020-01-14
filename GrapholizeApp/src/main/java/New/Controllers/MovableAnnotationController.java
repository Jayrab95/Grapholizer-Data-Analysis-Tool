package New.Controllers;

import New.CustomControls.Annotation.AnnotationRectangle;
import New.CustomControls.TimeLine.CustomTimeLinePane;
import New.Model.Entities.Annotation;
import New.Observables.ObservableAnnotation;
import javafx.scene.shape.Rectangle;

public class MovableAnnotationController {

    private final ObservableAnnotation oAnnotation;
    private CustomTimeLinePane parent;


    public MovableAnnotationController(ObservableAnnotation oAnnotation, CustomTimeLinePane parent){
        this.oAnnotation = oAnnotation;
        this.parent = parent;
    }

    public Annotation getObservableAnnotation() {
        return oAnnotation.getAnnotation();
    }

    public void editElement(String newAnnotationText){
        oAnnotation.setAnnotationText(newAnnotationText);
    }

    public void removeElement(AnnotationRectangle rect){
        parent.getChildren().remove(rect);
    }

    public void moveElement(double delta){
        oAnnotation.getAnnotation().move(delta);
    }

    public double[] getBounds(double xPosition){
        return parent.getBounds(xPosition);
    }

    public void adjustTimeStart(double newTimeStart){
        oAnnotation.setTimeStart(newTimeStart);
    }

    public void adjustTimeStop(double newTimeStop){
        oAnnotation.setTimeStop(newTimeStop);
    }

    public void enableResize(Rectangle left, Rectangle right){ parent.getChildren().addAll(left, right); }

    public void disableResize(Rectangle left, Rectangle right){
        parent.getChildren().removeAll(left, right);
    }

}
