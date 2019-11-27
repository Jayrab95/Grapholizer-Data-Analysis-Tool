package Controls.Timeline.Pane;

import Controls.TimelineElement.StrokeTimeLineElement;
import Controls.TimelineElement.TimeLineElement;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.Light;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import util.DialogGenerator;

import java.util.Optional;

public class CommentTimeLinePane extends TimeLinePane {

    private Light.Point anchor;
    private Rectangle selection;
    private double[] dragBounds;


    public CommentTimeLinePane(String timeLineName, double width, double height, double scale, Color c) {
        super(timeLineName, width, height, scale, c);
        setOnMousePressed(e-> handleMousePress(e));
        setOnMouseDragged(e-> handleMouseDrag(e));
        setOnMouseReleased(e-> handleMouseRelease(e));
        this.anchor = new Light.Point();
        selection = new Rectangle();
        dragBounds = new double[]{0, getWidth()};
        //getChildren().add(selection);
    }

    /**
     * Adds a new TimeLineElement to this TimeLine's children.
     * In addition to the baseclass's addTimeLineElement, this method also binds the element specific ContextMenu
     * to the elements via setOnContextMenuRequested.
     * @param tle The TimeLineElement that should be added.
     */
    @Override
    public void addTimeLineElement(TimeLineElement tle){
        super.addTimeLineElement(tle);
        //Assign contextmenu as ContextMenuRequest action
        tle.setOnContextMenuRequested(event -> {
            getElementSpecificContextMenu(tle).show(this, event.getScreenX(), event.getScreenY());
            event.consume(); //Consume event so that the context menu of the Timelinepane doesn't also show up.
        });
    }

    /**
     * Checks if the given TimeLineElement collides with any other Timeline elements in this timeline.
     * @param tle The TimelineElement which needs to be checked for collision with existing elements.
     * @return true if the given TimeLineElement tle collides with any other elements and false if there are no collisions.
     */
    public boolean collidesWithOtherElements(TimeLineElement tle){
        return getChildren().stream()
                .map(node -> (TimeLineElement)node)
                .filter(element -> element != tle && tle.collidesWith(element))
                .count() > 0;
    }

    //TODO: Perhaps think of better solution. Maybe having a linkedlist that manages the TLEs could be better.
    private void setBounds(double xPosition){
        double lowerBounds = 0;
        double upperBounds = getWidth();
        for(Node n : getChildren()){
            double nTimeStart = ((TimeLineElement)n).getTimeStart();
            double nTimeStop = ((TimeLineElement)n).getTimeStop();
            if(nTimeStop < xPosition && nTimeStop > lowerBounds){
                lowerBounds = nTimeStop;
            }
            if(nTimeStart > xPosition && nTimeStart < upperBounds){
                upperBounds = nTimeStart;
            }
        }
        dragBounds[0] = lowerBounds;
        dragBounds[1] = upperBounds;
    }

    /**
     * Generates a ContextMenu for a TimeLineElement on demand.
     * The ContextMenu currently has tow selectable MenuItems: "Edit element" and "delete element".
     * The ContextMenu is bound to the TimeLineElement via setOnContextMenuRequested during creation (AddTimeLineElement override)
     * @param tle The TimeLineElement for which the ContextMenu and its operations should be generated.
     * @return The generated ContextMenu
     */
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

        setBounds(event.getX());

        //Prepare selection
        getChildren().add(selection);
        selection.setWidth(0);
        selection.setHeight(getHeight());

        anchor.setX(event.getX());
        anchor.setY(0);

        selection.setX(event.getX());
        selection.setY(0);

        selection.setFill(null); // transparent
        selection.setStroke(Color.BLACK); // border
        selection.getStrokeDashArray().add(10.0);

    }

    private void handleMouseDrag(MouseEvent event){
        if(event.getX() > dragBounds[0] && event.getX() < dragBounds[1]){
            selection.setWidth(Math.abs(event.getX() - anchor.getX()));
            selection.setX(Math.min(anchor.getX(), event.getX()));
        }
        else{ //Out of bounds (Collision with other element or timeline border)
            double temporaryOutOfboundsPos = (event.getX() < dragBounds[0] ? dragBounds[0] : dragBounds[1]) + 0.1;
            selection.setWidth(Math.abs(temporaryOutOfboundsPos - anchor.getX()));
            selection.setX(Math.min(anchor.getX(), temporaryOutOfboundsPos));
        }
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
        getChildren().remove(selection);
    }
//endregion

}
