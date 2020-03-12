package New.CustomControls.TimeLine.SubTimeLines;

import New.CustomControls.TimeLine.SegmentationPane;
import New.Observables.ObservablePage;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;

public abstract class DetailSegmentation extends SegmentationPane {
    protected ObservablePage page;
    public DetailSegmentation(double totalLength, double height
            , DoubleProperty scaleProp, StringProperty name
            , ObservablePage p, String topicSetID) {
        super(totalLength, height, scaleProp, name, topicSetID);
        this.page = p;
    }

    protected abstract void setUp();
}
