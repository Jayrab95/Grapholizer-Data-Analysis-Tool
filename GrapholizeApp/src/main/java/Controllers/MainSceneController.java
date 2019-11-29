package Controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;

import Controls.Container.TimeLineContainer;
import Controls.Timeline.Pane.CommentTimeLinePane;
import Controls.Timeline.Pane.PressureTimeLinePane;
import Controls.Timeline.Pane.StrokeDurationTimeLinePane;
import Interfaces.Loader;
import Interfaces.Observable;
import Interfaces.Observer;
import Model.Entities.*;
import Observables.ObservableStroke;
import com.google.gson.internal.bind.util.ISO8601Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import net.lingala.zip4j.exception.ZipException;
import org.w3c.dom.ls.LSOutput;
import util.*;

public class MainSceneController implements Observer {
    // location and resources will be automatically injected by the FXML loader
    @FXML
    private URL location;

    @FXML
    private ResourceBundle resources;

    /* Internal State Of Application */
    Session _session;
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


    public MainSceneController(){

    }

    @FXML
    public void initialize() throws Exception{
        System.out.println("aaa");
        Page current_page = _session.getCurrent_page();
        current_page = loadDataFromFiles(new ProjectLoader());
        initObservableStrokes(current_page.getStrokes());
        canvas_mainCanvas.setWidth(current_page.getPageMetaData().getPageWidth() * canvasScale);
        canvas_mainCanvas.setHeight(current_page.getPageMetaData().getPageHeight() * canvasScale);

        drawThatSHit();

        totalDuration = current_page.getStrokes().get(current_page.getStrokes().size() - 1).getTimeEnd() - current_page.getStrokes().get(0).getTimeStart();
        setUpTimeLines();
        setupTimelineContainer();
    }

    @FXML
    private void loadRawJson(){
        loadDataFromFiles(new JsonLoader());
    }

    @FXML
    private void loadProjectZip() {
        ProjectLoader pLoader = new ProjectLoader();
        _session.setZ_Helper(pLoader.getZipHelper());
        loadDataFromFiles(pLoader);
    }

    @FXML
    private void loadNeoNotesFile() {
        loadDataFromFiles(new PageDataReader());
    }

    @FXML
    private void saveProject() {
        //Session.save
    }

    @FXML
    private void saveProjectTo() {
        //session.saveto
    }
    //Replace with openFileDialogue after testing.
    private Page loadDataFromFiles(Loader loader) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Data Files", "*.json", "*.zip", "*.data")
            );
            Stage stage = new Stage();
            stage.setTitle("Load File");
            File sFile = fileChooser.showOpenDialog(stage);
            if (sFile != null) {
                String absFilePath = sFile.getAbsolutePath();
                _session.setParticipantDataMap(loader.load(absFilePath));
                //TODO Sess set participants
                String key = _session.getParticipantDataMap().keySet().iterator().next();
                return _session.getParticipantDataMap().get(key).getPage(0);
            }
            return null;
        }catch(IOException ex) {
            new DialogGenerator().simpleErrorDialog("Input Error"
                    , "File could not be loaded"
                    , "The File you tried to open might not be in the right format or " +
                            "or contains corrupted data");
            System.out.println("File could not be loaded");
        }
        return null;
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
        Page current_page = _session.getCurrent_page();
        canvas_mainCanvas.setWidth(current_page.getPageMetaData().getPageWidth() * canvasScale);
        canvas_mainCanvas.setHeight(current_page.getPageMetaData().getPageHeight() * canvasScale);
        reDraw();
    }

    private void scaleDown(float step){
        if(canvasScale -step > 1){
            canvasScale -= step;
        }
        else{
            canvasScale = 1;
        }
        Page current_page = _session.getCurrent_page();
        canvas_mainCanvas.setWidth(current_page.getPageMetaData().getPageWidth() * canvasScale);
        canvas_mainCanvas.setHeight(current_page.getPageMetaData().getPageHeight() * canvasScale);
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
