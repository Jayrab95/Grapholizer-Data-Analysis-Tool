package New.CustomControls.TimeLine.SubTimeLines;

import New.CustomControls.TimeLine.TimeLinePane;
import New.Model.ObservableModel.ObservablePage;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;

public class PressureTimeLine extends TimeLinePane {

    ObservablePage p;

    protected PressureTimeLine(double totalLength, double height, DoubleProperty scaleProp, StringProperty name, ObservablePage p) {
        super(totalLength, height, scaleProp, name);
    }

    


}
