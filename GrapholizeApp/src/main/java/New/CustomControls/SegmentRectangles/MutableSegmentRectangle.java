package New.CustomControls.SegmentRectangles;


import New.Controllers.MutableSegmentController;
import New.CustomControls.SegmentationPanes.CustomSegmentationPane;
import New.Dialogues.DialogControls.TopicTextControl;
import New.Dialogues.SegmentDialog;
import New.Interfaces.Selector;
import New.Observables.ObservableSegment;
import New.Observables.ObservableSuperSet;
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

/**
 * The MutableSegmentRectangle extends from the SelectableSegmentRectangle. It is responsible
 * for handling the mutation logic (moving and adjusting size) of segments.
 */
public class MutableSegmentRectangle extends SelectableSegmentRectangle {

    private double[] dragBounds;
    private double mouseDelta;
    private ObservableSegment observableSegment;
    private ObservableSuperSet observableSuperSet;
    private MutableSegmentController mutableSegmentController;

    private DragRectangle left;
    private DragRectangle right;

    /**
     * Constructor for MutableSegmentRectangles. Note that the String properties for the display and too tip texts are
     * directly taken from the given observableSegment
     * @param colorProperty The color property which determines the color of this segment.
     * @param scale the double property which determines the currently active scale
     * @param observableSegment the observableSegment which wraps the segment object responsible for this graphical segment Rectangle
     * @param parentCustomSegmentationPane the segmentation this segment belongs to
     * @param observablePage the observable page this segment belongs to (or rather the segmentation this segment is currently in)
     * @param set The super set which defines the topics for this segment
     */
    public MutableSegmentRectangle(ObjectProperty<Color> colorProperty, DoubleProperty scale, ObservableSegment observableSegment, CustomSegmentationPane parentCustomSegmentationPane, Selector observablePage, ObservableSuperSet set) {
        super(colorProperty, observableSegment.getToolTipTextProperty(), observableSegment.getMainTopicAnnotationProperty() , scale, observableSegment.getDuration(), observableSegment.getTimeStart(), parentCustomSegmentationPane, observablePage, observableSegment);

        this.mutableSegmentController = new MutableSegmentController(observableSegment,parentCustomSegmentationPane);
        this.observableSegment = observableSegment;
        this.observableSuperSet = set;

        observableSegment.getTimeStartProperty().bind(this.xProperty().divide(scale));
        observableSegment.getTimeStopProperty().bind((this.xProperty().add(this.widthProperty())).divide(scale));
        observableSegment.getSelectedProperty().bindBidirectional(this.selected);

        this.left = new LeftDragRectangle(this);
        this.right = new RightDragRectangle(this);


        this.selected.addListener((observable, oldValue, newValue) -> {
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

        tooltip.setOnShowing(event -> this.toolTipTextProperty.set(this.observableSegment.generateToolTipText()));

    }

    /**
     *
     * @return Returns the left DragRectangle
     */
    public DragRectangle getLeft() {
        return left;
    }

    /**
     *
     * @return Returns the right DragRectangle
     */
    public DragRectangle getRight() {
        return right;
    }

    /**
     *
     * @return Returns the observableSuperSet for this segment
     */
    public ObservableSuperSet getObservableSuperSet() {
        return observableSuperSet;
    }

    /**
     *
     * @return returns the observableSegment for this segment
     */
    public ObservableSegment getObservableSegment() {
        return observableSegment;
    }

    /**
     * Adjusts the X position value for this segment
     * @param newXPos the new X value for this rectangle (unscaled)
     */
    private void move(double newXPos){
        this.setX(newXPos);
    }

    //region ContextMenu
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
                observableSuperSet.getTopicsObservableList(),
                Optional.of(observableSegment.getInnerSegment()),
                false
                );
        dialog.setResultConverter(b -> {
            if (b == dialog.getButtonTypeOK()) {
                for(TopicTextControl ttc : dialog.getControls()){
                    observableSegment.putAnnotation(ttc.getTopicID(), ttc.getTextFieldText());
                }
            }
            return null;
        });
        dialog.showAndWait();
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

    public void deleteSegment(){
        mutableSegmentController.removeElement(this);
    }
    //endregion

    //region mouse event handlers

    @Override
    protected void handleMousePress(MouseEvent event){
        if(!event.isShiftDown()){
            super.handleMousePress(event);
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
    }

    private void handleMouseDrag(MouseEvent event){
        if(!event.isShiftDown()){
            if(event.getButton() == MouseButton.PRIMARY){
                if(left.contains(event.getX(), event.getY())){
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
            if(event.getButton() == MouseButton.SECONDARY){}
        }
    }

    @Override
    protected void handleMouseRelease(MouseEvent event){
        if(!event.isShiftDown()){
            super.handleMouseRelease(event);
            adjustStartAndDurationProperty();
            event.consume();
        }

    }
    //endregion

    /**
     * Checks if the annotations adhere to the filter options
     * @param filter filter options map (key = topic ID, value = filter text)
     * @return true if the segment fits the filter criteria
     */
    public boolean fitsCriteria(Map<String, String> filter){
        return mutableSegmentController.fitsFilterCriteria(filter);
    }

    //region DragRectangles
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
            selectableSegmentController.selectOnlyDotsWithinTimeFrame(parent.getTimeStart(), parent.getTimeStop());
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
                }
                else{
                    parent.setWidth(5);
                }
            }
            else{
                //traveldirection: right, travelDistance is negative
                if((parent.getX() + parent.getWidth() + traveledDistance) < dragBounds[1]) {
                    parent.setWidth(parent.getWidth() - traveledDistance);
                }
                else {
                    parent.setWidth(dragBounds[1] - parent.getX());
                }
            }
            startX = e.getSceneX();
        }

        @Override
        protected void handleMouseRelease(MouseEvent e) {
            adjustStartAndDurationProperty();
            selectableSegmentController.selectOnlyDotsWithinTimeFrame(parent.getTimeStart(), parent.getTimeStop());
            e.consume();
        }
    }
    //endregion

}
