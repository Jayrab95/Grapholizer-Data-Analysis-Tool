package New.CustomControls.Annotation;

import New.Controllers.AnnotationSelectionController;
import New.CustomControls.TimeLine.SelectableSegmentationPane;
import New.Interfaces.Selector;
import New.Observables.ObservableSegment;
import javafx.beans.property.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class SelectableSegmentRectangle extends SegmentRectangle {
    protected BooleanProperty selected;
    protected AnnotationSelectionController annotationSelectionController;

    private double temporaryPreviousStart;
    private double temporaryPreviousStop;


    public SelectableSegmentRectangle(ObjectProperty<Color> c, StringProperty toolTipTextProperty, StringProperty segmentLabelTextProperty, DoubleProperty scale, double width, double start, SelectableSegmentationPane parent, Selector s, ObservableSegment oSegment) {
        super(c, toolTipTextProperty, segmentLabelTextProperty, scale, width, parent.getHeight(), start);

        this.selected = new SimpleBooleanProperty(false);
        this.selected.bindBidirectional(oSegment.getSelectedProperty());
        this.selected.addListener((observable, oldValue, newValue) -> onSelectionChange());

        this.annotationSelectionController = new AnnotationSelectionController(parent, s);

        setOnMousePressed(this::handleMousePress);
        setOnMouseMoved(e -> e.consume());
        setOnMouseReleased(this::handleMouseRelease);
    }

    public BooleanProperty getSelectedBooleanProperty(){return this.selected;}
    public boolean isSelected(){
        return selected.get();
    }
    public void setSelected(boolean selected){
        this.selected.set(selected);
    }

    public void toggleSelected(){
        this.selected.set(!selected.get());
    }


    protected void handleMouseClick(MouseEvent e){
        System.out.println("HandleMouseClick in SelectionAnnotationRectangle has been called");
        annotationSelectionController.selectTimeLine(e.isControlDown(), this);
        annotationSelectionController.selectOnlyDotsWithinTimeFrame(getTimeStart(), getTimeStop());
        toggleSelected();
    }

    protected void handleMousePress(MouseEvent e){
        annotationSelectionController.selectTimeLine((e.isControlDown()), this);
        temporaryPreviousStart = getTimeStart();
        temporaryPreviousStop = getTimeStop();
    }

    //TODO: Selection state is inconsistent at the moment
    //
    protected void handleMouseRelease(MouseEvent e){
        if(getTimeStart() == temporaryPreviousStart && getTimeStop() == temporaryPreviousStop){
            toggleSelected();
        }
        else{
            annotationSelectionController.selectOnlyDotsWithinTimeFrame(getTimeStart(), getTimeStop());
        }
    }



    private void onSelectionChange(){
        if (selected.get()) {
            setStroke(Color.GREEN);
            setStrokeWidth(5);
            annotationSelectionController.selectDots(getTimeStart(), getTimeStop());
        }
        else {
            setStroke(null);
            annotationSelectionController.deselectDots(getTimeStart(), getTimeStop());
        }
    }
}
