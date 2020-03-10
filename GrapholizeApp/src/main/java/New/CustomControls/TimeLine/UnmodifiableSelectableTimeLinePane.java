package New.CustomControls.TimeLine;

import New.CustomControls.Annotation.SelectableSegmentRectangle;
import New.CustomControls.Containers.TimeLineContainer;
import New.Model.Entities.Segment;
import New.Observables.ObservablePage;
import New.Observables.ObservableSegment;
import New.Observables.ObservableTopicSet;
import javafx.beans.property.DoubleProperty;

import java.util.Optional;
import java.util.Set;

public class UnmodifiableSelectableTimeLinePane extends SelectableTimeLinePane {

    private ObservableTopicSet observableTopicSet;
    public UnmodifiableSelectableTimeLinePane(double width, double height, DoubleProperty scaleProp, ObservableTopicSet observableTopicSet, ObservablePage oPage, TimeLineContainer parent) {
        super(width, height, scaleProp, observableTopicSet.getNameProperty(), parent, observableTopicSet.getTopicSetID());
        this.observableTopicSet = observableTopicSet;
        setUp(oPage.getAnnotationSet(observableTopicSet.getTopicSetID()), oPage);
    }

    private void setUp(Optional<Set<Segment>> segments, ObservablePage p){

        if(segments.isPresent()){
            segments.get().forEach(segment -> {
                ObservableSegment oSegment = new ObservableSegment(segment, observableTopicSet);
                SelectableSegmentRectangle selectableSegmentRectangle = new SelectableSegmentRectangle(
                        this.observableTopicSet.getColorProperty(),
                        oSegment.getToolTipTextProperty(),
                        oSegment.getMainTopicAnnotationProperty(),
                        this.scale,
                        segment.getDuration(),
                        segment.getTimeStart(),
                        this,
                        p,
                        oSegment
                );
                this.getChildren().add(selectableSegmentRectangle);
                this.observableSegments.add(oSegment);
            });
        }
    }

    public ObservableTopicSet getObservableTopicSet() {
        return observableTopicSet;
    }
}
