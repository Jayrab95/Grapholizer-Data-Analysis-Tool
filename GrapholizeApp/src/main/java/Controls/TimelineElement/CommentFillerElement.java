package Controls.TimelineElement;

import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

//This filler element is used on comment timelines.
public class CommentFillerElement extends FillerTimeLineElement {
    public CommentFillerElement(double tStart, double tEnd, double parentHeight, Color c) {
        super(tStart, tEnd, parentHeight, c);
    }

    @Override
    protected void setInitialMousePosition(MouseEvent e){
        super.setInitialMousePosition(e);
        //Reset previous rectangle
    }


    /*
    Idea: Move the event binding to Comment timeline
    => commentTimeLine has a list of elements. It might be easier to split the timeLineElements if done in main class.
    Alternative: CommentFillerElement becomes an observable and propagates the selected rectangle to the timeline.

     */
    @Override
    protected void handleMouseRelease(MouseEvent e) {
        System.out.println("handleMouseRelease in FillerTimeLineElement (CommentFiller)");
    }

}
