package Controls.Timeline;

import Controls.TimelineElement.TimeLineElement;
import javafx.scene.layout.HBox;

public abstract class TimeLine extends HBox {
    protected String timeLineName;

    //Probably not necessary?
    public void add(TimeLineElement elem){
        getChildren().add(elem);
    }

}
