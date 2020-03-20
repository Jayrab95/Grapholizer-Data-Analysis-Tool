package New.CustomControls.SegmentationPanes.DetailSegmentations;

import New.CustomControls.SegmentRectangles.SegmentRectangle;
import New.Model.Entities.Dot;
import New.Observables.ObservablePage;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;

import java.util.List;

public class DetailStrokesSegmentation extends DetailSegmentation {
    public DetailStrokesSegmentation(double totalLength, double height, DoubleProperty scaleProp, StringProperty name, ObservablePage p, String topicSetID) {
        super(totalLength, height, scaleProp, name, p, topicSetID);
        setUp();
    }

    @Override
    protected void setUp() {
        for(List<Dot> dotSection : page.getAllDotSectionsForTopicSet(topicSetID)) {
            if (dotSection.size() > 1) {
                long duration = calcDuration(dotSection.get(0).getTimeStamp(), dotSection.get(dotSection.size() - 1).getTimeStamp());
                SegmentRectangle rect = new SegmentRectangle(
                        new SimpleObjectProperty<>(Color.PAPAYAWHIP),
                        new SimpleStringProperty(String.format("Duration: %d", duration)),
                        new SimpleStringProperty(String.valueOf(duration)),
                        scale,
                        duration,
                        50,
                        dotSection.get(0).getTimeStamp()
                );
                getChildren().addAll(rect, rect.getDisplayedTextLabel());
            }
        }
    }

    private long calcDuration(long start, long stop){
        return stop - start;
    }
}
