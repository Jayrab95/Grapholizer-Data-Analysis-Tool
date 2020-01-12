package New.CustomControls.Annotation;

import New.Controllers.AnnotationSelectionController;
import New.CustomControls.TimeLine.SelectableTimeLinePane;
import New.Interfaces.Selector;
import javafx.beans.property.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class SelectableAnnotationRectangle extends AnnotationRectangle {
    protected BooleanProperty selected;
    protected AnnotationSelectionController annotationSelectionController;

    private double temporaryPreviousStart;
    private double temporaryPreviousStop;

    public SelectableAnnotationRectangle(ObjectProperty<Color> c, StringProperty text, DoubleProperty scale, double width, double height, double start, SelectableTimeLinePane parent, Selector s ) {
        super(c, text, scale, width, height, start);

        this.selected = new SimpleBooleanProperty(false);
        this.selected.addListener((observable, oldValue, newValue) -> onSelectionChange());

        this.annotationSelectionController = new AnnotationSelectionController(parent, s);

        //setOnMouseClicked(e -> handleMouseClick(e));
        setOnMousePressed(this::handleMousePress);
        setOnMouseMoved(e -> e.consume());
        setOnMouseReleased(this::handleMouseRelease);
        //setOnMouseClicked(e)
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
        System.out.println("Mouse Down on selectionRect");
        annotationSelectionController.selectTimeLine(e.isControlDown(), this);
        temporaryPreviousStart = getTimeStart();
        temporaryPreviousStop = getTimeStop();
    }

    //TODO: Selection state is inconsistent at the moment
    //
    protected void handleMouseRelease(MouseEvent e){
        System.out.println("Handlerelease in selectionRectangle called");

        if(getTimeStart() == temporaryPreviousStart && getTimeStop() == temporaryPreviousStop){
            toggleSelected();
        }
        else{
            annotationSelectionController.selectOnlyDotsWithinTimeFrame(getTimeStart(), getTimeStop());
        }
    }



    private void onSelectionChange(){
        System.out.println("on selection change called");
        if (selected.get()) {
            setStroke(Color.GREEN);
            setStrokeWidth(5);
            annotationSelectionController.selectDots(getTimeStart(), getTimeStop());
        }
        else {
            setStroke(annotationColor.get());
            annotationSelectionController.deselectDots(getTimeStart(), getTimeStop());
        }
    }
}
