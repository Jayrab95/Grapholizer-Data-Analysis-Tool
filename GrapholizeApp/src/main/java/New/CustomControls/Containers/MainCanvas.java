package New.CustomControls.Containers;

import New.Interfaces.Observer.PageObserver;
import New.Interfaces.Observer.StrokeObserver;
import New.Model.Entities.Dot;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import New.Observables.ObservablePage;
import New.Observables.ObservableStroke;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.effect.Light;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class MainCanvas extends VBox implements PageObserver, StrokeObserver {
    private Slider scaleSlider;
    private ScrollPane canvasContainer;
    private final Canvas canvas;
    private final double canvasWidth;
    private final double canvasHeight;
    private DoubleProperty canvasScale;
    private ObservablePage p;

    private Light.Point anchor;
    private Rectangle selection;

    public MainCanvas(double initWidth, double initHeight, double initScale, ObservablePage obsPage){
        this.canvasWidth = initWidth;
        this.canvasHeight = initHeight;
        this.canvasScale = new SimpleDoubleProperty(initScale);
        this.p = obsPage;

        scaleSlider = initializeSlider(initScale);

        canvasScale.bind(scaleSlider.valueProperty());
        canvasScale.addListener((observable, oldValue, newValue) -> resetCanvas());

        canvas = new Canvas(initWidth * canvasScale.get(), initHeight * canvasScale.get());
        obsPage.addObserver(this);
        obsPage.registerStrokeObserver(this);

        initializeSelector();
        canvas.setOnMousePressed(this::startSelection);
        canvas.setOnMouseDragged(this::moveSelection);
        canvas.setOnMouseReleased(this::endSelection);

        initializeCanvas();
    }

    private void initializeCanvas(){
        canvasContainer = new ScrollPane(canvas);

        getChildren().addAll(scaleSlider, canvasContainer);
        drawStrokes();
    }

    private Slider initializeSlider(double init){
        //40 is the max scale. Anything above can cause a bug with the graphics object.
        //https://bugs.openjdk.java.net/browse/JDK-8174077
        Slider slider = new Slider(1, 40, init);
        return slider;
    }

    private void resizeCanvas(){
        canvas.setWidth(canvasWidth * canvasScale.get());
        canvas.setHeight(canvasHeight * canvasScale.get());
    }

    private void drawStrokes(){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        for(ObservableStroke s : p.getObservableStrokes()){

            for(int i = 0; i < s.getDots().size() - 1; i++){

                Dot d1 = s.getDots().get(i);
                Dot d2 = s.getDots().get(i + 1);
                double fAvg = (d1.getForce() + d2.getForce()) / 2;
                //TODO: Make draw stroke specific (Use Decorator pattern for different visual filters
                if(s.isSelected()){
                    gc.setLineWidth((fAvg) + 2);
                    gc.setStroke(new Color(0,1, 0, 1));
                    gc.strokeLine(d1.getX() * canvasScale.get(), d1.getY() * canvasScale.get(), d2.getX() * canvasScale.get(), d2.getY() * canvasScale.get());
                }
                gc.setLineWidth((fAvg + 0.5));
                gc.setStroke(s.getColor());
                gc.strokeLine(d1.getX() * canvasScale.get(), d1.getY() * canvasScale.get(), d2.getX() * canvasScale.get(), d2.getY() * canvasScale.get());
            }
        }
        gc.setFill(new Color(0, 0, 1, 0.33));
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(10);
        gc.fillRect(selection.getX(), selection.getY(), selection.getWidth(), selection.getHeight());
    }

    private void resetCanvas(){
        canvas.getGraphicsContext2D().clearRect(0,0,canvas.getWidth(), canvas.getHeight());
        resizeCanvas();
        drawStrokes();
    }

    private void initializeSelector(){
        anchor = new Light.Point();
        selection = new Rectangle();
        selection.setFill(Color.TRANSPARENT);
        selection.setStroke(Color.BLACK); // border
        selection.setStrokeWidth(10);
        selection.getStrokeDashArray().add(10.0);

    }

    //Source: https://coderanch.com/t/689100/java/rectangle-dragging-image
    private void startSelection(MouseEvent event){
        System.out.println("Start Selection called");
        System.out.println("X:" + event.getX() + " Y:" + event.getY());
        anchor.setX(event.getX());
        anchor.setY(event.getY());
        selection.setX(event.getX());
        selection.setY(event.getY());
        selection.setStroke(Color.BLACK); // border
        selection.getStrokeDashArray().add(10.0);
    }

    //Source: https://coderanch.com/t/689100/java/rectangle-dragging-image
    private void moveSelection(MouseEvent event){
        selection.setWidth(Math.abs(event.getX() - anchor.getX()));
        selection.setHeight(Math.abs(event.getY() - anchor.getY()));
        selection.setX(Math.min(anchor.getX(), event.getX()));
        selection.setY(Math.min(anchor.getY(), event.getY()));
        System.out.println(selection);
        resetCanvas();
    }

    private void endSelection(MouseEvent event){
        System.out.println("End selection called");
        selection.setWidth(0);
        selection.setHeight(0);

        resetCanvas();
    }

    @Override
    public void update(ObservablePage sender) {
        p.registerStrokeObserver(this);
        resetCanvas();
    }

    @Override
    public void update(ObservableStroke sender) {
        resetCanvas();
    }
}
