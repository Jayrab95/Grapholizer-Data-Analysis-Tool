package New.CustomControls.Annotation;


import New.Controllers.MovableAnnotationController;
import New.CustomControls.TimeLine.CustomTimeLinePane;
import New.Interfaces.Selector;
import New.Model.Entities.Annotation;
import New.Observables.ObservableAnnotation;
import New.util.DialogGenerator;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Optional;

public class MovableAnnotationRectangle extends SelectableAnnotationRectangle {

    private double[] dragBounds;
    private double mouseDelta;
    private MovableAnnotationController movableAnnotationController;

    private DragRectangle left;
    private DragRectangle right;


    public MovableAnnotationRectangle(ObjectProperty<Color> c, DoubleProperty scale, ObservableAnnotation t, CustomTimeLinePane parent, Selector s) {
        super(c, t.getAnnotationTextProperty(), scale, t.getDuration(), parent.getHeight(), t.getTimeStart(), parent, s);

        this.movableAnnotationController = new MovableAnnotationController(t,parent);

        /*
        xProperty().bind(t.getTimeStartProperty().multiply(scale));
        widthProperty().bind(t.getTimeStopProperty().subtract(t.getTimeStart()).multiply(scale));

        t.getTimeStartProperty().bind(this.xProperty().divide(scale));
        t.getTimeStopProperty().bind(this.xProperty().add(this.widthProperty()).divide(scale));

         */

        t.getTimeStartProperty().bind(this.xProperty().divide(scale));
        t.getTimeStopProperty().bind(this.xProperty().add(this.widthProperty()).divide(scale));
        //t.getTimeStartProperty().addListener(observable -> System.out.println("TmeStart changed"));

        /*
        t.getTimeStartProperty().addListener((observable, oldValue, newValue) -> {
            this.setX(newValue.doubleValue() * scale.get());
            this.setWidth((newValue.doubleValue() * scale.get()));
        });
        t.getTimeStopProperty().addListener((observable, oldValue, newValue) -> {

        });

         */


        this.left = new LeftDragRectangle(this);
        this.right = new RightDragRectangle(this);



        selected.addListener((observable, oldValue, newValue) -> {
            if(newValue){
                movableAnnotationController.enableResize(left, right);
            }
            else{
                movableAnnotationController.disableResize(left, right);
            }
        });




        setOnMousePressed(e-> handleMousePress(e));
        setOnMouseDragged(e-> handleMouseDrag(e));
        setOnMouseReleased(e-> handleMouseRelease(e));

        /*
        System.out.println("X:" + getX());
        System.out.println("LayoutX: " + getLayoutX());
        System.out.println("ScaleX: " + getScaleX());
        System.out.println("translateX: " + getTranslateX());

         */
    }


