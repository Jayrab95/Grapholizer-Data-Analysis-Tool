package Controls.Timeline.Canvas;

import Controls.TimelineElement.CommentTimeLineElement;
import Controls.TimelineElement.TimeLineElement;
import javafx.geometry.Point2D;
import javafx.scene.control.ContextMenu;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class CommentTimeLineCanvas extends TimelineCanvas<CommentTimeLineElement>{

    private String tag;

    private Point2D mouseStart;
    private double xAnchor;
    private Rectangle selection;
    private CommentTimeLineElement selectedElement;
    private Color timeLineColor;

    public CommentTimeLineCanvas(String timeLineName, double width, double height, double scale, Color tlColor) {
        super(timeLineName, width, height, scale);
        this.timeLineColor=tlColor;
        setOnMouseDragged(e -> handleMouseDragMove(e));
        setOnMouseReleased(e -> handleMouseRelease(e));
    }

    //How to detect rightlicks and doubleclicks: https://stackoverflow.com/questions/10949461/javafx-2-click-and-double-click
    //Show contextMenu/Info on right click.
    //Difference MouseClick vs MousePress: Click = Press+Release. Press = only press.
    @Override
    protected void handleMouseClick(MouseEvent e) {
        //Click only does selection of an element.
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

    private void handleMousePress(MouseEvent e){
        //selectionRect = new Rectangle();
    }

    protected void handleMouseDragMove(MouseEvent e) {
        if(selectedElement != null){
            //move element
        }
        else if(mouseStart != null){
            double distance = Math.abs(e.getX() - mouseStart.getX());
            double x = e.getX() < mouseStart.getX() ? e.getX() : mouseStart.getX();
            getGraphicsContext2D().clearRect(x, 0, distance, getHeight());
            getGraphicsContext2D().setFill(new Color(0,0,0,0.3));
            getGraphicsContext2D().fillRect(x, 0, distance, getHeight());
        }
    }

    protected void handleMouseRelease(MouseEvent e) {
        System.out.println("CommentTimeLine: Released MouseButton");
        selectedElement = null;
        mouseStart = null;
    }

    @Override
    protected void InitiateContextMenu() {
        contextMenu = new ContextMenu();

    }

    private void invalidateCanvas(){
        getGraphicsContext2D().clearRect(0, 0, getWidth(), getHeight());
        draw();
    }
}
