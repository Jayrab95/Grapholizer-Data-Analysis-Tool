package New.Controllers;

import New.CustomControls.MainCanvas;
import New.CustomControls.TimeLineContainer;
import New.Interfaces.*;
import New.Model.Entities.*;
import New.Model.Session;
import New.util.*;


import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class MainSceneController {
    // location and resources will be automatically injected by the FXML loader
    @FXML
    private URL location;

    @FXML
    private ResourceBundle resources;

    /* Internal State Of Application */
    Session _session;

    @FXML
    private ScrollPane scrollPane_TimeLines;

    @FXML
    private Canvas canvas_mainCanvas;

    @FXML
    private AnchorPane anchorPane_canvasContainer;

    //private TimeLineContainer timeLineContainer;



    public MainSceneController(){

    }

    @FXML
    public void initialize() throws Exception{

        ProjectLoader loader = new ProjectLoader();
        loadThatShitBoy();
        PageMetaData pmd = _session.getActivePage().getPageMetaData();
        _session.setZ_Helper(loader.getZipHelper());
        anchorPane_canvasContainer.getChildren().add(new MainCanvas(pmd.getPageWidth(), pmd.getPageHeight(), 10, _session.getActivePage()));
        scrollPane_TimeLines.setContent(new TimeLineContainer(_session.getActiveProject(), _session.getActivePage(), 0.05));

    }

    //Replace with openFileDialogue after testing.
    private void loadThatShitBoy() throws Exception{
        String path = "src\\main\\resources\\data\\page.data";
        _session= new Session(new PageDataReader().load(path));
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
    private void loadDataFromFiles(Loader loader) {
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
                _session = new Session(loader.load(absFilePath));
            }

        }catch(IOException ex) {
            new DialogGenerator().simpleErrorDialog("Input Error"
                    , "File could not be loaded"
                    , "The File you tried to open might not be in the right format or " +
                            "or contains corrupted data");
            System.out.println("File could not be loaded");
        }

    }
}
