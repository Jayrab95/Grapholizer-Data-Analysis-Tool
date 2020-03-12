package New.Controllers;

import New.CustomControls.TimeLine.SelectableSegmentationPane;
import New.Observables.ObservableSegmentation;
import javafx.beans.binding.BooleanBinding;

public class SelectableTimeLineController {

    ObservableSegmentation timeLine;

    public SelectableTimeLineController(ObservableSegmentation timeLine){ this.timeLine = timeLine; }

    public void selectTimeLine(SelectableSegmentationPane timeLinePane){
        timeLine.setSelectedTimeLine(timeLinePane);
    }

    public BooleanBinding getSegmentationIsNullProperty(){
        return timeLine.getSelectedTimeLineProperty().isNull();
    }

}
