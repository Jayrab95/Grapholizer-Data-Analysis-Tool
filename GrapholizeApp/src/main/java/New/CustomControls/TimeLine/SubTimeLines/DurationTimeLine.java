package New.CustomControls.TimeLine.SubTimeLines;

import New.CustomControls.Annotation.AnnotationRectangle;
import New.Model.Entities.Dot;
import New.Observables.ObservablePage;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;

import java.util.List;

public class DurationTimeLine extends DetailTimeLine {

    public DurationTimeLine(double totalLength, double height, DoubleProperty scaleProp, StringProperty name, ObservablePage p, String topicSetID) {
        super(totalLength, height, scaleProp, name, p, topicSetID);
    }

    @Override
    protected void setUp() {
        List<List<Dot>> dotSections = page.getAllDotSectionsForTopicSet(topicSetID);

        for(List<Dot> dots : dotSections) {
            //At least 2 dots are required so that a line can be drawn
            if (dots.size() >= 2) {
                double duration = dots.get(0).getTimeStamp() - dots.get(dots.size() - 1).getTimeStamp();
                AnnotationRectangle rect = new AnnotationRectangle(
                        new SimpleObjectProperty<>(Color.PAPAYAWHIP),
                        new SimpleStringProperty(String.valueOf(duration)),
                        scale,
                        duration,
                        50,
                        dots.get(0).getTimeStamp());
                getChildren().add(rect);
            }
        }
    }
}
