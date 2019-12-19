package New.Controllers;

import New.CustomControls.TimeLine.SelectableTimeLinePane;
import New.Observables.ObservableTimeLine;

public class SelectableTimeLineController {

    ObservableTimeLine timeLine;

    public SelectableTimeLineController(ObservableTimeLine timeLine){
        this.timeLine = timeLine;
    }

    public void selectTimeLine(SelectableTimeLinePane timeLinePane){
        timeLine.setSelectedTimeLine(timeLinePane);
    }
}
