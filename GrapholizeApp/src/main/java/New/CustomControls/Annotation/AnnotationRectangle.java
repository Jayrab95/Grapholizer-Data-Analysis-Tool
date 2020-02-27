package New.CustomControls.Annotation;

import javafx.beans.property.*;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class AnnotationRectangle extends Rectangle {

    protected ObjectProperty<Color> annotationColor;
    protected StringProperty annotationText;
    protected DoubleProperty scale;

    protected DoubleProperty startProperty;
    protected DoubleProperty durationProperty;

    protected Label displayedText;
    protected Tooltip tooltip;



    public AnnotationRectangle(ObjectProperty<Color> c, StringProperty text, DoubleProperty scale, double width, double height, double start){
        this.annotationColor = new SimpleObjectProperty<>(c.get());
        this.annotationColor.bind(c);
        this.annotationColor.addListener((observable, oldValue, newValue) -> onColorChange());

        this.annotationText = new SimpleStringProperty(text.get());
        this.annotationText.bind(text);

        this.scale = new SimpleDoubleProperty(scale.get());
        this.scale.bind(scale);
        this.scale.addListener((observable, oldValue, newValue) -> onScaleChange());

        this.durationProperty = new SimpleDoubleProperty(width);
        this.startProperty = new SimpleDoubleProperty(start);

        //this.durationProperty.bind(this.widthProperty().divide(this.scale));
        //this.startProperty.bind(this.xProperty().divide(this.scale));

        this.displayedText = new Label(text.get());
        this.displayedText.textProperty().bind(text);
        this.displayedText.setLabelFor(this);
        this.displayedText.translateXProperty().bind((this.xProperty().add((this.widthProperty()).divide(2))).subtract(this.displayedText.widthProperty().divide(2)));
        this.displayedText.setTranslateY((getHeight()/2) + (this.displayedText.getHeight()/2));
        this.displayedText.visibleProperty().bind(widthProperty().greaterThanOrEqualTo(this.displayedText.widthProperty()));

        this.tooltip = new Tooltip(text.get());
        this.tooltip.textProperty().bind(text);
        Tooltip.install(this, tooltip);

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

        setX(startProperty.get() * scale.get());
        setWidth(durationProperty.get() * scale.get());
    }

    protected void onScaleChange(){
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

    public Label getDisplayedText() {
        return displayedText;
    }

    protected void adjustStartAndDurationProperty(){
        startProperty.set(getTimeStart());
        durationProperty.set(getTimeStop() - getTimeStart());
    }
}
