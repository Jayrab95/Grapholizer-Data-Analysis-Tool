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
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
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

    Path raw_data_file;

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

    //TODO Lukas Replace with openFileDialogue after testing.
    private void loadThatShitBoy() throws Exception{
        raw_data_file = Path.of("src\\main\\resources\\data\\page.data");
        _session= new Session(new PageDataReader().load(raw_data_file.toString()));
    }

    @FXML
    private void loadRawJson(){
        loadDataFromFiles(new JsonLoader());
    }

    @FXML
    private void loadProjectZip() {
        ProjectLoader pLoader = new ProjectLoader();
        loadDataFromFiles(pLoader);
        raw_data_file = pLoader.getZipHelper().getPathTempData();
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
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose a directory");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Data Files",  "*.zip", "*.grapholizer")
            );
            Stage stage = new Stage();
            stage.setTitle("Project File Save");
            File sFile = fileChooser.showSaveDialog(stage);
            if (sFile != null) {
                String path = sFile.getCanonicalPath();
                _session.setZ_Helper(new ZipHelper(path));
                save();
            } else {
                throw new IOException("No directory or file has been entered");
            }
        }catch(IOException ex) {
            new DialogGenerator().simpleErrorDialog("Input Error"
                    , "Directory could not be loaded"
                    , "Either no directory was chosen or the directory can't be read opened" +
                            "or contains corrupted data");
        }
    }

    private void save() {
        try {
            ZipHelper zHelper = _session.getZ_Helper();
            zHelper.init();
            if (zHelper != null) {
                String content = new ProjectSerializer().serialize(_session.getActiveProject().getInner());
                zHelper.writeTimelines(content);
                zHelper.replaceTimelines();
            } else {
                new DialogGenerator().simpleErrorDialog("Save Error"
                        , "No Project File"
                        , "You have not defined a project folder for saving yet");
            }
        }catch(Exception e) {
            e.printStackTrace();
            new DialogGenerator().simpleErrorDialog("Save Error"
                    , "While writing the file an error occured"
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
        }
    }
}
