package New.Controllers;

import New.CustomControls.TimeLine.SelectableTimeLinePane;
import New.Observables.ObservableSegmentation;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;

public class SelectableTimeLineController {

    ObservableSegmentation timeLine;

    public SelectableTimeLineController(ObservableSegmentation timeLine){ this.timeLine = timeLine; }

    public void selectTimeLine(SelectableTimeLinePane timeLinePane){
        timeLine.setSelectedTimeLine(timeLinePane);
    }

    public BooleanBinding getSegmentationIsNullProperty(){
        return timeLine.getSelectedTimeLineProperty().isNull();
    }

}
