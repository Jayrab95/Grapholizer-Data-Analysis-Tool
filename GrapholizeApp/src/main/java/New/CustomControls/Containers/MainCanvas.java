package New.CustomControls.Containers;

import New.Interfaces.Observer.PageObserver;
import New.Interfaces.Observer.StrokeObserver;
import New.Model.Entities.Dot;
import New.Interfaces.Observable;
import New.Interfaces.Observer.Observer;
import New.Model.ObservableModel.ObservablePage;
import New.Model.ObservableModel.ObservableStroke;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ListChangeListener;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.List;

public class MainCanvas extends VBox implements PageObserver, StrokeObserver {
    private Slider scaleSlider;
    private ScrollPane canvasContainer;
    private final Canvas canvas;
    private final double canvasWidth;
    private final double canvasHeight;
    private DoubleProperty canvasScale;
    private ObservablePage p;


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
    }




    private void resetCanvas(){
        canvas.getGraphicsContext2D().clearRect(0,0,canvas.getWidth(), canvas.getHeight());
        resizeCanvas();
        drawStrokes();
    }

    /*
    //40 is the max scale. Anything above can cause a bug with the graphics object.
    //
    private void scaleUp(float step){
        if(canvasScale.get() + step < 40){
            canvasScale.get() += step;
        }
        else {
            canvasScale.get() = 40;}
        canvas.setWidth(canvasWidth* canvasScale.get());
        canvas.setHeight(canvasHeight * canvasScale.get());
        resetCanvas();
    }

    private void scaleDown(float step){
        if(canvasScale.get() -step > 1){
            canvasScale.get() -= step;
        }
        else{
            canvasScale.get() = 1;
        }
        canvas.setWidth(canvasWidth* canvasScale.get());
        canvas.setHeight(canvasHeight * canvasScale.get());
        resetCanvas();
    }

     */


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
