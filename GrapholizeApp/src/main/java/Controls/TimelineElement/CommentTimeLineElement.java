package Controls.TimelineElement;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class CommentTimeLineElement extends TimeLineElement {

    private String comment;

    public CommentTimeLineElement(double tStart, double tEnd, double parentHeight, Color c) {
        super(tStart, tEnd, parentHeight, c);
    }

    public String getComment(){
        return comment;
    }

    public void setComment(String newComment){
        this.comment = newComment;
    }

    @Override
    protected void handleMouseRelease(MouseEvent e) {
        //Todo: remove mouse handling once finished migrating to new canvasbased timeline.
    }
}
