package New.Controllers;
import New.Characteristics.Characteristic;
import New.Interfaces.Controller;
import New.util.Export.CSVExporter;
import New.util.Export.ExportConfig;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.Set;

public class ExportDialogController implements Controller {
    @FXML
    private ListView<String> view_participantsID;

    @FXML
    private ListView<String> view_timelineTopics;

    @FXML
    private ListView<Characteristic> view_characteristics;

    @FXML
    private ListView<String> selection_participantsID;

    @FXML
    private ListView<String> selection_timelineTopics;

    @FXML
    private ListView<Characteristic> selection_characteristics;

    MainSceneController callback_reference;

    public ExportDialogController() { }

    public void initialize(Set<String> timelineTags
            , Set<String> participantIDs
            , Set<Characteristic> characteristics
            , MainSceneController mainScene) {
        view_participantsID.getItems().addAll(participantIDs);
        view_timelineTopics.getItems().addAll(timelineTags);
        view_characteristics.getItems().addAll(characteristics);
        this.callback_reference = mainScene;
    }

    @FXML
    private void selectParticipant(){
        addIfNotPresent(selection_participantsID,view_participantsID.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void selectTopic(){
        addIfNotPresent(selection_timelineTopics,view_timelineTopics.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void selectCharacteristic(){
        addIfNotPresent(selection_characteristics,view_characteristics.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void deselectParticipant() {
        removeWithNullCheck(selection_participantsID);
    }

    @FXML
    private void deselectTopic() {
        removeWithNullCheck(selection_timelineTopics);
    }

    @FXML
    private void deselectCharacteristic() {
        removeWithNullCheck(selection_characteristics);
    }

    @FXML
    private void selectAllParticipants() {
        view_participantsID.getItems().forEach(item -> addIfNotPresent(selection_participantsID,item));
    }

    @FXML
    private void selectAllTopics() {
        view_timelineTopics.getItems().forEach(item -> addIfNotPresent(selection_timelineTopics,item));
    }

    @FXML
    private void selectAllCharacteristic() {
        view_characteristics.getItems().forEach(item -> addIfNotPresent(selection_characteristics,item));
    }

    @FXML
    private void deselectAllParticipants() {
        selection_participantsID.getItems().clear();
    }

    @FXML
    private void deselectAllTopics() {
        selection_timelineTopics.getItems().clear();
    }

    @FXML
    private void deselectAllCharacteristic() {
        selection_characteristics.getItems().clear();
    }

    @FXML
    private void cancelAction() {
        ((Stage)selection_participantsID.getScene().getWindow()).close();
    }

    @FXML
    private void exportAction() {
        callback_reference.exportWindowCallback(new CSVExporter(),
                new ExportConfig(selection_participantsID.getItems()
                        ,selection_timelineTopics.getItems()
                        ,selection_characteristics.getItems())
             );
    }

    private boolean addIfNotPresent(ListView listview, Object element) {
        for (Object item : listview.getItems()) {
            if(item.equals(element)){
                return false;
            }
        }
        listview.getItems().add(element);
        return true;
    }

    private boolean removeWithNullCheck(ListView listView){
        int index = listView.getSelectionModel().getSelectedIndex();
        if(index > -1){
            listView.getItems().remove(index);
            return true;
        }
        return false;
    }
}
