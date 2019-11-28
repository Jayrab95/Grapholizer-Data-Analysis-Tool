package Controls.Timeline.Pane;

import Controls.TimelineElement.TimeLineElementRect;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.Light;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import util.DialogGenerator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CommentTimeLinePane extends TimeLinePane {

    private Light.Point anchor;
    private Rectangle selection;
    private double[] dragBounds;
    private double mouseDelta;

    private Tooltip tooltip;

    private TimeLineElementRect debugElement;
    private double debugX;
    private double debugY;


    public CommentTimeLinePane(String timeLineName, double width, double height, double scale, Color c) {
        super(timeLineName, width, height, scale, c);

        this.anchor = new Light.Point();
        selection = new Rectangle();
        tooltip = new Tooltip();
        tooltip.setHideDelay(Duration.seconds(1));
        dragBounds = new double[]{0, getWidth()};

        setOnMousePressed(e-> handleTimelineMousePress(e));
        setOnMouseDragged(e-> handleTimelineMouseDrag(e));
        setOnMouseReleased(e-> handleTimelineMouseRelease(e));
        setOnMouseMoved(e -> getToolTip(e));
    }

    /**
     * Adds a new TimeLineElement to this TimeLine's children.
     * In addition to the baseclass's addTimeLineElement, this method also binds the element specific ContextMenu
     * to the elements via setOnContextMenuRequested.
     * @param tle The TimeLineElement that should be added.
     */
    @Override
    public void addTimeLineElement(TimeLineElementRect tle){
        super.addTimeLineElement(tle);
        //Assign contextmenu as ContextMenuRequest action
        tle.setOnContextMenuRequested(event -> {
            getElementSpecificContextMenu(tle).show(this, event.getScreenX(), event.getScreenY());
            event.consume(); //Consume event so that the context menu of the Timelinepane doesn't also show up.
        });
        tle.setOnMousePressed(e -> handleTimeLineElementMousePress(e, tle));
        tle.setOnMouseDragged(e -> handleTimeLineElementMouseDrag(e, tle));
        tle.setOnMouseReleased(e -> handleTimeLineElementMouseRelease(e, tle));
        tle.setOnMouseClicked(e -> handleTimeLineElementDoubleClick(e,tle));
    }

    /**
     * Checks if the given TimeLineElement collides with any other Timeline elements in this timeline.
     * @param tle The TimelineElement which needs to be checked for collision with existing elements.
     * @return true if the given TimeLineElement tle collides with any other elements and false if there are no collisions.
     */
    public boolean collidesWithOtherElements(TimeLineElementRect tle){
        List<TimeLineElementRect> debug = getChildren().stream()
                .map(node -> (TimeLineElementRect)node)
                .filter(element -> element != tle && tle.collidesWith(element)).collect(Collectors.toList());
        return getChildren().stream()
                .map(node -> (TimeLineElementRect)node)
                .filter(element -> element != tle && tle.collidesWith(element))
                .count() > 0;
    }

    //TODO: Perhaps think of better solution. Maybe having a linkedlist that manages the TLEs could be better.
    private void setBounds(double xPosition){
        double lowerBounds = 0;
        double upperBounds = getWidth();
        for(Node n : getChildren()){
            double nTimeStart = ((TimeLineElementRect)n).getTimeStart();
            double nTimeStop = ((TimeLineElementRect)n).getTimeStop();
            if(nTimeStop < xPosition && nTimeStop > lowerBounds){
                System.out.println("Lowerbounds before update: " + lowerBounds);
                lowerBounds = nTimeStop;
                System.out.println("Updating lowerBounds. nTimeStop = " + nTimeStop + " . xPosition = " + xPosition + ". lowerBounds = " + lowerBounds);
            }
            if(nTimeStart > xPosition && nTimeStart < upperBounds){
                System.out.println("Upperbounds before update: " + upperBounds);
                upperBounds = nTimeStart;
                System.out.println("Updating upperbounds. nTimeStop = " + nTimeStart + " . xPosition = " + xPosition + ". lowerBounds = " + upperBounds);
            }
        }
        dragBounds[0] = lowerBounds;
        dragBounds[1] = upperBounds;
    }

    //TODO: Behavior is a bit buggy. Perhaps fix the position.
    private void getToolTip(MouseEvent e){
        //tooltip.hide();
        for(Node n : getChildren()){
            TimeLineElementRect tle = (TimeLineElementRect)n;
            if(e.getX() > tle.getTimeStart() && e.getX() < tle.getTimeStop()){
                tooltip.setText(tle.getAnnotationText());
                tooltip.show(this, e.getScreenX() + 10, e.getScreenY() + 10);
            }
        }
    }

    /**
     * Generates a ContextMenu for a TimeLineElement on demand.
     * The ContextMenu currently has tow selectable MenuItems: "Edit element" and "delete element".
     * The ContextMenu is bound to the TimeLineElement via setOnContextMenuRequested during creation (AddTimeLineElement override)
     * @param tle The TimeLineElement for which the ContextMenu and its operations should be generated.
     * @return The generated ContextMenu
     */
    private ContextMenu getElementSpecificContextMenu(TimeLineElementRect tle){
        MenuItem menuItem_EditTLE = new MenuItem("Edit annotation");
        menuItem_EditTLE.setOnAction(event -> handleEditTimeLineElementClick(tle));


        MenuItem menuItem_DeleteTLE = new MenuItem("Delete annotation");
        menuItem_DeleteTLE.setOnAction(event -> handleDeleteTimeLineElementClick(tle));

        return new ContextMenu(menuItem_EditTLE, menuItem_DeleteTLE);
    }

    //region handlerMethods
    //region TimeLine handlers
    private void handleEditTimeLineElementClick(TimeLineElementRect tle){
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

    private void handleDeleteTimeLineElementClick(TimeLineElementRect tle){
        if(DialogGenerator.confirmationDialogue(
                "Delete annotation",
                "Delete annotation?",
                "Are you sure you want to delete the annotation \"" + tle.getAnnotationText() + "\"? This action cannot be undone."))
        {
            getChildren().remove(tle);
        }
    }

    //Source: https://coderanch.com/t/689100/java/rectangle-dragging-image
    private void handleTimelineMousePress(MouseEvent event){
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

    private void handleTimelineMouseDrag(MouseEvent event){
        if(event.getX() > dragBounds[0] && event.getX() < dragBounds[1]){
            selection.setWidth(Math.abs(event.getX() - anchor.getX()));
            selection.setX(Math.min(anchor.getX(), event.getX()));
        }
        else{ //Out of bounds (Collision with other element or timeline border)
            double temporaryOutOfBoundsPos = (event.getX() < dragBounds[0] ? dragBounds[0] : dragBounds[1]) + 0.1;
            selection.setWidth(Math.abs(temporaryOutOfBoundsPos - anchor.getX()));
            selection.setX(Math.min(anchor.getX(), temporaryOutOfBoundsPos));
        }
    }

    private void handleTimelineMouseRelease(MouseEvent e){
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
                TimeLineElementRect tle = new TimeLineElementRect(timeLineColor, selection, s.get());
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

    //region TimeLineElement handlers
    //Because the TimeLineElement drags are dependant on the bounds which require the entire collection of elements,
    //the handle functions are also defined on the timeline class.

    private void handleTimeLineElementMousePress(MouseEvent event, TimeLineElementRect tle){
        mouseDelta = event.getX() - tle.getX();
        setBounds(event.getX());
        event.consume();
    }

    private void handleTimeLineElementMouseDrag(MouseEvent event, TimeLineElementRect tle){
        double newPosition = event.getX() - mouseDelta;
        if(newPosition > dragBounds[0] && newPosition + tle.getWidth() < dragBounds[1]){
            //tle.setX(newPosition);
            tle.move(newPosition);
        }
        else{
            //Out of bounds (Collision with other element or timeline border)
            double temporaryOutOfBoundsPos = newPosition < dragBounds[0] ? dragBounds[0] + 0.1 : dragBounds[1] - tle.getWidth() - 0.1;
            //tle.setX(temporaryOutOfBoundsPos);
            tle.move(temporaryOutOfBoundsPos);
        }
        event.consume();
    }

    private void handleTimeLineElementMouseRelease(MouseEvent event, TimeLineElementRect tle){
        /*
        tle.setTimeStart(tle.getX());
        tle.setTimeStop(tle.getX() + tle.getWidth());
         */

        event.consume();
    }

    private void handleTimeLineElementDoubleClick(MouseEvent e, TimeLineElementRect tle){
        if(e.getButton().equals(MouseButton.PRIMARY) && e.getClickCount() == 2){
            handleEditTimeLineElementClick(tle);
        }
    }
    //endregion
    //endregion

}
