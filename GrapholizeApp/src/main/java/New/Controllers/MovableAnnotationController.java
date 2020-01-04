package New.Controllers;

import New.CustomControls.Annotation.AnnotationRectangle;
import New.CustomControls.TimeLine.CustomTimeLinePane;
import New.Model.Entities.Annotation;
import New.Observables.ObservableAnnotation;
import javafx.scene.shape.Rectangle;

public class MovableAnnotationController {

    private final ObservableAnnotation annotation;
    private CustomTimeLinePane parent;


    public MovableAnnotationController(ObservableAnnotation annotation, CustomTimeLinePane parent){
        this.annotation = annotation;
        this.parent = parent;
    }

    public Annotation getAnnotation() {
        return annotation.getAnnotation();
    }

    public void editElement(String newAnnotationText){
        annotation.setAnnotationText(newAnnotationText);
    }

    public void removeElement(AnnotationRectangle rect){
        parent.getChildren().remove(rect);
    }

    public void moveElement(double delta){
        annotation.getAnnotation().move(delta);
    }

    public double[] getBounds(double xPosition){
        return parent.getBounds(xPosition);
    }

    public void adjustTimeStart(double newTimeStart){
        annotation.setTimeStart(newTimeStart);
    }

    public void adjustTimeStop(double newTimeStop){
        annotation.setTimeStop(newTimeStop);
    }

    public void enableResize(Rectangle left, Rectangle right){
        parent.getChildren().addAll(left, right);
    }

    public void disableResize(Rectangle left, Rectangle right){
        parent.getChildren().removeAll(left, right);
    }

}
