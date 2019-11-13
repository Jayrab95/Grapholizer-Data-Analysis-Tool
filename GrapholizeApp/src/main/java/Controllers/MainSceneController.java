package Controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.ResourceBundle;

import Controls.Timeline.BasicStrokeTimeLine;
import Model.Dot;
import Model.Page;
import Model.Stroke;
import com.sun.javafx.geom.Line2D;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import util.PageDataReader;

public class MainSceneController {

    // location and resources will be automatically injected by the FXML loader
    @FXML
    private URL location;

    @FXML
    private ResourceBundle resources;

    private Page p;
    private float scale = 10;
    private float step = 5;

    @FXML
    private ScrollPane scrollPane_TimeLines;

    @FXML
    private Canvas canvas_mainCanvas;

    private VBox timeLineContainer;

    @FXML
    public void initialize() throws Exception{
        System.out.println("aaa");
        p = loadThatShitBoy();
        canvas_mainCanvas.setWidth(p.getPageMetaData().getPageWidth() * scale);
        canvas_mainCanvas.setHeight(p.getPageMetaData().getPageHeight() * scale);

        drawThatSHit();

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

    private Color randomColor(){
        Random rand = new Random();
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        return new Color(r,g,b, 1);
    }

    private void drawThatSHit(){
        GraphicsContext gc = canvas_mainCanvas.getGraphicsContext2D();
        for(Stroke s : p.getStrokes()){

            for(int i = 0; i < s.getDots().size() - 1; i++){

                Dot d1 = s.getDots().get(i);
                Dot d2 = s.getDots().get(i + 1);
                double fAvg = (d1.getForce() + d2.getForce()) / 2;

                if(s.isSelected()){
                    //gc.setLineWidth(((fAvg / 1000000000) + 10));
                    gc.setLineWidth(((fAvg) + 10));
                    gc.setStroke(Color.AQUAMARINE);
                    gc.strokeLine(d1.getX() * scale, d1.getY() * scale, d2.getX() * scale, d2.getY() * scale);
                }

                //gc.setLineWidth(fAvg / 1000000000);
                gc.setLineWidth(fAvg + 0.5);
                gc.setStroke(new Color(s.getColor().getR(), s.getColor().getG(), s.getColor().getB(), 1));
                gc.strokeLine(d1.getX() * scale, d1.getY() * scale, d2.getX() * scale, d2.getY() * scale);
            }
        }
    }

    public void reDraw(){
        canvas_mainCanvas.getGraphicsContext2D().clearRect(0, 0, canvas_mainCanvas.getWidth(), canvas_mainCanvas.getHeight());
        drawThatSHit();
    }


    @FXML
    protected void handleScaleUpPress(ActionEvent e) {
        scaleUp(step);
    }

    @FXML
    protected void handleScaleDownPress(ActionEvent e) {
        scaleDown(step);
    }

    private void scaleUp(float step){
        if(scale + step < 40){
            scale += step;
        }
        else {scale = 40;}
        canvas_mainCanvas.setWidth(p.getPageMetaData().getPageWidth() * scale);
        canvas_mainCanvas.setHeight(p.getPageMetaData().getPageHeight() * scale);
        reDraw();
    }

    private void scaleDown(float step){
        if(scale -step > 1){
            scale -= step;
        }
        else{scale = 1;}
        canvas_mainCanvas.setWidth(p.getPageMetaData().getPageWidth() * scale);
        canvas_mainCanvas.setHeight(p.getPageMetaData().getPageHeight() * scale);
        reDraw();
    }

    private void setupTimelineContainer(){
        scrollPane_TimeLines.setContent(timeLineContainer);
    }

    private void setUpTimeLines(){

        timeLineContainer = new VBox();
        timeLineContainer.setSpacing(10);
        timeLineContainer.getChildren().add(new BasicStrokeTimeLine(Arrays.asList(p.getStrokes()), 50, this));
        timeLineContainer.getChildren().stream().forEach(ch -> {
            System.out.println(ch.getClass().toString());
        });
    }
}