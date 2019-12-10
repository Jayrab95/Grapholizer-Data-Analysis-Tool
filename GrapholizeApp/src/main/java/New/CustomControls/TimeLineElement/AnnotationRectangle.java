package New.CustomControls.TimeLineElement;

import New.Controllers.AnnotationController;
import New.CustomControls.TimeLine.TimeLinePane;
import New.Model.Entities.Annotation;
import New.Model.ObservableModel.ObservableAnnotation;
import javafx.beans.property.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class AnnotationRectangle extends Rectangle {

    protected StringProperty viewAnnotationTextProp;
    protected ObjectProperty<Color> annotationColor;
    protected BooleanProperty selected;
    protected DoubleProperty scale;

    protected AnnotationController annotationController;
    //Reason for having comment in baseclass: When copying annotations, there first needs to be a type check to see if
    //There's also a comment

    public AnnotationRectangle(ObjectProperty<Color> c, DoubleProperty scale, Annotation t, TimeLinePane parent){
        this.annotationColor = new SimpleObjectProperty<>(c.get());
        this.selected = new SimpleBooleanProperty(false);

        this.scale = new SimpleDoubleProperty(scale.get());
        this.scale.bind(scale);
        this.scale.addListener((observable, oldValue, newValue) -> onValueChange());

        this.annotationColor.bind(c);
        this.annotationColor.addListener((observable, oldValue, newValue) -> onColorChange());

        this.viewAnnotationTextProp = new SimpleStringProperty(t.getAnnotationText());

        ObservableAnnotation observableAnnotation = new ObservableAnnotation(t);
        viewAnnotationTextProp.bind(observableAnnotation.getAnnotationTextProperty());

        this.annotationController = new AnnotationController(observableAnnotation, parent);

        setHeight(parent.getHeight());
        double width = t.getDuration();
        setWidth(width);
        setX(t.getTimeStart());
        setY(0);

        setOnMouseClicked(e -> handleMouseClick(e));


        if(this.getWidth() > 0) {
            setFill(c.get());
        }
    }

    private void onColorChange(){
        setFill(annotationColor.get());
    }


    protected void rescaleElement(){
        setX(annotationController.getAnnotation().getTimeStart() * scale.get());
        setWidth((annotationController.getAnnotation().getDuration()) * scale.get());
    }

    protected void onValueChange(){
        rescaleElement();
    }


    public Annotation getTimeLineElement(){return this.annotationController.getAnnotation();}

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

    public boolean collidesWith(AnnotationRectangle other){
        return annotationController.getAnnotation().collidesWith(other.getTimeLineElement().getTimeStart(), other.getTimeLineElement().getTimeStop());
    }

    public boolean timeStampWithinTimeRange(double timeStamp){
        return annotationController.getAnnotation().timeStampWithinTimeRange(timeStamp);
    }

    protected void handleMouseClick(MouseEvent e){
        toggleSelected();
    }
}
