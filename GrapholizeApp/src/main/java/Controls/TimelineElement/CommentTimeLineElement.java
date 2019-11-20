package Controls.TimelineElement;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class CommentTimeLineElement extends TimeLineElement {

    private String comment;

    public CommentTimeLineElement(double tStart, double tEnd, double parentHeight, Color c) {
        super(tStart, tEnd, parentHeight, c);
    }
    public CommentTimeLineElement(double tStart, double tEnd, double parentHeight, Color c, String comment) {
        super(tStart, tEnd, parentHeight, c);
        this.comment = comment;
    }

    //TODO: Make the Rectangle-base constructor call the internal constructor.
    public CommentTimeLineElement(Color c, Rectangle r){
        super(c, r);
    }
    public CommentTimeLineElement(Color c, Rectangle r, String comment){
        super(c, r);
        this.comment = comment;
    }

    @Override
    protected void handleMouseClick(MouseEvent e) {
        System.out.println("Comment: " + comment);
    }

    public String getComment(){
        return comment;
    }

    public void setComment(String newComment){
        this.comment = newComment;
    }

    public void move(double dx, double dy){
        setX(getX() + dx);
        setX(getY() + dx);
    }


}
