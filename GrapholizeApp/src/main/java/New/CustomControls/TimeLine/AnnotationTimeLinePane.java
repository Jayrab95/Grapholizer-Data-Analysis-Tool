package New.CustomControls.TimeLine;

import New.CustomControls.Annotation.AnnotationRectangle;
import New.Model.Entities.Segment;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;

import java.util.List;

public class AnnotationTimeLinePane extends TimeLinePane {

    ObjectProperty<Color>c;
    public AnnotationTimeLinePane(double totalLength, double height, DoubleProperty scaleProp, StringProperty name, ObjectProperty<Color> c, List<AnnotationRectangle>l) {
        super(totalLength, height, scaleProp, name);
        this.c=c;
        addAnnotations(l);
    }


    //TODO: This somehow needs to receive the main topic
    private void addAnnotation(Segment a){
        getChildren().add(new AnnotationRectangle(
                c,
                new SimpleStringProperty(),
                scale,
                a.getDuration(),
                this.getHeight(),
                a.getTimeStart()
                ));
    }

    private void addAnnotations(List<AnnotationRectangle> annotations){
        for(AnnotationRectangle a : annotations){
            Segment newSegment = new Segment(a.getTimeStart(), a.getTimeStop());
            addAnnotation(newSegment);
        }
    }
}
