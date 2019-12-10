package Controllers;

import Controls.TimelineElement.TimeLineElementRect;
import Model.Entities.TimeLineElement;

public class TimeLineElementController {
    private TimeLineElement timeLineElement;
    public TimeLineElementController(TimeLineElement timeLineElement){
        this.timeLineElement = timeLineElement;
    }

    public TimeLineElement getTimeLineElement() {
        return timeLineElement;
    }
}
