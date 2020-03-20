package New.CustomControls.Annotation;

import javafx.beans.property.*;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * The SegmentRectangle class is a base class for visual segment components.
 * The SegmentRectangle class is responsible for displaying the text of the main topic annotation,
 * displaying the tooltip of the segment and adjusting the size of the segment if the scale of the segment container
 * changes.
 */
public class SegmentRectangle extends Rectangle implements Comparable<SegmentRectangle> {

    protected ObjectProperty<Color> segmentColorProperty;
    protected StringProperty segmentTextProperty;
    protected StringProperty toolTipTextProperty;
    protected DoubleProperty scaleProperty;

    protected DoubleProperty startProperty;
    protected DoubleProperty durationProperty;

    protected Label label_DisplayedText;
    protected Tooltip tooltip;

    /**
     *
     * @param colorProperty The color property which determines the color of this segment.
     * @param toolTipTextProperty The string property which determines the toopTipText of this segment.
     * @param labelTextProperty the string property which determines the display text of this segment
     * @param scaleProperty the double property which determines the currently active scale
     * @param width the initial width of this segment
     * @param height the initial height of this segment
     * @param start the initial timeStart value of this segment (unscaled)
     */
    public SegmentRectangle(ObjectProperty<Color> colorProperty, StringProperty toolTipTextProperty, StringProperty labelTextProperty, DoubleProperty scaleProperty, double width, double height, double start){
        this.segmentColorProperty = new SimpleObjectProperty<>(colorProperty.get());
        this.segmentColorProperty.bind(colorProperty);
        this.segmentColorProperty.addListener((observable, oldValue, newValue) -> onColorChange());

        this.segmentTextProperty = new SimpleStringProperty(labelTextProperty.get());
        this.segmentTextProperty.bind(labelTextProperty);

        this.toolTipTextProperty = new SimpleStringProperty(toolTipTextProperty.get());

        this.scaleProperty = new SimpleDoubleProperty(scaleProperty.get());
        this.scaleProperty.bind(scaleProperty);
        this.scaleProperty.addListener((observable, oldValue, newValue) -> onScaleChange());

        this.durationProperty = new SimpleDoubleProperty(width);
        this.startProperty = new SimpleDoubleProperty(start);

        this.label_DisplayedText = new Label(segmentTextProperty.get());
        this.label_DisplayedText.textProperty().bind(segmentTextProperty);
        this.label_DisplayedText.setLabelFor(this);
        this.label_DisplayedText.translateXProperty().bind((this.xProperty().add((this.widthProperty()).divide(2))).subtract(this.label_DisplayedText.widthProperty().divide(2)));
        this.label_DisplayedText.setTranslateY((getHeight()/2) + (this.label_DisplayedText.getHeight()/2));
        this.label_DisplayedText.visibleProperty().bind(widthProperty().greaterThanOrEqualTo(this.label_DisplayedText.widthProperty()));

        this.tooltip = new Tooltip(this.toolTipTextProperty.get());
        this.tooltip.textProperty().bind(this.toolTipTextProperty);
        Tooltip.install(this, tooltip);

        setHeight(height);
        setWidth(width * scaleProperty.get());
        setX(start * scaleProperty.get());
        setY(0);

        if(this.getWidth() > 0) {
            setFill(colorProperty.get());
            setStroke(Color.BLACK);
        }
    }

    /**
     * Changes the color of the segment. This method is called when the color property is notified by a change event
     */
    private void onColorChange(){
        setFill(segmentColorProperty.get());
    }


    /**
     * Rescales the segment by adjusting its X position and width according to the scale.
     */
    protected void rescaleSegment(){

        setX(startProperty.get() * scaleProperty.get());
        setWidth(durationProperty.get() * scaleProperty.get());
    }

    /**
     * Rescales the element. This method is called when the scale property is notified by a change event.
     */
    protected void onScaleChange(){
        rescaleSegment();
    }

    /**
     * Returns the timeStart value of this segment by dividing the current X position by the scale.
     * @return the timeStart value of the segment
     */
    public double getTimeStart(){
        return this.getX() / scaleProperty.get();
    }

    /**
     * Returns the timeStop value of this segment by adding the current X position and the width of this
     * rectangle and dividing that value by the scale.
     * @return the timeStop value of the segment
     */
    public double getTimeStop(){
        return (this.getX() + this.getWidth()) / scaleProperty.get();
    }

    /**
     * @return the text that is currently displayed.
     */
    public String getText(){
        return segmentTextProperty.get();
    }

    /**
     * @return the label responsible for the display text of this segment
     */
    public Label getDisplayedTextLabel() {
        return label_DisplayedText;
    }

    /**
     * Overwrites the startProperty and duration property with the current values of the Rectangle (divided by the scale)
     */
    protected void adjustStartAndDurationProperty(){
        startProperty.set(getTimeStart());
        durationProperty.set(getTimeStop() - getTimeStart());
    }

    /**
     * Compares this SegmentRectangle to another one by comparing their timeStart values.
     * @param o Other segment rectangle which needs to be compared.
     * @return 1, if this Rectangle has a larger timeStart value, -1 if it has a smaller timeStart value, 0 if they're the same.
     */
    @Override
    public int compareTo(SegmentRectangle o) {
        return Double.compare(this.getTimeStart(), o.getTimeStart());
    }
}
