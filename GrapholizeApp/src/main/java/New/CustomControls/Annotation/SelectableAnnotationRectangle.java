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

    public SelectableAnnotationRectangle(ObjectProperty<Color> c, StringProperty text, DoubleProperty scale, double width, double height, double start, SelectableTimeLinePane parent, Selector s ) {
        super(c, text, scale, width, height, start);

        this.selected = new SimpleBooleanProperty(false);
        this.selected.addListener((observable, oldValue, newValue) -> onSelectionChange());

        this.annotationSelectionController = new AnnotationSelectionController(parent, s);

        setOnMouseClicked(e -> handleMouseClick(e));
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
        System.out.println("HandleMouseClick in AnnotationRectangle Base has been called");
        annotationSelectionController.selectTimeLine(e.isControlDown(), this);
        toggleSelected();
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
