package New.CustomControls.Annotation;

import New.Controllers.AnnotationSelectionController;
import New.CustomControls.TimeLine.CustomTimeLinePane;
import New.CustomControls.TimeLine.SelectableTimeLinePane;
import javafx.beans.property.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class AnnotationRectangle extends Rectangle {

    protected ObjectProperty<Color> annotationColor;
    protected StringProperty annotationText;
    protected DoubleProperty scale;
    protected double duration;
    protected double start;



    public AnnotationRectangle(ObjectProperty<Color> c, StringProperty text, DoubleProperty scale, double width, double height, double start){
        this.annotationColor = new SimpleObjectProperty<>(c.get());
        this.annotationColor.bind(c);
        this.annotationColor.addListener((observable, oldValue, newValue) -> onColorChange());

        this.annotationText = new SimpleStringProperty(text.get());
        this.annotationText.bind(text);

        this.scale = new SimpleDoubleProperty(scale.get());
        this.scale.bind(scale);
        this.scale.addListener((observable, oldValue, newValue) -> onValueChange());

        this.duration = width;
        this.start = start;

        setHeight(height);
        setWidth(width * scale.get());
        setX(start * scale.get());
        setY(0);

        if(this.getWidth() > 0) {
            setFill(c.get());
        }
    }

    private void onColorChange(){
        System.out.println("Color change has been called");
        setFill(annotationColor.get());
    }


    protected void rescaleElement(){
        setX(start * scale.get());
        setWidth(duration * scale.get());
    }

    protected void onValueChange(){
        rescaleElement();
    }

    public double getTimeStart(){
        return this.getX() / scale.get();
    }

    public double getTimeStop(){
        return (this.getX() + this.getWidth()) / scale.get();
    }

    public String getText(){
        return annotationText.get();
    }

}
