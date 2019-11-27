package Controls.Timeline.Pane;

import Controls.TimelineElement.StrokeTimeLineElement;
import Controls.TimelineElement.TimeLineElement;
import javafx.scene.control.*;
import javafx.scene.effect.Light;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import util.DialogGenerator;

import java.util.Optional;

public class CommentTimeLinePane extends TimeLinePane {

    Light.Point anchor;
    Rectangle selection;

    public CommentTimeLinePane(String timeLineName, double width, double height, double scale, Color c) {
        super(timeLineName, width, height, scale, c);
        setOnMousePressed(e-> handleMousePress(e));
        setOnMouseDragged(e-> handleMouseDrag(e));
        setOnMouseReleased(e-> handleMouseRelease(e));
        this.anchor = new Light.Point();
        selection = new Rectangle();
        getChildren().add(selection);
    }

    @Override
    public void addTimeLineElement(TimeLineElement tle){
        super.addTimeLineElement(tle);
        //Assign contextmenu as ContextMenuRequest action
        tle.setOnContextMenuRequested(event -> {
            getElementSpecificContextMenu(tle).show(this, event.getScreenX(), event.getScreenY());
            event.consume(); //Consume event so that the context menu of the Timelinepane doesn't also show up.
        });
    }

    private ContextMenu getElementSpecificContextMenu(TimeLineElement tle){
        MenuItem menuItem_EditTLE = new MenuItem("Edit annotation");
        menuItem_EditTLE.setOnAction(event -> handleEditTimeLineElementClick(tle));


        MenuItem menuItem_DeleteTLE = new MenuItem("Delete annotation");
        menuItem_DeleteTLE.setOnAction(event -> handleDeleteTimeLineElementClick(tle));

        return new ContextMenu(menuItem_EditTLE, menuItem_DeleteTLE);
    }

    //region handlerMethods
    private void handleEditTimeLineElementClick(TimeLineElement tle){
        //OPen dialogue. if successful => Edit.
        Optional<String> newAnnotationText = DialogGenerator.simpleTextInputDialog(
                tle.getAnnotationText(),
                "Edit annotation",
                "Edit the text of your annotation, then click on OK to apply the changes.",
                tle.getAnnotationText()
        );
        if(newAnnotationText.isPresent()){
            tle.setAnnotationText(newAnnotationText.get());
        }
    }

    private void handleDeleteTimeLineElementClick(TimeLineElement tle){
        if(DialogGenerator.confirmationDialogue(
                "Delete annotation",
                "Delete annotation?",
                "Are you sure you want to delete the annotation \"" + tle.getAnnotationText() + "\"? This action cannot be undone."
        )){
            getChildren().remove(tle);
        }
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
        System.out.printf("X: %.2f, Y: %.2f, Width: %.2f, Height: %.2f%n",
                selection.getX(), selection.getY(), selection.getWidth(), selection.getHeight());
        if(selection.getWidth() > 0){
            Optional<String> s = DialogGenerator.simpleTextInputDialog(
                    "New annotation",
                    "Create new annotation",
                    "Enter a text for your annotation. (The annotation text can also be empty).",
                    "Annotation text");
            if(s.isPresent()){
                System.out.println(s.get());
                TimeLineElement tle = new TimeLineElement(timeLineColor, selection, s.get());
                addTimeLineElement(tle);
            }
            else{
                System.out.println("Annotation creation aborted.");
            }
        }

        //Reset SelectionRectangle
        selection.setWidth(0);
        selection.setHeight(0);
    }
//endregion

}
