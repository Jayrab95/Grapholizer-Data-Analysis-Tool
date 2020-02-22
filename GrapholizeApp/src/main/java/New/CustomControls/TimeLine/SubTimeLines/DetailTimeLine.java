package New.CustomControls.TimeLine.SubTimeLines;

import New.CustomControls.TimeLine.TimeLinePane;
import New.Observables.ObservablePage;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;

public abstract class DetailTimeLine extends TimeLinePane {
    protected ObservablePage page;
    public DetailTimeLine(double totalLength, double height
            , DoubleProperty scaleProp, StringProperty name
            , ObservablePage p, String topicSetID) {
        super(totalLength, height, scaleProp, name, topicSetID);
        this.page = p;
        setUp();
    }

    protected abstract void setUp();
}
