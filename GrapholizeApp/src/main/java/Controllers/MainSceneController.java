package Controllers;

import java.net.URL;
import java.util.*;

import Controls.Container.TimeLineContainer;
import Controls.Timeline.Pane.CommentTimeLinePane;
import Controls.Timeline.Pane.PressureTimeLinePane;
import Controls.Timeline.Pane.StrokeDurationTimeLinePane;
import Interfaces.Observable;
import Interfaces.Observer;
import Model.Entities.Dot;
import Model.Entities.Page;
import Model.Entities.Stroke;
import Observables.ObservableStroke;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import util.PageDataReader;

public class MainSceneController implements Observer {

    // location and resources will be automatically injected by the FXML loader
    @FXML
    private URL location;

    @FXML
    private ResourceBundle resources;

    private Page p;
    private List<ObservableStroke> observableStrokes;
    private float canvasScale = 10;
    private float canvasScalingStep = 5;

    private float timeLineScale = 0.05f;
    private float timeLineScalingStep = 0.05f;

    @FXML
    private ScrollPane scrollPane_TimeLines;

    @FXML
    private Canvas canvas_mainCanvas;

    private VBox timeLineContainer;

    private long totalDuration;

    @FXML
    public void initialize() throws Exception{
        System.out.println("aaa");
        p = loadThatShitBoy();
        initObservableStrokes(p.getStrokes());
        canvas_mainCanvas.setWidth(p.getPageMetaData().getPageWidth() * canvasScale);
        canvas_mainCanvas.setHeight(p.getPageMetaData().getPageHeight() * canvasScale);

        drawThatSHit();


        totalDuration = p.getStrokes().get(p.getStrokes().size() - 1).getTimeEnd() - p.getStrokes().get(0).getTimeStart();
        setUpTimeLines();
        setupTimelineContainer();
    }

    public MainSceneController(){

    }

    //Replace with openFileDialogue after testing.
    private Page loadThatShitBoy() throws Exception{
        String path = "src\\main\\resources\\data\\page.data";
        return PageDataReader.ReadPage(path);
    }

    private void initObservableStrokes(List<Stroke> strokes){
        observableStrokes = new ArrayList<>();
        for (Stroke s : strokes){
            observableStrokes.add(new ObservableStroke(s, this));
        }
    }

    private Color randomColor(){
        Random rand = new Random();
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        return new Color(r,g,b, 1);
    }

    private void drawThatSHit(){
        GraphicsContext gc = canvas_mainCanvas.getGraphicsContext2D();
        for(ObservableStroke s : observableStrokes){

            for(int i = 0; i < s.getDots().size() - 1; i++){

                Dot d1 = s.getDots().get(i);
                Dot d2 = s.getDots().get(i + 1);
                double fAvg = (d1.getForce() + d2.getForce()) / 2;

                //TODO: Make draw stroke specific (Use Decorator pattern for different visual filters
                if(s.isSelected()){
                    //gc.setLineWidth(((fAvg / 1000000000) + 10));
                    gc.setLineWidth((fAvg) + 2);
                    gc.setStroke(new Color(0,1, 0, 1));
                    gc.strokeLine(d1.getX() * canvasScale, d1.getY() * canvasScale, d2.getX() * canvasScale, d2.getY() * canvasScale);
                }

                //gc.setLineWidth(fAvg / 1000000000);
                gc.setLineWidth((fAvg + 0.5));
                gc.setStroke(new Color(s.getColor().getR(), s.getColor().getG(), s.getColor().getB(), 1));
                gc.strokeLine(d1.getX() * canvasScale, d1.getY() * canvasScale, d2.getX() * canvasScale, d2.getY() * canvasScale);
            }
        }
    }

    public void reDraw(){
        canvas_mainCanvas.getGraphicsContext2D().clearRect(0, 0, canvas_mainCanvas.getWidth(), canvas_mainCanvas.getHeight());
        drawThatSHit();
    }


    @FXML
    protected void handleScaleUpPress(ActionEvent e) {
        scaleUp(canvasScalingStep);
    }

    @FXML
    protected void handleScaleDownPress(ActionEvent e) {
        scaleDown(canvasScalingStep);
    }

    private void scaleUp(float step){
        if(canvasScale + step < 40){
            canvasScale += step;
        }
        else {
            canvasScale = 40;}
        canvas_mainCanvas.setWidth(p.getPageMetaData().getPageWidth() * canvasScale);
        canvas_mainCanvas.setHeight(p.getPageMetaData().getPageHeight() * canvasScale);
        reDraw();
    }

    private void scaleDown(float step){
        if(canvasScale -step > 1){
            canvasScale -= step;
        }
        else{
            canvasScale = 1;
        }
        canvas_mainCanvas.setWidth(p.getPageMetaData().getPageWidth() * canvasScale);
        canvas_mainCanvas.setHeight(p.getPageMetaData().getPageHeight() * canvasScale);
        reDraw();
    }

    private void setupTimelineContainer(){
        //scrollPane_TimeLines.setContent(timeLineContainer);
        TimeLineContainer tlc = new TimeLineContainer(totalDuration, timeLineScale);
        StrokeDurationTimeLinePane s = new StrokeDurationTimeLinePane("Stroke duration", totalDuration,  50, timeLineScale, observableStrokes);
        tlc.addTimeLine(s);
        tlc.addTimeLine(new StrokeDurationTimeLinePane("Stroke duration 2", totalDuration,  50, timeLineScale, observableStrokes));
        tlc.addTimeLine(new CommentTimeLinePane("Custom", totalDuration, 50, timeLineScale, Color.ROYALBLUE));
        tlc.addTimeLine(new PressureTimeLinePane("pressure", totalDuration, 50, timeLineScale, Color.PINK, observableStrokes, s));
        scrollPane_TimeLines.setContent(tlc);
    }

    private void setUpTimeLines(){
        timeLineContainer = new VBox();
        timeLineContainer.setSpacing(10);
        timeLineContainer.getChildren().add(new StrokeDurationTimeLinePane("Stroke duration", totalDuration,  50, timeLineScale, observableStrokes));
        timeLineContainer.getChildren().add(new CommentTimeLinePane("Custom", totalDuration, 50, timeLineScale, Color.ROYALBLUE));
    }

    public float getTimeLineScale(){return this.timeLineScale;}

    @Override
    public void update(Observable sender) {
        reDraw();
    }
}
