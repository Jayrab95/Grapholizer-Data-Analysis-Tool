package New.CustomControls.TimeLine;

import New.CustomControls.Annotation.SelectableSegmentRectangle;
import New.CustomControls.Containers.TimeLineContainer;
import New.Model.Entities.Segment;
import New.Observables.ObservablePage;
import New.Observables.ObservableSegment;
import New.Observables.ObservableTopicSet;
import javafx.beans.property.DoubleProperty;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class UnmodifiableSelectableTimeLinePane extends SelectableTimeLinePane {

    private ObservablePage p;
    private ObservableTopicSet ts;
    public UnmodifiableSelectableTimeLinePane(double width, double height, DoubleProperty scaleProp, ObservableTopicSet ts, ObservablePage oPage, TimeLineContainer parent) {
        super(width, height, scaleProp, ts.getNameProperty(), parent, ts.getTopicSetID());
        this.p = oPage;
        this.ts=ts;
        setUp(oPage.getAnnotationSet(ts.getTopicSetID()));
    }

    private void setUp(Optional<Set<Segment>> segments){

        if(segments.isPresent()){
            segments.get().forEach(segment -> {
                ObservableSegment oSegment = new ObservableSegment(segment, ts);
                SelectableSegmentRectangle selectableSegmentRectangle = new SelectableSegmentRectangle(
                        this.ts.getColorProperty(),
                        oSegment.getToolTipTextProperty(),
                        oSegment.getMainTopicAnnotationProperty(),
                        this.scale,
                        segment.getDuration(),
                        segment.getTimeStart(),
                        this,
                        this.p,
                        oSegment
                );
                this.getChildren().add(selectableSegmentRectangle);
                this.observableSegments.add(oSegment);
            });
        }
    }
}
