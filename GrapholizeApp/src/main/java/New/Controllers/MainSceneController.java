package New.Controllers;
import New.Characteristics.*;
import New.CustomControls.Containers.ContentSwitcher;
import New.CustomControls.Containers.MainCanvas;
import New.CustomControls.Containers.TimeLineContainer;
import New.Interfaces.*;
import New.Model.Entities.*;
import New.Model.Session;
import New.util.*;
import New.util.Export.ExportConfig;
import New.util.Export.ProjectSerializer;
import New.util.Import.JsonLoader;
import New.util.Import.PageDataReader;
import New.util.Import.ProjectLoader;
import New.util.CharacteristicList;

import New.util.javafx.JavaFxUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class MainSceneController {

    @FXML
    private URL location;

    @FXML
    private ResourceBundle resources;

    @FXML
    private AnchorPane scrollPane_TimeLines;

    @FXML
    private VBox anchorPane_canvasContainer;

    private Optional<MainCanvas> optionalCanvas;
    private Optional<ContentSwitcher> optionalContentSwitcher;
    private Optional<TimeLineContainer> optionalTimeLineContainer;

    /** Internal State Of Application */
    public Session _session;

    Path raw_data_file; //TODO Lukas there should be a better way than keeping it here

    final Set<Characteristic> characteristicList;

    public MainSceneController(){
        optionalCanvas = Optional.empty();
        optionalContentSwitcher = Optional.empty();
        optionalTimeLineContainer = Optional.empty();
        characteristicList = new HashSet<>();
        characteristicList.addAll(CharacteristicList.characteristicsExport());
    }

    @FXML
    private void exportDataToCSV(){
        try {
           FXMLLoader loader = JavaFxUtil.openWindowReturnController(this.getClass(),"fxml/views/ExportDialog.fxml"
                   , "CSV-Export"
                   ,800,400);
           if(raw_data_file == null) throw new NullPointerException();
           ((ExportDialogController) loader.getController()).initialize(
                     _session.getActiveProject().getTopicSets()
                    ,_session.getActiveProject().getParticipantIDs()
                    ,characteristicList
                    ,this);
        } catch (IOException e) {
            new DialogGenerator().simpleErrorDialog("Export Error"
                    , "While exporting an error occured."
                    , e.getMessage());
        } catch (NullPointerException e) {
            new DialogGenerator().simpleErrorDialog("Export Info"
                    , "There is no data to be exported"
                    , e.getMessage());
        }
    }

    public void exportWindowCallback(IExporter exporter, ExportConfig exportConfig){
        export(exporter, exportConfig, "*.csv");
    }

    @FXML
    private void loadRawJson(){
        loadDataFromFiles(new JsonLoader(), "*.json");
    }

    @FXML
    private void loadProjectZip() {
        try {
            ProjectLoader pLoader = new ProjectLoader();
            loadDataFromFiles(pLoader, "*.zip", "*.grapholizer");
            raw_data_file = pLoader.getZipHelper().getPathTempData();
            _session.setZ_Helper(pLoader.getZipHelper());
        }catch(IOException ex) {
            new DialogGenerator().simpleErrorDialog("Load Error"
                    , "While temp-file cleanup an error occured."
                    , ex.getMessage());
        }
    }

    @FXML
    private void loadNeoNotesFile() { loadDataFromFiles(new PageDataReader(), "*.data");}

    @FXML
    private void saveProject() { save();}

    @FXML
    private void saveProjectTo() {
        try {
            File sFile = openFileDialog("Save Dialog"
                    , "Save Project"
                    , true
                    , "*.zip", "*.grapholizer");
            if (sFile != null && raw_data_file != null) {
                String path = sFile.getCanonicalPath();
                _session.setZ_Helper(new ZipHelper(path, false));

                StringBuilder sBuilder = new StringBuilder();
                Files.newBufferedReader(raw_data_file).lines().forEach(l -> sBuilder.append(l));
                _session.getZ_Helper().writeRawData(sBuilder.toString());
                _session.getZ_Helper().replaceData();

                save();
            } else {
                throw new IOException("No directory or file has been entered");
            }
        }catch(IOException ex) {
            new DialogGenerator().simpleErrorDialog("Input Error"
                    , "Directory could not be loaded"
                    , ex.getMessage());
        }
    }

    private void save() {
        try {
            ZipHelper zHelper = _session.getZ_Helper();
            if (zHelper != null) {
                String content = new ProjectSerializer().serialize(_session.getActiveProject().getInner());
                zHelper.writeTimelines(content);
                zHelper.replaceTimelines();
            } else {
                throw new IOException("You have not yet saved to a project folder");
            }
        }catch(Exception e) {
            new DialogGenerator().simpleErrorDialog("Save Error"
                    , "While writing the file an error occured"
                    , e.getMessage());
        }
    }

    /**
     *
     * @param exporter The exporter that implements the export algorithm
     * @param config The subset that is going to be exported
     */
    private void export(IExporter exporter, ExportConfig config, String ... filefilters) {
        try {
            File sFile = openFileDialog("Export Data"
                    , "Export"
                    , true
                    , filefilters);
            exporter.export(sFile.getAbsolutePath(), _session.getActiveProject().getInner(), config);
        }catch(IOException ex) {
            new DialogGenerator().simpleErrorDialog("Failed to Export"
                    , "During writing the file an error occured"
                    , ex.getMessage());
        }
    }

    private void loadDataFromFiles(Loader loader, String ... fileExtensions) {
        try {
            File sFile = openFileDialog("Load Dialog"
                    , "Load Data"
                    , false
                    , fileExtensions);
            if (sFile != null) {
                String absFilePath = sFile.getAbsolutePath();
                if(_session == null){
                    _session = new Session(loader.load(absFilePath));
                }else {
                    _session.setProject(loader.load(absFilePath));
                }

                if( loader instanceof ProjectLoader){
                    raw_data_file = ((ProjectLoader) loader).getZipHelper().getPathTempData();
                }else {
                    raw_data_file = Path.of(absFilePath);
                }
                initializeProject();
            }
        }catch(IOException ex) {
            new DialogGenerator().simpleErrorDialog("Input Error"
                    , "File could not be loaded"
                    , ex.getMessage());
        }
    }

    /**
     *
     * @param windowTitle The title of the newly opened window
     * @param chooserTitle The title of the file chooser
     * @param isSaveDialog true if this it is indended to save the file
     * @param fileFilters A list of String representing the allowed file endings
     * @return Returns the chosen file or null if no file was picked
     */
    private File openFileDialog(String windowTitle , String chooserTitle, boolean isSaveDialog, String ... fileFilters) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(chooserTitle);
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Supported Formats", fileFilters)
        );
        Stage stage = new Stage();
        stage.setTitle(windowTitle);
        File sFile;
        if(isSaveDialog) sFile = fileChooser.showSaveDialog(stage);
        else sFile = fileChooser.showOpenDialog(stage);
        return sFile;
    }


    void initializeProject(){
        //_session = new Session(new JsonLoader().load("src\\main\\resources\\data\\lukas_test_1.json"));
        PageMetaData pmd = _session.getActivePage().getPageMetaData();
        //_session.setZ_Helper(loader.getZipHelper());
        if(optionalContentSwitcher.isEmpty()){
            System.out.println("new switcher");
            optionalContentSwitcher = Optional.of(new ContentSwitcher(_session.getActiveProject(),_session.getActiveParticipant(), _session.getActivePage()));
            anchorPane_canvasContainer.getChildren().add(optionalContentSwitcher.get());
        }
        if(optionalCanvas.isEmpty()){
            System.out.println("new canvas");
            optionalCanvas = Optional.of(new MainCanvas(pmd.getPageWidth(), pmd.getPageHeight(), 5, _session.getActivePage()));
            anchorPane_canvasContainer.getChildren().add(optionalCanvas.get());
        }
        if(optionalTimeLineContainer.isEmpty()){
            System.out.println("new container");
            optionalTimeLineContainer = Optional.of(new TimeLineContainer(_session.getActiveProject(), _session.getActivePage(), 0.2));
            scrollPane_TimeLines.getChildren().add(optionalTimeLineContainer.get());
        }
    }
}
