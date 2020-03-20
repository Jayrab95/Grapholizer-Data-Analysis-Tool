package New.CustomControls.Containers;

import New.Filters.*;
import New.Interfaces.Selector;
import New.Observables.*;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.effect.Light;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.List;

public class MainCanvas extends VBox{
    private Slider scaleSlider;
    private ScrollPane canvasContainer;
    private FilterContainer filterContainer;
    private final Pane canvas;
    private final double canvasWidth;
    private final double canvasHeight;
    private DoubleProperty canvasScale;
    private Selector selector;


    private Light.Point anchor;
    private Rectangle selection;

    private ObservableFilterCollection ofc;
    private ObservableSegmentation selectedTimeLine;


    public MainCanvas(double initWidth, double initHeight, double initScale, ObservablePage obsPage, ObservableSegmentation observableSegmentation){
        this.canvasWidth = initWidth;
        this.canvasHeight = initHeight;
        this.canvasScale = new SimpleDoubleProperty(initScale);
        this.selector = obsPage;
        this.ofc = new ObservableFilterCollection(
                new DefaultFilter(obsPage),
                new StrokeColorFilter(obsPage,Color.BLUE, Color.RED),
                new PressureFilter(obsPage),
                new VelocityFilter(obsPage));

        this.scaleSlider = initializeSlider(initScale);

        this.canvasScale.bind(this.scaleSlider.valueProperty());
        this.canvasScale.addListener((observable, oldValue, newValue) -> {
            resizeCanvas();
            setScroll(oldValue.doubleValue(), newValue.doubleValue());
        });

        this.canvas = new Pane();
        this.canvas.setPrefWidth(initWidth * canvasScale.get());
        this.canvas.setPrefHeight(initHeight * canvasScale.get());

        obsPage.getPageProperty().addListener((observable, oldValue, newValue) -> addStrokes(obsPage));

        initializeSelector();
        canvas.setOnMousePressed(this::startSelection);
        canvas.setOnMouseDragged(this::moveSelection);
        canvas.setOnMouseReleased(this::endSelection);

        canvasContainer = new ScrollPane(canvas);
        filterContainer = new FilterContainer(ofc);
        getChildren().addAll(scaleSlider, filterContainer,canvasContainer);
        addStrokes(obsPage);

        this.selectedTimeLine = observableSegmentation;
        observableSegmentation.getSelectedSegmentationProperty().addListener((observable, oldValue, newValue) -> {
            obsPage.deselectAll();
        });
    }

    private Slider initializeSlider(double init){
        //Note for using JavaFX Canvas:
        //40 is the max scale. Anything above can cause a bug with the graphics object.
        //https://bugs.openjdk.java.net/browse/JDK-8174077
        Slider slider = new Slider(1, 40, init);
        return slider;
    }

    private void resizeCanvas(){
        canvas.setPrefWidth(canvasWidth * canvasScale.get());
        canvas.setPrefHeight(canvasHeight * canvasScale.get());
    }

    private void setScroll(double oldScale, double newScale){
        double hPos = canvasContainer.getHvalue() / oldScale;
        double vPos = canvasContainer.getVvalue() / oldScale;
        canvasContainer.setHvalue(hPos * newScale);
        canvasContainer.setVvalue(vPos * newScale);
    }

    private void addStrokes(ObservablePage p){
        for(Node n : canvas.getChildren()){
            ((DotLine)n).unlink();
        }
        canvas.getChildren().clear();
        for(ObservableStroke s : p.getObservableStrokes()){
            List<ObservableDot> dots = s.getObservableDots();
            for (int i = 0; i < dots.size() - 1; i++) {
                canvas.getChildren().add(new DotLine(dots.get(i), dots.get(i+1), canvasScale));
            }
        }
    }


    private void initializeSelector(){
        anchor = new Light.Point();
        selection = new Rectangle();
        selection.setFill(Color.TRANSPARENT);
        selection.setStroke(Color.BLACK); // border
        selection.setStrokeWidth(1);
        selection.getStrokeDashArray().add(10.0);
    }

    //Source: https://coderanch.com/t/689100/java/rectangle-dragging-image
    private void startSelection(MouseEvent event){
        //This forces all segmentRectangles to become unselected
        selectedTimeLine.setSelectedTimeLine(null);
        anchor.setX(event.getX());
        anchor.setY(event.getY());
        selection.setX(event.getX());
        selection.setY(event.getY());
        selection.setStroke(Color.BLACK); // border
        selection.getStrokeDashArray().add(10.0);
        canvas.getChildren().add(selection);
    }

    //Source: https://coderanch.com/t/689100/java/rectangle-dragging-image
    private void moveSelection(MouseEvent event){
        selection.setWidth(Math.abs(event.getX() - anchor.getX()));
        selection.setHeight(Math.abs(event.getY() - anchor.getY()));
        selection.setX(Math.min(anchor.getX(), event.getX()));
        selection.setY(Math.min(anchor.getY(), event.getY()));
    }

    private void endSelection(MouseEvent event){
        if (event.isShiftDown()) {
            selector.selectRectUnscaled(selection.getX(), selection.getY(), selection.getWidth(), selection.getHeight(), canvasScale.get());
        } else {
            selector.selectOnlyRectUnscaled(selection.getX(), selection.getY(), selection.getWidth(), selection.getHeight(), canvasScale.get());
        }
        //Reset selection rectangle and remove it from canvas.
        selection.setWidth(0);
        selection.setHeight(0);
        canvas.getChildren().remove(selection);
    }


}
