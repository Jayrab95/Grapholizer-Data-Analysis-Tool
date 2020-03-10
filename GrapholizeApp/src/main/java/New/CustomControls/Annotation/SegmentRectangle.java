package New.CustomControls.Annotation;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class SegmentRectangle extends Rectangle implements Comparable<SegmentRectangle> {

    protected ObjectProperty<Color> segmentColorProperty;
    protected StringProperty segmentTextProperty;
    protected StringProperty toolTipTextProperty;
    protected DoubleProperty scaleProperty;

    protected DoubleProperty startProperty;
    protected DoubleProperty durationProperty;

    protected Label displayedText;
    protected Tooltip tooltip;

    public SegmentRectangle(ObjectProperty<Color> c, StringProperty toolTipTextProperty, StringProperty labelTextProperty, DoubleProperty scaleProperty, double width, double height, double start){
        this.segmentColorProperty = new SimpleObjectProperty<>(c.get());
        this.segmentColorProperty.bind(c);
        this.segmentColorProperty.addListener((observable, oldValue, newValue) -> onColorChange());

        this.segmentTextProperty = new SimpleStringProperty(labelTextProperty.get());
        this.segmentTextProperty.bind(labelTextProperty);

        this.toolTipTextProperty = new SimpleStringProperty(toolTipTextProperty.get());

        this.scaleProperty = new SimpleDoubleProperty(scaleProperty.get());
        this.scaleProperty.bind(scaleProperty);
        this.scaleProperty.addListener((observable, oldValue, newValue) -> onScaleChange());

        this.durationProperty = new SimpleDoubleProperty(width);
        this.startProperty = new SimpleDoubleProperty(start);

        //this.durationProperty.bind(this.widthProperty().divide(this.scale));
        //this.startProperty.bind(this.xProperty().divide(this.scale));

        this.displayedText = new Label(segmentTextProperty.get());
        this.displayedText.textProperty().bind(segmentTextProperty);
        this.displayedText.setLabelFor(this);
        this.displayedText.translateXProperty().bind((this.xProperty().add((this.widthProperty()).divide(2))).subtract(this.displayedText.widthProperty().divide(2)));
        this.displayedText.setTranslateY((getHeight()/2) + (this.displayedText.getHeight()/2));
        this.displayedText.visibleProperty().bind(widthProperty().greaterThanOrEqualTo(this.displayedText.widthProperty()));

        this.tooltip = new Tooltip(this.toolTipTextProperty.get());
        this.tooltip.textProperty().bind(this.toolTipTextProperty);
        Tooltip.install(this, tooltip);

        setHeight(height);
        setWidth(width * scaleProperty.get());
        setX(start * scaleProperty.get());
        setY(0);

        if(this.getWidth() > 0) {
            setFill(c.get());
        }
    }

    private void onColorChange(){
        System.out.println("Color change has been called");
        setFill(segmentColorProperty.get());
    }


    protected void rescaleElement(){

        setX(startProperty.get() * scaleProperty.get());
        setWidth(durationProperty.get() * scaleProperty.get());
    }

    protected void onScaleChange(){
        rescaleElement();
    }

    public double getTimeStart(){
        return this.getX() / scaleProperty.get();
    }

    public double getTimeStop(){
        return (this.getX() + this.getWidth()) / scaleProperty.get();
    }

    public String getText(){
        return segmentTextProperty.get();
    }

    public Label getDisplayedText() {
        return displayedText;
    }

    protected void adjustStartAndDurationProperty(){
        startProperty.set(getTimeStart());
        durationProperty.set(getTimeStop() - getTimeStart());
    }

    @Override
    public int compareTo(SegmentRectangle o) {
        return Double.compare(this.getTimeStart(), o.getTimeStart());
    }
}
