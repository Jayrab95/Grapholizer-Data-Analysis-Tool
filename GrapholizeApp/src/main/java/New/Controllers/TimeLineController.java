package New.Controllers;

import New.CustomControls.TimeLineContainer;
import New.Model.Entities.Annotation;
import New.Model.Entities.SimpleColor;
import New.Model.Entities.TimeLineTag;
import New.Model.ObservableModel.ObservableActiveState;

public class TimeLineController {

    private ObservableActiveState state;
    private TimeLineTag timeLineTag;
    private TimeLineContainer parent;

    public TimeLineController(ObservableActiveState state, TimeLineTag timeLineTag, TimeLineContainer parent) {
        this.state = state;
        this.timeLineTag = timeLineTag;
        this.parent = parent;
    }

    public TimeLineTag getTimeLineTag(){return timeLineTag;}

    public void addAnnotation(Annotation a){
        state.getActivePage().getTimeLine(timeLineTag).add(a);
    }

    public boolean editTimeLine(String newTimeLineName, SimpleColor newSimpleColor){
        return state.getActiveProject().editTimeLineTag(timeLineTag.getTag(), newTimeLineName, newSimpleColor);
    }

    public TimeLineTag removeTimeLine(){
        return state.getActiveProject().removeTimeLineTag(timeLineTag.getTag());
    }

    /**
     * Checks if the given Annotation collides with any other Annotation elements in this timeline.
     * @param annotation The Annotation which needs to be checked for collision with existing elements.
     * @return true if the given Annotation tle collides with any other elements and false if there are no collisions.
     */
    public boolean collidesWithOtherElements(Annotation annotation){
        return state.getActivePage().getTimeLines().get(timeLineTag.getTag()).stream()
                .filter(element -> element != annotation && annotation.collidesWith(element.getTimeStart(), element.getTimeStop()))
                .count() > 0;
    }

}
