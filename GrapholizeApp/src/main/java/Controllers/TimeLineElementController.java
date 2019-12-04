package Controllers;

import Controls.TimelineElement.TimeLineElementRect;
import Model.Entities.TimeLineElement;

public class TimeLineElementController {
    TimeLineElement timeLineElement;
    public TimeLineElementController(TimeLineElement timeLineElement){
        this.timeLineElement = timeLineElement;
    }

    public TimeLineElement getTimeLineElement() {
        return timeLineElement;
    }
}
