package New.Controllers;

import New.CustomControls.TimeLine.SelectableSegmentationPane;
import New.Observables.ObservableSegmentation;
import javafx.beans.binding.BooleanBinding;

public class SelectableSegmentationController {

    ObservableSegmentation observableSegmentation;

    public SelectableSegmentationController(ObservableSegmentation observableSegmentation){ this.observableSegmentation = observableSegmentation; }

    /**
     * Sets the given SelectableSegmentationPane as the selected and active segmentation
     * @param segmentationPane segmentationPane which should become the active segmentation
     */
    public void selectSegmentation(SelectableSegmentationPane segmentationPane){
        observableSegmentation.setSelectedTimeLine(segmentationPane);
    }

    /**
     * Returns a booleanBinding which says whether or not the selectedTimeLineProperty of the
     * ObservableSegmentation is null (no segmentation selected)
     * @return the BooleanBinding described above
     */
    public BooleanBinding getSegmentationIsNullProperty(){
        return observableSegmentation.getSelectedSegmentationProperty().isNull();
    }

}
