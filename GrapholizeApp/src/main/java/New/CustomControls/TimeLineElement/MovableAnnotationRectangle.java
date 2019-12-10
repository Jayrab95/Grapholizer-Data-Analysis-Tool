package New.CustomControls.TimeLineElement;


import New.CustomControls.TimeLine.TimeLinePane;
import New.Model.Entities.Annotation;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import util.DialogGenerator;

import java.util.Optional;

public class MovableAnnotationRectangle extends AnnotationRectangle {

    private double[] dragBounds;
    private double mouseDelta;

    public MovableAnnotationRectangle(ObjectProperty<Color> c, DoubleProperty scale, Annotation t, TimeLinePane parent) {
        super(c, scale, t, parent);

        setOnMousePressed(e-> handleMousePress(e));
        setOnMouseDragged(e-> handleMouseDrag(e));
        setOnMouseReleased(e-> handleMouseRelease(e));
    }


    private void move(double newTimeStart){
        double delta = (newTimeStart - getX()) / scale.get();
        this.setX(newTimeStart);
        annotationController.moveElement(delta);
    }

    /**
     * Generates a ContextMenu for a TimeLineElement on demand.
     * The ContextMenu currently has tow selectable MenuItems: "Edit element" and "delete element".
     * The ContextMenu is bound to the TimeLineElement via setOnContextMenuRequested during creation (AddTimeLineElement override)
     * @return The generated ContextMenu
     */
    private ContextMenu getElementSpecificContextMenu(){
        MenuItem menuItem_EditTLE = new MenuItem("Edit annotation");
        menuItem_EditTLE.setOnAction(event -> handleEditTimeLineElementClick());


        MenuItem menuItem_DeleteTLE = new MenuItem("Delete annotation");
        menuItem_DeleteTLE.setOnAction(event -> handleDeleteTimeLineElementClick());

        return new ContextMenu(menuItem_EditTLE, menuItem_DeleteTLE);
    }

    private void openEditDialog(){
        Optional<String> newAnnotationText = DialogGenerator.simpleTextInputDialog(
                viewAnnotationTextProp.get(),
                "Edit annotation",
                "Edit the text of your annotation, then click on OK to apply the changes.",
                viewAnnotationTextProp.get()
        );
        if(newAnnotationText.isPresent()){
            annotationController.editElement(newAnnotationText.get());
        }
    }

    private void handleEditTimeLineElementClick(){
        openEditDialog();
    }

    private void handleDeleteTimeLineElementClick(){
        if(DialogGenerator.confirmationDialogue(
                "Delete annotation",
                "Delete annotation?",
                "Are you sure you want to delete the annotation \"" + viewAnnotationTextProp.get() + "\"? This action cannot be undone."))
        {
            annotationController.removeElement();
        }
    }

    //region TimeLineElement handlers

    private void handleMousePress(MouseEvent event){
        mouseDelta = event.getX() - getX();
        dragBounds = annotationController.getBounds(event.getX());
        event.consume();
    }

    private void handleMouseDrag(MouseEvent event){
        double newPosition = event.getX() - mouseDelta;
        if(newPosition > dragBounds[0] && newPosition + getWidth() < dragBounds[1]){
            move(newPosition);
        }
        else{
            double temporaryOutOfBoundsPos = newPosition < dragBounds[0] ? dragBounds[0] + 0.1 : dragBounds[1] - getWidth() - 0.1;
            move(temporaryOutOfBoundsPos);
        }
        event.consume();
    }

    private void handleMouseRelease(MouseEvent event){
        event.consume();
    }
    //endregion

}
