package New.Controllers;

import New.CustomControls.Annotation.MutableSegmentRectangle;
import New.CustomControls.TimeLine.CustomTimeLinePane;
import New.Model.Entities.Segment;
import New.Observables.ObservableSegment;
import javafx.scene.shape.Rectangle;

import java.util.Map;

public class MutableSegmentController {

    private final ObservableSegment oAnnotation;
    private CustomTimeLinePane parent;


    public MutableSegmentController(ObservableSegment oAnnotation, CustomTimeLinePane parent){
        this.oAnnotation = oAnnotation;
        this.parent = parent;
    }

    public Segment getObservableAnnotation() {
        return oAnnotation.getSegment();
    }

    public void editElement(String newAnnotationText){
        //oAnnotation.setAnnotationText(newAnnotationText);
    }

    public void removeElement(MutableSegmentRectangle rect){
        parent.deleteSegment(rect, oAnnotation);
    }

    public void moveElement(double delta){
        oAnnotation.getSegment().move(delta);
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

    public boolean fitsFilterCriteria(Map<String, String> topicFilters){
        return oAnnotation.fitsFilterCriteria(topicFilters);
    }

}