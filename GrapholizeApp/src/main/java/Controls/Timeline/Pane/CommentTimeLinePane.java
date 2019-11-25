package Controls.Timeline.Pane;

import Controls.TimelineElement.CommentTimeLineElement;
import javafx.geometry.Point2D;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TextInputDialog;
import javafx.scene.effect.Light;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Optional;

public class CommentTimeLinePane extends TimeLinePane {

    private String comment;
    private Color color;
    Light.Point anchor;
    Rectangle selection;

    public CommentTimeLinePane(String timeLineName, double width, double height, double scale, Color c) {
        super(timeLineName, width, height, scale);
        this.color = c;
        setOnMousePressed(e-> handleMousePress(e));
        setOnMouseDragged(e-> handleMouseDrag(e));
        setOnMouseReleased(e-> handleMouseRelease(e));
        this.anchor = new Light.Point();
        selection = new Rectangle();
        getChildren().add(selection);
    }


    //Source: https://coderanch.com/t/689100/java/rectangle-dragging-image
    //TODO: Check for surrounding comments. the selection can only be done between 2 elements.
    //TODO: Currently, the mousepress of the Timeline overrides the mouseclick of the TLE.
    //Perhaps check if the mouse is currently over an element.
    private void handleMousePress(MouseEvent event){
        //TODO: Check if a comment was clicked.
        //If an element was clicked => set Selected Element. The element can now be dragged.
        //Reset selection
        System.out.println("HandleMousePress in CommentTimeLinePane");
        selection.setWidth(0);
        selection.setHeight(getHeight());

        anchor.setX(event.getX());
        //anchor.setY(event.getY());
        anchor.setY(0);

        selection.setX(event.getX());
        //selection.setY(event.getY());
        selection.setY(0);

        selection.setFill(null); // transparent
        selection.setStroke(Color.BLACK); // border
        selection.getStrokeDashArray().add(10.0);

    }

    private void handleMouseDrag(MouseEvent event){
        selection.setWidth(Math.abs(event.getX() - anchor.getX()));
        //selection.setHeight(Math.abs(event.getY() - anchor.getY()));
        selection.setX(Math.min(anchor.getX(), event.getX()));
        //selection.setY(Math.min(anchor.getY(), event.getY()));
        selection.setY(0);
    }

    private void handleMouseRelease(MouseEvent e){
        //TODO: Check if width of selection is larger than 0-5

        // Do what you want with selection's properties here
        System.out.printf("X: %.2f, Y: %.2f, Width: %.2f, Height: %.2f%n",
                selection.getX(), selection.getY(), selection.getWidth(), selection.getHeight());
        if(selection.getWidth() > 0){
            Optional<String> s = openCommentCreationDialogue();
            if(s.isPresent()){
                System.out.println(s.get());
                CommentTimeLineElement ctle = new CommentTimeLineElement(color, selection);
                ctle.setComment(s.get());
                getChildren().add(ctle);
            }
            else{
                System.out.println("Comment creation aborted.");
            }
        }

        //Reset SelectionRectangle
        selection.setWidth(0);
        selection.setHeight(0);
    }

    //https://code.makery.ch/blog/javafx-dialogs-official/
    private Optional<String> openCommentCreationDialogue(){
        TextInputDialog dialog = new TextInputDialog("Your comment");
        dialog.setTitle("Create new Comment");
        dialog.setHeaderText("Enter the next for your new comment.");
        dialog.setContentText("Comment:");
        return dialog.showAndWait();
    }

}
