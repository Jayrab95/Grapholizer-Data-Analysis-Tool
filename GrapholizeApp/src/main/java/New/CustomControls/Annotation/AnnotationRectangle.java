package New.CustomControls.Annotation;

import javafx.beans.property.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public abstract class AnnotationRectangle extends Rectangle {

    protected ObjectProperty<Color> annotationColor;
    protected StringProperty annotationText;
    protected BooleanProperty selected;
    protected DoubleProperty scale;


    public AnnotationRectangle(ObjectProperty<Color> c, StringProperty text, DoubleProperty scale, double width, double height, double start){
        this.annotationColor = new SimpleObjectProperty<>(c.get());
        this.selected = new SimpleBooleanProperty(false);

        this.annotationText = new SimpleStringProperty(text.get());
        this.annotationText.bind(text);

        this.scale = new SimpleDoubleProperty(scale.get());
        this.scale.bind(scale);
        this.scale.addListener((observable, oldValue, newValue) -> onValueChange());

        this.annotationColor.bind(c);
        this.annotationColor.addListener((observable, oldValue, newValue) -> onColorChange());

        setHeight(height);
        setWidth(width);
        setX(start);
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
        setX(getX() * scale.get());
        setWidth(getWidth() * scale.get());
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
        toggleSelected();
    }
}
