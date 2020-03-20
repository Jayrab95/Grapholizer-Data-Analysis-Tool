package New.CustomControls.Annotation;

import New.Controllers.SelectableSegmentController;
import New.CustomControls.TimeLine.SelectableSegmentationPane;
import New.Interfaces.Selector;
import New.Observables.ObservableSegment;
import javafx.beans.property.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * The SelectableSegmentRectangle extends from the SegmentRectangle and is responsible for
 * handling the selection logic of the segments. Note that selecting a SelectableSegmentRectangle
 * will also select the parent segmentation to become the active segmentation.
 */
public class SelectableSegmentRectangle extends SegmentRectangle {
    protected BooleanProperty selected;
    protected SelectableSegmentController selectableSegmentController;

    private double temporaryPreviousStart;
    private double temporaryPreviousStop;


    /**
     *
     * @param colorProperty The color property which determines the color of this segment.
     * @param toolTipTextProperty The string property which determines the toopTipText of this segment.
     * @param segmentLabelTextProperty the string property which determines the display text of this segment
     * @param scale the double property which determines the currently active scale
     * @param width the initial width of this segment
     * @param start the initial timeStart value of this segment (unscaled)
     * @param parent the segmentation this segment belongs to
     * @param selector the selector component (page) which contains selectable dots
     * @param oSegment the observableSegment which wraps the segment object responsible for this graphical segment Rectangle
     */
    public SelectableSegmentRectangle(ObjectProperty<Color> colorProperty, StringProperty toolTipTextProperty, StringProperty segmentLabelTextProperty, DoubleProperty scale, double width, double start, SelectableSegmentationPane parent, Selector selector, ObservableSegment oSegment) {
        super(colorProperty, toolTipTextProperty, segmentLabelTextProperty, scale, width, parent.getHeight(), start);

        this.selected = new SimpleBooleanProperty(false);
        this.selected.bindBidirectional(oSegment.getSelectedProperty());
        this.selected.addListener((observable, oldValue, newValue) -> onSelectionChange());

        this.selectableSegmentController = new SelectableSegmentController(parent, selector);

        setOnMousePressed(this::handleMousePress);
        setOnMouseMoved(e -> e.consume());
        setOnMouseReleased(this::handleMouseRelease);
    }

    /**
     * Returns the BooleanProperty responsible for the selection state of this SelectableSegmentRectangle
     * @return the boolean property
     */
    public BooleanProperty getSelectedBooleanProperty(){return this.selected;}

    /**
     *
     * @return the boolean value denoting the current selection state
     */
    public boolean isSelected(){
        return selected.get();
    }

    /**
     * Sets the current selection state to the given boolean value
     * @param selected value to which the selected status will be set to.
     */
    public void setSelected(boolean selected){
        this.selected.set(selected);
    }

    /**
     * Toggles the current selected value (true if currently false and false if currently true)
     */
    public void toggleSelected(){
        this.selected.set(!selected.get());
    }


    protected void handleMouseClick(MouseEvent e){
        System.out.println("HandleMouseClick in SelectionAnnotationRectangle has been called");
        selectableSegmentController.selectTimeLine(e.isControlDown(), this);
        selectableSegmentController.selectOnlyDotsWithinTimeFrame(getTimeStart(), getTimeStop());
        toggleSelected();
    }

    protected void handleMousePress(MouseEvent e){
        selectableSegmentController.selectTimeLine((e.isControlDown()), this);
        temporaryPreviousStart = getTimeStart();
        temporaryPreviousStop = getTimeStop();
    }

    protected void handleMouseRelease(MouseEvent e){
        if(getTimeStart() == temporaryPreviousStart && getTimeStop() == temporaryPreviousStop){
            toggleSelected();
        }
        else{
            selectableSegmentController.selectOnlyDotsWithinTimeFrame(getTimeStart(), getTimeStop());
        }
    }

    private void onSelectionChange(){
        if (selected.get()) {
            setStroke(Color.GREEN);
            setStrokeWidth(5);
            selectableSegmentController.selectDots(getTimeStart(), getTimeStop());
        }
        else {
            setStroke(Color.BLACK);
            setStrokeWidth(1);
            selectableSegmentController.deselectDots(getTimeStart(), getTimeStop());
        }
    }
}
