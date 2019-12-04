package Controls.Timeline.Pane;

import javafx.beans.property.DoubleProperty;
import javafx.scene.paint.Color;

/**
 * A SubTimeLinePane is a TimeLinePane which is automatically generated for each timeline.
 * The SubTimeLinePane is a simple extension from the TimeLinePane that also receives a reference to its
 * parent timeline upon creation.
 */
public class SubTimeLinePane extends TimeLinePane {

    //TODO: Does the SubTimeLinePane really need a link to the Timeline or is a Link to a list of TimeLineElements enough?
    protected TimeLinePane parentTimeLine;

    protected SubTimeLinePane(String timeLineName, double width, double height, DoubleProperty scale, Color c, TimeLinePane parentTimeLine) {
        super(timeLineName, width, height, scale, c);
        this.parentTimeLine = parentTimeLine;
    }


}
