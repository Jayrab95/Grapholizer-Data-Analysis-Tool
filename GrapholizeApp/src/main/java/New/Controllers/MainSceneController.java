package New.Controllers;

import New.CustomControls.Containers.ContentSwitcher;
import New.CustomControls.Containers.MainCanvas;
import New.CustomControls.Containers.TimeLineContainer;
import New.Interfaces.*;
import New.Model.Entities.*;
import New.Model.Session;
import New.util.*;


import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
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
    private AnchorPane scrollPane_TimeLines;

    @FXML
    private VBox anchorPane_canvasContainer;


    public MainSceneController(){

    }

    @FXML
    public void initialize() throws Exception{
        /*
        ProjectLoader loader = new ProjectLoader();
        loadThatShitBoy();
        _session = new Session(new JsonLoader().load("src\\main\\resources\\data\\lukas_test_1.json"));
        PageMetaData pmd = _session.getActivePage().getPageMetaData();
        _session.setZ_Helper(loader.getZipHelper());
        anchorPane_canvasContainer.getChildren().addAll(
                new MainCanvas(pmd.getPageWidth(), pmd.getPageHeight(), 5, _session.getActivePage()),
                new ContentSwitcher(_session.getActiveProject(),_session.getActiveParticipant(), _session.getActivePage()));
        scrollPane_TimeLines.getChildren().add(new TimeLineContainer(_session.getActiveProject(), _session.getActivePage(), 0.05));

         */
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
                temp();
            }

        }catch(IOException ex) {
            new DialogGenerator().simpleErrorDialog("Input Error"
                    , "File could not be loaded"
                    , "The File you tried to open might not be in the right format or " +
                            "or contains corrupted data");
            System.out.println("File could not be loaded");
        }

    }

    void temp(){
        //_session = new Session(new JsonLoader().load("src\\main\\resources\\data\\lukas_test_1.json"));
        PageMetaData pmd = _session.getActivePage().getPageMetaData();
        //_session.setZ_Helper(loader.getZipHelper());
        anchorPane_canvasContainer.getChildren().addAll(
                new MainCanvas(pmd.getPageWidth(), pmd.getPageHeight(), 5, _session.getActivePage()),
                new ContentSwitcher(_session.getActiveProject(),_session.getActiveParticipant(), _session.getActivePage()));
        scrollPane_TimeLines.getChildren().add(new TimeLineContainer(_session.getActiveProject(), _session.getActivePage(), 0.05));
    }
}