    private void move(double newTimeStart){
        double delta = (newTimeStart - getX()) / scale.get();
        this.setX(newTimeStart);
        //movableAnnotationController.moveElement(delta);
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
                annotationText.get(),
                "Edit annotation",
                "Edit the text of your annotation, then click on OK to apply the changes.",
                annotationText.get()
        );
        if(newAnnotationText.isPresent()){
            movableAnnotationController.editElement(newAnnotationText.get());
        }
    }

    private void handleEditTimeLineElementClick(){
        openEditDialog();
    }

    private void handleDeleteTimeLineElementClick(){
        if(DialogGenerator.confirmationDialogue(
                "Delete annotation",
                "Delete annotation?",
                "Are you sure you want to delete the annotation \"" + annotationText.get() + "\"? This action cannot be undone."))
        {
            movableAnnotationController.removeElement(this);
        }
    }

    //region TimeLineElement handlers

    @Override
    protected void handleMousePress(MouseEvent event){
        super.handleMousePress(event);
        System.out.println("MousePress in Movable");
        if(event.getButton() == MouseButton.SECONDARY){
            getElementSpecificContextMenu().show(this, event.getScreenX(), event.getScreenY());
        }
        else if(left.contains(event.getX(), event.getY())){
            left.handleMousePress(event);
        }
        else if(right.contains(event.getX(), event.getY())){
            right.handleMousePress(event);
        }
        else{
            mouseDelta = event.getX() - getX();
            dragBounds = movableAnnotationController.getBounds(event.getX());
        }
        event.consume();
    }

    private void handleMouseDrag(MouseEvent event){
        if(event.getButton() == MouseButton.SECONDARY){}
        else if(left.contains(event.getX(), event.getY())){
            left.handleMousePress(event);
        }
        else if(right.contains(event.getX(), event.getY())){
            right.handleMousePress(event);
        }
        else{
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
    }

    @Override
    protected void handleMouseRelease(MouseEvent event){
        super.handleMouseRelease(event);
        System.out.println("HandleMouseRelease in Movable");
        //annotationSelectionController.selectOnlyDotsWithinTimeFrame(getTimeStart(), getTimeStop());
        event.consume();
    }
    //endregion

    private abstract class DragRectangle extends Rectangle {

        protected AnnotationRectangle parent;
        protected final double size = 5;
        protected double startX = 0;

        private DragRectangle(AnnotationRectangle parent){
            this.parent = parent;
            setWidth(calculateWidth());
            setHeight(parent.getHeight());
            setFill(Color.WHITESMOKE);
            parent.scale.addListener((observable, oldValue, newValue) -> {
                setWidth(calculateWidth());
            });
            this.setOnMousePressed(this::handleMousePress);
            this.setOnMouseDragged(this::handleMouseDrag);
            this.setOnMouseReleased(this::handleMouseRelease);
        }

        private double calculateWidth(){
            return parent.getWidth() > 20 ? 5 : parent.getWidth() / 4;
        }


        protected abstract void handleMousePress(MouseEvent e);

        protected abstract void handleMouseDrag(MouseEvent e);

        protected abstract void handleMouseRelease(MouseEvent e);
    }

    private class LeftDragRectangle extends DragRectangle{

        private LeftDragRectangle(AnnotationRectangle parent) {
            super(parent);
            this.xProperty().bind(parent.xProperty());
        }

        @Override
        protected void handleMousePress(MouseEvent e) {
            startX = e.getSceneX();
            dragBounds = movableAnnotationController.getBounds(getX());
            dragBounds[1] = parent.getX() + parent.getWidth() - 1;
            e.consume();
        }

        @Override
        protected void handleMouseDrag(MouseEvent e) {
            double timeStop = parent.getTimeStop();
            double traveledDistance = startX - (e.getSceneX());
            if(e.getSceneX() < startX){
                //traveldirection: left. traveledDistance is positive
                if((parent.getX() - traveledDistance) > dragBounds[0]){
                    parent.setX(parent.getX() - traveledDistance);
                    parent.setWidth(parent.getWidth() + traveledDistance);
                }
                else{
                    parent.setX((dragBounds[0]/scale.get()));
                }
            }
            else{
                //traveldirection: right. traveled distance is negative
                if((parent.getX() - traveledDistance) < dragBounds[1]){
                    parent.setX(parent.getX() - traveledDistance);
                    parent.setWidth(parent.getWidth() + traveledDistance);
                }
                else{
                    parent.setX(dragBounds[1]);
                    parent.setWidth(5);
                }
            }
            startX = e.getScreenX();

        }

        @Override
        protected void handleMouseRelease(MouseEvent e) {
            start=parent.getTimeStart();
            duration = parent.getTimeStop() - parent.getTimeStart();
            annotationSelectionController.selectOnlyDotsWithinTimeFrame(parent.getTimeStart(), parent.getTimeStop());
            e.consume();
        }
    }

    private class RightDragRectangle extends DragRectangle{

        private RightDragRectangle(AnnotationRectangle parent) {
            super(parent);
            this.xProperty().bind(parent.xProperty().add(parent.widthProperty()));
        }


        @Override
        protected void handleMousePress(MouseEvent e) {
            startX = e.getSceneX();
            dragBounds = movableAnnotationController.getBounds(parent.getX() + parent.getWidth());
            dragBounds[0] = parent.getX() + 0.5;
            e.consume();
        }

        @Override
        protected void handleMouseDrag(MouseEvent e) {
            double traveledDistance = startX - e.getSceneX();
            double newPos = 0;
            if(e.getSceneX() < startX){
                //traveldirection: left (travelDistance is positive)
                if(((parent.getX() + parent.getWidth()) - traveledDistance) > dragBounds[0]){
                    parent.setWidth(parent.getWidth() - traveledDistance);
                    //movableAnnotationController.adjustTimeStop(getTimeStart() - (traveledDistance / scale.get()));
                }
                else{
                    parent.setWidth(5);
                    //movableAnnotationController.adjustTimeStop((dragBounds[0]/scale.get()) + 0.5);
                }
            }
            else{
                //traveldirection: right, travelDistance is negative
                if((parent.getX() + parent.getWidth() + traveledDistance) < dragBounds[1]){
                    parent.setWidth(parent.getWidth() - traveledDistance);
                    //movableAnnotationController.adjustTimeStop(getTimeStop() - traveledDistance);
                }
                else{
                    parent.setWidth(dragBounds[1] - parent.getX());
                    //movableAnnotationController.adjustTimeStop((dragBounds[1] / scale.get()) - 0.5);
                }


            }
            startX = e.getSceneX();
            //setX(e.getSceneX());

        }

        @Override
        protected void handleMouseRelease(MouseEvent e) {
            start=parent.getTimeStart();
            duration = parent.getTimeStop() - parent.getTimeStart();
            annotationSelectionController.selectOnlyDotsWithinTimeFrame(parent.getTimeStart(), parent.getTimeStop());
            e.consume();
            //movableAnnotationController.adjustTimeStart();
        }
    }



}
