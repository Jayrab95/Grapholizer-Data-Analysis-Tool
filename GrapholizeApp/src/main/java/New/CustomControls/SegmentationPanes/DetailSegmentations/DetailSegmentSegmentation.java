package New.CustomControls.SegmentationPanes.DetailSegmentations;

import New.CustomControls.SegmentRectangles.SegmentRectangle;
import New.Model.Entities.Segment;
import New.Observables.ObservablePage;
import javafx.beans.property.*;
import javafx.scene.paint.Color;

public class DetailSegmentSegmentation extends DetailSegmentation {

    private String mainTopicID;

    public DetailSegmentSegmentation(double totalLength, double height, DoubleProperty scaleProp, StringProperty name, ObservablePage p, String topicSetID, String mainTopicID) {
        super(totalLength, height, scaleProp, name, p, topicSetID);
        this.mainTopicID = mainTopicID;
        setUp();
    }

    @Override
    protected void setUp() {
        for(Segment s : page.getPageProperty().get().getSegmentation(topicSetID)){
            SegmentRectangle rect = new SegmentRectangle(
                    new SimpleObjectProperty<>(Color.PAPAYAWHIP),
                    new SimpleStringProperty(s.getAnnotation(mainTopicID)),
                    new SimpleStringProperty(s.getAnnotation(mainTopicID)),
                    scale,
                    s.getDuration(),
                    50,
                    s.getTimeStart()
            );
            getChildren().addAll(rect, rect.getDisplayedTextLabel());
        }
    }
}
