package New.Controllers;

import New.CustomControls.Containers.ContentSwitcher;
import New.CustomControls.Containers.MainCanvas;
import New.CustomControls.Containers.TimeLineContainer;
import New.Interfaces.*;
import New.Model.Entities.*;
import New.Model.Session;
import New.util.*;


import New.util.Export.JsonSerializer;
import New.util.Export.ProjectSerializer;
import New.util.Import.JsonLoader;
import New.util.Import.PageDataReader;
import New.util.Import.ProjectLoader;
import javafx.fxml.FXML;
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

    @FXML
    private AnchorPane scrollPane_TimeLines;

    @FXML
    private VBox anchorPane_canvasContainer;

    /* Internal State Of Application */
    Session _session;

    public MainSceneController(){

    }

    @FXML
    public void initialize() throws Exception{
        //loadThatShitBoy();
        _session = new Session(new JsonLoader().load("src\\main\\resources\\data\\lukas_test_1.json"));
        PageMetaData pmd = _session.getActivePage().getPageMetaData();
        anchorPane_canvasContainer.getChildren().addAll(
                new MainCanvas(pmd.getPageWidth(), pmd.getPageHeight(), 5, _session.getActivePage()),
                new ContentSwitcher(_session.getActiveProject(),_session.getActiveParticipant(), _session.getActivePage()));
        scrollPane_TimeLines.getChildren().add(new TimeLineContainer(_session.getActiveProject(), _session.getActivePage(), 0.05));

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
        loadDataFromFiles(pLoader);
        _session.setZ_Helper(pLoader.getZipHelper());
    }

    @FXML
    private void loadNeoNotesFile() { loadDataFromFiles(new PageDataReader());}

    @FXML
    private void saveProject() {
        save();
    }

    @FXML
    private void saveProjectTo() {
        //session.saveto
    }

    private void save() {
        try {
            ZipHelper zHelper = _session.getZ_Helper();
            if (zHelper != null) {
                //Serialize Timelines
                String content = new ProjectSerializer().serialize(_session.getActiveProject().getInner());
                zHelper.writeTimelines(content);
                //replace old timeline files in project folder with new ones in temp
            } else {
                new DialogGenerator().simpleErrorDialog("Save Error"
                        , "No Project File"
                        , "You have not defined a project folder for saving yet");
            }
        }catch(Exception e) {
            new DialogGenerator().simpleErrorDialog("Save Error"
                    , "While writing the file an Error Occured"
                    , "This might be a problem with the format of the file, or the file has been moved");
        }
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
                _session.setProject(loader.load(absFilePath));
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
