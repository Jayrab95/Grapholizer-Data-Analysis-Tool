package New.CustomControls.Annotation;


import New.Controllers.MutableSegmentController;
import New.CustomControls.TimeLine.CustomTimeLinePane;
import New.Dialogues.DialogControls.TopicTextControl;
import New.Dialogues.SegmentDialog;
import New.Interfaces.Selector;
import New.Observables.ObservableSegment;
import New.Observables.ObservableTopicSet;
import New.util.DialogGenerator;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Map;
import java.util.Optional;

public class MutableSegmentRectangle extends SelectableSegmentRectangle {

    private double[] dragBounds;
    private double mouseDelta;
    private ObservableSegment observableSegment;
    private ObservableTopicSet observableTopicSet;
    private MutableSegmentController mutableSegmentController;

    private DragRectangle left;
    private DragRectangle right;


    public MutableSegmentRectangle(ObjectProperty<Color> c, DoubleProperty scale, ObservableSegment t, CustomTimeLinePane parent, Selector observableSegment, ObservableTopicSet set) {
        super(c, t.getToolTipTextProperty(), t.getMainTopicAnnotationProperty() , scale, t.getDuration(), parent.getHeight(), t.getTimeStart(), parent, observableSegment);

        this.mutableSegmentController = new MutableSegmentController(t,parent);
        this.observableSegment =t;
        this.observableTopicSet = set;

        t.getTimeStartProperty().bind(this.xProperty().divide(scale));
        t.getTimeStopProperty().bind((this.xProperty().add(this.widthProperty())).divide(scale));

        this.left = new LeftDragRectangle(this);
        this.right = new RightDragRectangle(this);


        selected.addListener((observable, oldValue, newValue) -> {
            if(newValue){
                mutableSegmentController.enableResize(left, right);
            }
            else{
                mutableSegmentController.disableResize(left, right);
            }
        });

        setOnMousePressed(e-> handleMousePress(e));
        setOnMouseDragged(e-> handleMouseDrag(e));
        setOnMouseReleased(e-> handleMouseRelease(e));

        tooltip.setOnShowing(event -> this.toolTipTextProperty.set(this.observableSegment.generateText()));

    }

    public DragRectangle getLeft() {
        return left;
    }

    public DragRectangle getRight() {
        return right;
    }

    public ObservableTopicSet getObservableTopicSet() {
        return observableTopicSet;
    }

    public ObservableSegment getObservableSegment() {
        return observableSegment;
    }

    private void move(double newTimeStart){
        double delta = (newTimeStart - getX()) / scaleProperty.get();
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

        SegmentDialog dialog = new SegmentDialog(
                "Edit Segment",
                "Edit the text of your segment.",
                "Edit the text of your segment, then click on Ok to apply the changes.",
                observableTopicSet.getTopicsObservableList(),
                Optional.of(observableSegment.getInnerSegment()),
                false
                );
        dialog.setResultConverter(b -> {
            if (b == dialog.getButtonTypeOK()) {
                //TODO: Move this to controller??
                for(TopicTextControl ttc : dialog.getControls()){
                    observableSegment.putAnnotation(ttc.getTopicID(), ttc.getTextFieldText());
                }
            }
            return null;
        });
        dialog.showAndWait();
        /*
        Optional<String> newAnnotationText = DialogGenerator.simpleTextInputDialog(
                annotationText.get(),
                "Edit annotation",
                "Edit the text of your annotation, then click on OK to apply the changes.",
                annotationText.get()
        );
        if(newAnnotationText.isPresent()){
            movableAnnotationController.editElement(newAnnotationText.get());
        }

         */
    }

    private void handleEditTimeLineElementClick(){
        openEditDialog();
    }

    private void handleDeleteTimeLineElementClick(){
        if(DialogGenerator.confirmationDialogue(
                "Delete annotation",
                "Delete annotation?",
                "Are you sure you want to delete the annotation \"" + segmentTextProperty.get() + "\"? This action cannot be undone."))
        {
            mutableSegmentController.removeElement(this);
        }
    }

    //region TimeLineElement handlers

    @Override
    protected void handleMousePress(MouseEvent event){
        super.handleMousePress(event);
        //Todo: Can this if/else stucture be improved somehow?
        if(event.getClickCount() == 2 && event.getButton().equals(MouseButton.PRIMARY)){
            openEditDialog();
        }
        else if(event.getButton() == MouseButton.SECONDARY){
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
            dragBounds = mutableSegmentController.getBounds(event.getX());
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
        adjustStartAndDurationProperty();
        event.consume();
    }
    //endregion

    public boolean fitsCriteria(Map<String, String> filter){
        return mutableSegmentController.fitsFilterCriteria(filter);
    }

    private abstract class DragRectangle extends Rectangle {

        protected SegmentRectangle parent;
        protected final double size = 5;
        protected double startX = 0;

        private DragRectangle(SegmentRectangle parent){
            this.parent = parent;
            setWidth(calculateWidth());
            setHeight(parent.getHeight());
            setFill(Color.WHITESMOKE);
            parent.scaleProperty.addListener((observable, oldValue, newValue) -> {
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

        private LeftDragRectangle(SegmentRectangle parent) {
            super(parent);
            this.xProperty().bind(parent.xProperty());
        }

        @Override
        protected void handleMousePress(MouseEvent e) {
            startX = e.getSceneX();
            dragBounds = mutableSegmentController.getBounds(getX());
            dragBounds[1] = parent.getX() + parent.getWidth() - 1;
            e.consume();
        }

        @Override
        protected void handleMouseDrag(MouseEvent e) {
            double traveledDistance = startX - (e.getSceneX());
            if(e.getSceneX() < startX){
                //traveldirection: left. traveledDistance is positive
                if((parent.getX() - traveledDistance) > dragBounds[0]){
                    parent.setX(parent.getX() - traveledDistance);
                    parent.setWidth(parent.getWidth() + traveledDistance);
                }
                else{
                    parent.setX((dragBounds[0]));
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
            adjustStartAndDurationProperty();
            annotationSelectionController.selectOnlyDotsWithinTimeFrame(parent.getTimeStart(), parent.getTimeStop());
            e.consume();
        }
    }

    private class RightDragRectangle extends DragRectangle{

        private RightDragRectangle(SegmentRectangle parent) {
            super(parent);
            this.xProperty().bind(parent.xProperty().add(parent.widthProperty()));
        }


        @Override
        protected void handleMousePress(MouseEvent e) {
            startX = e.getSceneX();
            dragBounds = mutableSegmentController.getBounds(parent.getX() + parent.getWidth());
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
            adjustStartAndDurationProperty();
            annotationSelectionController.selectOnlyDotsWithinTimeFrame(parent.getTimeStart(), parent.getTimeStop());
            e.consume();
            //movableAnnotationController.adjustTimeStart();
        }
    }



}
