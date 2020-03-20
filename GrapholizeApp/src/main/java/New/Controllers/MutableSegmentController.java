package New.Controllers;

import New.CustomControls.SegmentRectangles.MutableSegmentRectangle;
import New.CustomControls.SegmentationPanes.CustomSegmentationPane;
import New.Observables.ObservableSegment;
import javafx.scene.shape.Rectangle;

import java.util.Map;

public class MutableSegmentController {

    private final ObservableSegment oAnnotation;
    private CustomSegmentationPane parent;


    public MutableSegmentController(ObservableSegment oAnnotation, CustomSegmentationPane parent){
        this.oAnnotation = oAnnotation;
        this.parent = parent;
    }

    /**
     * deletes the given segment rectangle from the parent. Note that this implicitly also deletes the segment from the page
     * @param rect SegmentRectangle (usually owner of this controller) that needs to be deleted.
     */
    public void removeElement(MutableSegmentRectangle rect){
        parent.deleteSegment(rect, oAnnotation);
    }

    /**
     * Retrieves the drag bounds for this segment, determined by the given x position
     * @param xPosition
     * @return a double array with two values, the first being the left bound and the second being the right bound.
     */
    public double[] getBounds(double xPosition){
        return parent.getBounds(xPosition);
    }

    /**
     * Adds the left and right drag handle to the segmentation pane
     * @param left left drag handle
     * @param right right drag handle
     */
    public void enableResize(Rectangle left, Rectangle right){ parent.getChildren().addAll(left, right); }

    /**
     * removes the left and right drag handle from the segmentation pane
     * @param left left drag handle
     * @param right right drag handle
     */
    public void disableResize(Rectangle left, Rectangle right){
        parent.getChildren().removeAll(left, right);
    }

    /**
     * Checks if the segment fits the filter criteria
     * @param topicFilters map containing the filter criteria where the key is the topic id and the value is the filter.
     * @return true if the segment fits the criteria, false if it does not.
     */
    public boolean fitsFilterCriteria(Map<String, String> topicFilters){
        return oAnnotation.fitsFilterCriteria(topicFilters);
    }

}
