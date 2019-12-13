package New.Controllers;

import New.CustomControls.TimeLine.CustomTimeLinePane;
import New.CustomControls.TimeLine.TimeLinePane;
import New.Model.Entities.Annotation;
import New.Model.ObservableModel.ObservableAnnotation;

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

    public void removeElement(){
        parent.getChildren().remove(this);
    }

    public void moveElement(double delta){
        annotation.getAnnotation().move(delta);
    }

    public double[] getBounds(double xPosition){
        return parent.getBounds(xPosition);
    }


}
