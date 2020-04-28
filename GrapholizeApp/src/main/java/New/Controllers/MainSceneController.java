package New.Controllers;

import New.Characteristics.*;
import New.CustomControls.Containers.ContentSwitcher;
import New.CustomControls.Containers.MainCanvas;
import New.CustomControls.Containers.SegmentationContainer;
import New.Dialogues.CSVExportDialog;
import New.Interfaces.*;
import New.Model.Entities.*;
import New.Model.Session;

import New.util.*;
import New.util.Export.ExportConfig;
import New.util.Export.JsonSerializer;
import New.util.Export.ProjectSerializer;
import New.util.Import.JsonLoader;
import New.util.Import.PageDataReader;
import New.util.Import.ProjectLoader;
import New.util.CharacteristicList;
import New.util.Import.model.CompressedParticipant;
import New.util.javafx.JavaFxUtil;
import New.Enums.DataRessourceType;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class MainSceneController {
    @FXML
    private AnchorPane scrollPane_TimeLines;

    @FXML
    private VBox anchorPane_canvasContainer;
    private Optional<MainCanvas> optionalCanvas;
    private Optional<ContentSwitcher> optionalContentSwitcher;
    private Optional<SegmentationContainer> optionalSegmentationContainer;

    /** Internal State Of Application */
    public Session _session;
    private Path raw_data_file;
    private DataRessourceType ressourceType = DataRessourceType.UNDEF;
    private final Set<Characteristic> characteristicList;


    public MainSceneController(){
        optionalCanvas = Optional.empty();
        optionalContentSwitcher = Optional.empty();
        optionalSegmentationContainer = Optional.empty();
        characteristicList = new HashSet<>();
        characteristicList.addAll(CharacteristicList.characteristicsExport());
    }

    @FXML
    private void exportDataToCSV(){
        try {
            if(ressourceType != DataRessourceType.UNDEF) {
                FXMLLoader loader = JavaFxUtil.openWindowReturnController(this.getClass(), "fxml/views/ExportDialog.fxml"
                        , "CSV-Export"
                        , 900, 450);
                ((CSVExportDialog) loader.getController()).initialize(
                        _session.getActiveProject(true).getTopicSets()
                        , _session.getActiveProject(false).getParticipantIDs()
                        , characteristicList
                        , this);
            }
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
            if(loadDataFromFiles(pLoader, "*.zip", "*.grapholizer")) {
                _session.setZ_Helper(pLoader.getZipHelper());
            }
        }catch(IOException ex) {
            new DialogGenerator().simpleErrorDialog("Load Error"
                    , "While Loading Project File there was an IOError"
                    , ex.getMessage());
        }
    }

    @FXML
    private void loadNeoNotesFile() { loadDataFromFiles(new PageDataReader(), "*.data");}

    @FXML
    private void saveProject() {
        try{
            switch (ressourceType) {
                case JSON:
                case NEONOTES:
                    throw new IOException("There is no Project defined yet. " +
                            "Create a Project to save your progress to");
                case PROJECT:
                    saveProjectData();
                    break;
            }
        } catch(IOException ex) {
            new DialogGenerator().simpleErrorDialog("Saving Error"
                    , "There has been an IO-Error during saving"
                    , ex.getMessage());
        }
    }

    @FXML
    private void saveProjectTo() {
        try{
            File sFile = JavaFxUtil.openFileDialog("Save Dialog"
                    , "Save Project"
                    , true
                    , "*.zip", "*.grapholizer");
            if(sFile != null ) {
                switch (ressourceType) {
                    case JSON:
                        turnJsonToProject(sFile);
                        break;
                    case NEONOTES:
                        turnNeoNotesToProject(sFile);
                        break;
                    case PROJECT:
                        copyProjectToAnotherLocation(sFile);
                        break;
                    default:
                        throw new IOException("No directory or file has been entered");
                }
            }else {
                throw new IOException("The chosen file/directory " +
                        "can not be read or none has been chosen");
            }
        } catch( IOException ex ){
            new DialogGenerator().simpleErrorDialog("Saving Error"
                    , "There has been an IO-Error during saving"
                    , ex.getMessage());
        }
    }

    private void saveProjectData() {
        try {
            ZipHelper zHelper = _session.getZ_Helper();
            if (zHelper != null) {
                String content = new ProjectSerializer().serialize(_session.getActiveProject(true).getInner());
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
     * @param fileFilters the file endings allowed for this kind of export.
     */
    private void export(IExporter exporter, ExportConfig config, String ... fileFilters) {
        try {
            File sFile = JavaFxUtil.openFileDialog("Export Data"
                    , "Export"
                    , true
                    , fileFilters);
            exporter.export(sFile.getAbsolutePath(), _session.getActiveProject(true).getInner(), config);
        }catch(IOException ex) {
            new DialogGenerator().simpleErrorDialog("Failed to Export"
                    , "During writing the file an error occured"
                    , ex.getMessage());
        }
    }

    private boolean loadDataFromFiles(Loader loader, String ... fileExtensions) {
        try {
            File sFile = JavaFxUtil.openFileDialog("Load Dialog"
                    , "Load Data"
                    , false
                    , fileExtensions);
            if (sFile != null) {
                //Clean old project before loading the new one
                if(ressourceType == DataRessourceType.PROJECT) _session.getZ_Helper().cleanUp();

                String absFilePath = sFile.getAbsolutePath();
                if(_session == null){
                    _session = new Session(loader.load(absFilePath));
                }else {
                    _session.setProject(loader.load(absFilePath));
                }

                if( loader instanceof ProjectLoader){
                    raw_data_file = ((ProjectLoader) loader).getZipHelper().getPathTempData();
                }else {
                    raw_data_file = sFile.toPath();
                }
                ressourceType = loader.getRessourceType();
                initializeProject();
                return true;
            }
        }catch(IOException ex) {
            new DialogGenerator().simpleErrorDialog("Input Error"
                    , "File could not be loaded"
                    , ex.getMessage());
        }
        return false;
    }

    private void initializeProject(){
        PageMetaData pmd = _session.getActivePage().getPageMetaData();
        //These components only need to be initialized once.
        //If they are already present, they already handle the logic for updating their
        //content upon a project change
        if(optionalContentSwitcher.isEmpty()){
            optionalContentSwitcher = Optional.of(new ContentSwitcher(_session.getActiveProject(false),_session.getActiveParticipant(), _session.getActivePage()));
            anchorPane_canvasContainer.getChildren().add(optionalContentSwitcher.get());
        }

        if(optionalSegmentationContainer.isEmpty()){
            optionalSegmentationContainer = Optional.of(new SegmentationContainer(_session.getActiveProject(false), _session.getActivePage(), 0.2));
            scrollPane_TimeLines.getChildren().add(optionalSegmentationContainer.get());
        }

        if(optionalCanvas.isEmpty()){
            optionalCanvas = Optional.of(new MainCanvas(pmd.getPageWidth(), pmd.getPageHeight(), 5, _session.getActivePage(), optionalSegmentationContainer.get().getSelectedSegmentation()));
            anchorPane_canvasContainer.getChildren().add(optionalCanvas.get());
        }
    }

    private void turnJsonToProject(File sFile) throws IOException {
        String path = sFile.getCanonicalPath();
        _session.setZ_Helper(new ZipHelper(path, false));

        //copy json
        String json = copyJsonFile(raw_data_file);

        //write the json and replace the old one
        _session.getZ_Helper().writeRawData(json);
        _session.getZ_Helper().replaceData();
        saveProjectData();

        //Define the used ressource type as a project
        ressourceType = DataRessourceType.PROJECT;
    }

    private void turnNeoNotesToProject(File sFile) throws IOException {
        String path = sFile.getCanonicalPath();

        //Create a json string out of neonotes data
        Collection<Participant> participants = _session.getActiveProject(true).getInner().getAllParticipants();
        LinkedList<CompressedParticipant> cParts = new LinkedList<>();
        for (Participant participant : participants) {
            cParts.add(new CompressedParticipant(participant));
        }
        String json = new JsonSerializer().serialize(cParts);

        //Write data to temp file
        _session.setZ_Helper(new ZipHelper(path, false));
        _session.getZ_Helper().writeRawData(json);
        _session.getZ_Helper().replaceData();
        saveProjectData();

        //Set ressource type
        ressourceType = DataRessourceType.PROJECT;
    }

    private void copyProjectToAnotherLocation(File sFile) throws IOException{
        //Initialisierung
        String newFilePath = sFile.getCanonicalPath();
        Path pathTempData = _session.getZ_Helper().getPathTempData();
        Path pathTempTimelines = _session.getZ_Helper().getPathTempTimelines();
        //copy timelines json
        String dataJson = copyJsonFile(pathTempData);
        //copy data.json
        String timelineJson = copyJsonFile(pathTempTimelines);

        //clean up old archive & create new archive
        _session.getZ_Helper().cleanUp();
        _session.setZ_Helper(new ZipHelper(newFilePath, false));
        //Write the data to the new Zipfolder and replace it
        _session.getZ_Helper().writeRawData(dataJson);
        _session.getZ_Helper().replaceData();
        //Write the timelines to the new Zipfolder and replace it
        _session.getZ_Helper().writeTimelines(timelineJson);
        _session.getZ_Helper().replaceTimelines();
        //save project
        saveProject();
    }

    private String copyJsonFile(Path path) throws IOException {
        StringBuilder sBuilder = new StringBuilder();
        Files.newBufferedReader(path).lines().forEach(l -> sBuilder.append(l));
        return sBuilder.toString();
    }
}
