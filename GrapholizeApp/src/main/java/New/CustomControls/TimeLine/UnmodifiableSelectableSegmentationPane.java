package New.CustomControls.TimeLine;

import New.CustomControls.Annotation.SelectableSegmentRectangle;
import New.CustomControls.Containers.TimeLineContainer;
import New.Model.Entities.Segment;
import New.Observables.ObservablePage;
import New.Observables.ObservableSegment;
import New.Observables.ObservableSuperSet;
import javafx.beans.property.DoubleProperty;

import java.util.Optional;
import java.util.Set;

public class UnmodifiableSelectableSegmentationPane extends SelectableSegmentationPane {

    private ObservableSuperSet observableSuperSet;
    public UnmodifiableSelectableSegmentationPane(double width, double height, DoubleProperty scaleProp, ObservableSuperSet observableSuperSet, ObservablePage oPage, TimeLineContainer parent) {
        super(width, height, scaleProp, observableSuperSet.getNameProperty(), parent, observableSuperSet.getTopicSetID());
        this.observableSuperSet = observableSuperSet;
        setUp(oPage.getAnnotationSet(observableSuperSet.getTopicSetID()), oPage);
    }

    private void setUp(Optional<Set<Segment>> segments, ObservablePage p){

        if(segments.isPresent()){
            segments.get().forEach(segment -> {
                ObservableSegment oSegment = new ObservableSegment(segment, observableSuperSet);
                SelectableSegmentRectangle selectableSegmentRectangle = new SelectableSegmentRectangle(
                        this.observableSuperSet.getColorProperty(),
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

    public ObservableSuperSet getObservableSuperSet() {
        return observableSuperSet;
    }
}
