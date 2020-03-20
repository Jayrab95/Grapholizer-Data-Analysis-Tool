package New.CustomControls.SegmentationPanes.DetailSegmentations;

import New.CustomControls.SegmentationPanes.SegmentationPane;
import New.Observables.ObservablePage;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;

/**
 * Base class for DetailSegmentations
 * The DetailSegmentationClass contains an abstract setUp method, which is responsible for creating
 * the segments that need to be displayed.
 * Note that each child is themselves responsible for calling the setUp method during their construction.
 */
public abstract class DetailSegmentation extends SegmentationPane {
    protected ObservablePage page;
    public DetailSegmentation(double totalLength, double height
            , DoubleProperty scaleProp, StringProperty name
            , ObservablePage p, String topicSetID) {
        super(totalLength, height, scaleProp, name, topicSetID);
        this.page = p;
        //Because some detail segmentations receive additional attributes in the constructor,
        //the setup method cannot be called in the base class. Each child is responsible for
        //calling the setUp method.
    }

    /**
     * Sets up the segmentation. Note that this method needs to be overriden by the children
     * and should be called in the constructor, once any additional attributes have been
     * initialized
     */
    protected abstract void setUp();
}
