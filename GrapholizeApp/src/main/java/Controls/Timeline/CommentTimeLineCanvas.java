package Controls.Timeline;

import Controls.TimelineElement.CommentTimeLineElement;
import Controls.TimelineElement.TimeLineElement;
import javafx.geometry.Point2D;
import javafx.scene.control.ContextMenu;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class CommentTimeLineCanvas extends TimelineCanvas<CommentTimeLineElement>{
    private String tag;

    private Point2D mouseStart;
    private CommentTimeLineElement selectedElement;
    private Color timeLineColor;

    protected CommentTimeLineCanvas(String timeLineName, double width, double height, Color tlColor) {
        super(timeLineName, width, height);
        this.timeLineColor=tlColor;
    }

    //How to detect rightlicks and doubleclicks: https://stackoverflow.com/questions/10949461/javafx-2-click-and-double-click

    //Show contextMenu/Info on right click.
    @Override
    protected void handleMouseClick(MouseEvent e) {
        double x = e.getX();
        TimeLineElement selected;
        for(CommentTimeLineElement tle : elements){
            if(x >= tle.getTimeStart() && x <= tle.getTimeStop()){
                //Element was found at coordinates.
                selectedElement = tle;
            }
        }
        if(selectedElement == null){
            //Draw selection rectangle
            mouseStart = new Point2D(e.getX(), e.getY());
            getGraphicsContext2D().setFill(new Color(0,0,0,0.3));
            getGraphicsContext2D().fillRect(x, 0, 1, getHeight());
        }
        //If click on comment => Show comment
        //Otherwise, do selection.


    }

    @Override
    protected void handleMouseDragMove(MouseEvent e) {
        if(selectedElement != null){
            //move element
        }
        else if(mouseStart != null){
            double distance = Math.abs(e.getX() - mouseStart.getX());
            getGraphicsContext2D().setFill(new Color(0,0,0,0.3));
            getGraphicsContext2D().fillRect(e.getX() < mouseStart.getX() ? e.getX() : mouseStart.getX(), 0, distance, getHeight());
        }
    }

    @Override
    protected void handleMouseRelease(MouseEvent e) {
        selectedElement = null;
        mouseStart = null;
    }

    @Override
    protected void InitiateContextMenu() {
        contextMenu = new ContextMenu();

    }
}
