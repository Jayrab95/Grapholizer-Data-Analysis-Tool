package New.CustomControls;

import New.Interfaces.Observer.PageObserver;
import New.Model.Entities.Dot;
import New.Interfaces.Observable;
import New.Interfaces.Observer.Observer;
import New.Model.ObservableModel.ObservablePage;
import New.Model.ObservableModel.ObservableStroke;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.List;

public class MainCanvas extends VBox implements PageObserver {
    private HBox hbox_Controls;
    private ScrollPane canvasContainer;
    private final Canvas canvas;
    private final double canvasWidth;
    private final double canvasHeight;
    private double canvasScale;
    private List<ObservableStroke> strokes;


    public MainCanvas(double initWidth, double initHeight, double initScale, ObservablePage obsPage){
        this.canvasWidth = initWidth;
        this.canvasHeight = initHeight;
        this.canvasScale = initScale;
        this.strokes = obsPage.getObservableStrokes();
        canvas = new Canvas(initWidth * canvasScale, initHeight * canvasScale);
        obsPage.addObserver(this);
        initializeCanvas();
    }

    private void initializeCanvas(){
        canvasContainer = new ScrollPane(canvas);
        getChildren().add(canvasContainer);
        drawStrokes();
    }

    private void drawStrokes(){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        for(ObservableStroke s : strokes){

            for(int i = 0; i < s.getDots().size() - 1; i++){

                Dot d1 = s.getDots().get(i);
                Dot d2 = s.getDots().get(i + 1);
                double fAvg = (d1.getForce() + d2.getForce()) / 2;
                //TODO: Make draw stroke specific (Use Decorator pattern for different visual filters
                if(s.isSelected()){
                    gc.setLineWidth((fAvg) + 2);
                    gc.setStroke(new Color(0,1, 0, 1));
                    gc.strokeLine(d1.getX() * canvasScale, d1.getY() * canvasScale, d2.getX() * canvasScale, d2.getY() * canvasScale);
                }
                gc.setLineWidth((fAvg + 0.5));
                gc.setStroke(s.getColor());
                gc.strokeLine(d1.getX() * canvasScale, d1.getY() * canvasScale, d2.getX() * canvasScale, d2.getY() * canvasScale);
            }
        }
    }


    @Override
    public void update(ObservablePage sender) {
        this.strokes = sender.getObservableStrokes();
        resetCanvas();
    }

    private void resetCanvas(){
        canvas.getGraphicsContext2D().clearRect(0,0,canvas.getWidth(), canvas.getHeight());
        drawStrokes();
    }

    private void scaleUp(float step){
        if(canvasScale + step < 40){
            canvasScale += step;
        }
        else {
            canvasScale = 40;}
        canvas.setWidth(canvasWidth* canvasScale);
        canvas.setHeight(canvasHeight * canvasScale);
        resetCanvas();
    }

    private void scaleDown(float step){
        if(canvasScale -step > 1){
            canvasScale -= step;
        }
        else{
            canvasScale = 1;
        }
        canvas.setWidth(canvasWidth* canvasScale);
        canvas.setHeight(canvasHeight * canvasScale);
        resetCanvas();
    }
}
