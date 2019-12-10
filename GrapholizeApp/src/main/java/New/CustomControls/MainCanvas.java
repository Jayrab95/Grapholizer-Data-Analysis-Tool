package New.CustomControls;

import Model.Entities.Dot;
import New.Controllers.CanvasController;
import New.Interfaces.Observable;
import New.Interfaces.Observer;
import New.Model.ObservableModel.ObservableActiveState;
import Observables.ObservableStroke;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class MainCanvas extends VBox implements Observer {
    private HBox hbox_Controls;
    private final Canvas canvas;
    private final double canvasWidth;
    private final double canvasHeight;
    private CanvasController canvasController;
    private double canvasScale;


    public MainCanvas(ObservableActiveState state, double initWidth, double initHeight, double initScale){
        this.canvasWidth = initWidth;
        this.canvasHeight = initHeight;
        this.canvasController = new CanvasController(state);
        this.canvasScale = initScale;
        canvas = new Canvas(initWidth, initHeight);
        initializeCanvas();
    }

    private void initializeCanvas(){

    }

    private void drawStrokes(){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        for(ObservableStroke s : canvasController.getStrokes()){

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
                gc.setStroke(new Color(s.getColor().getR(), s.getColor().getG(), s.getColor().getB(), 1));
                gc.strokeLine(d1.getX() * canvasScale, d1.getY() * canvasScale, d2.getX() * canvasScale, d2.getY() * canvasScale);
            }
        }
    }


    @Override
    public void update(Observable sender) {
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
