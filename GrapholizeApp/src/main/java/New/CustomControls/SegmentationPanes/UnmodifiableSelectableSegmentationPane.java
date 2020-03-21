package New.CustomControls.SegmentationPanes;

import New.CustomControls.SegmentRectangles.SelectableSegmentRectangle;
import New.CustomControls.Containers.SegmentationContainer;
import New.Model.Entities.Segment;
import New.Observables.ObservablePage;
import New.Observables.ObservableSegment;
import New.Observables.ObservableSuperSet;
import javafx.beans.property.DoubleProperty;

import java.util.Optional;
import java.util.Set;

/**
 * The UnmodifiabeSegmentationPane is similar to the DetailSegmentation, in that the segments are
 * added upon creation and cannot be edited or removed afterwards. Because it inherits logic from
 * the SelectableSegmentationPane, the segmentation can still be selected. This class is mainly
 * used for the Stroke duration segmentation, which each page receives per default, since the
 * segments on this segmentation are not to be moved or modified.
 */
public class UnmodifiableSelectableSegmentationPane extends SelectableSegmentationPane {

    private ObservableSuperSet observableSuperSet;
    public UnmodifiableSelectableSegmentationPane(double width, double height, DoubleProperty scaleProp, ObservableSuperSet observableSuperSet, ObservablePage oPage, SegmentationContainer parent) {
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

    /**
     * Returns the ObservableSuperSet this Segmentation has been defined under
     * @return the object described above
     */
    public ObservableSuperSet getObservableSuperSet() {
        return observableSuperSet;
    }
}
