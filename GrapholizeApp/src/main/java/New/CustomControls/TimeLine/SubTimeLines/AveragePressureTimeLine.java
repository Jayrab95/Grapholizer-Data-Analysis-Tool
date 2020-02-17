package New.CustomControls.TimeLine.SubTimeLines;

import New.Observables.ObservablePage;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;

public class AveragePressureTimeLine extends DetailTimeLine{

    public AveragePressureTimeLine(double totalLength, double height, DoubleProperty scaleProp, StringProperty name, ObservablePage p, String topicSetID) {
        super(totalLength, height, scaleProp, name, p, topicSetID);
    }

    @Override
    protected void setUp() {

    }
}
