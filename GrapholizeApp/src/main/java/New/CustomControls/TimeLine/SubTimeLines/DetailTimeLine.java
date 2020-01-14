package New.CustomControls.TimeLine.SubTimeLines;

import New.CustomControls.TimeLine.TimeLinePane;
import New.Observables.ObservablePage;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;

public abstract class DetailTimeLine extends TimeLinePane {
    protected ObservablePage page;
    protected String topic;
    public DetailTimeLine(double totalLength, double height, DoubleProperty scaleProp, StringProperty name, ObservablePage p, String topic) {
        super(totalLength, height, scaleProp, name);
        this.page = page;
        this.topic = topic;
        setUp();
    }

    protected abstract void setUp();
}
