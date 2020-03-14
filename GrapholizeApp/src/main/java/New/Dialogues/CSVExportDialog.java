package New.Dialogues;
import New.Characteristics.Characteristic;
import New.Controllers.MainSceneController;
import New.Model.Entities.SuperSet;
import New.util.DialogGenerator;
import New.util.Export.CSVExporter;
import New.util.Export.ExportConfig;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CSVExportDialog {
    @FXML
    private ListView<String> view_participantsID;

    @FXML
    private ListView<SuperSet> view_timelineTopics;

    @FXML
    private ListView<Characteristic> view_characteristics;

    @FXML
    private ListView<String> selection_participantsID;

    @FXML
    private ListView<SuperSet> selection_timelineTopics;

    @FXML
    private ListView<Characteristic> selection_characteristics;

    MainSceneController callback_reference;

    public CSVExportDialog() { }

    public void initialize(List<SuperSet> superSets
            , Set<String> participantIDs
            , Set<Characteristic> characteristics
            , MainSceneController mainScene) {
        if(participantIDs.isEmpty() || superSets.isEmpty() || characteristics.isEmpty()){
            cancelAction();
            throw new NullPointerException();
        }
        view_participantsID.getItems().addAll(participantIDs);
        view_timelineTopics.getItems().addAll(superSets);
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

        if(isSelectionValid()) {
            cancelAction();
            callback_reference.exportWindowCallback(new CSVExporter(),
                    new ExportConfig(selection_participantsID.getItems()
                            , selection_timelineTopics.getItems().stream().map(t -> t.getTagID()).collect(Collectors.toList())
                            , selection_characteristics.getItems())
            );
        } else {
            new DialogGenerator().simpleErrorDialog("Input Error"
                    , "The current selection is not valid."
                    , "maybe you did not select an element for all three dialogs");
        }
    }

    /**
     * checks if any of the selected item lists is empty
     * @return boolean true if in all selections there is at least one element
     */
    private boolean isSelectionValid() {
        boolean topicsIsEmpty = selection_timelineTopics.getItems().isEmpty();
        boolean participantsIsEmpty = selection_participantsID.getItems().isEmpty();
        boolean characteristicsIsEmpty = selection_characteristics.getItems().isEmpty();

        if(participantsIsEmpty || topicsIsEmpty || characteristicsIsEmpty) return false;

        return true;
    }

    /**
     *
     * @param listview list view that the element should be added to
     * @param element element that should be added to the listview
     * @return boolean, true if adding the element was successful, ergo element was not already in the list
     */
    private boolean addIfNotPresent(ListView listview, Object element) {
        if(element == null) return false;
        for (Object item : listview.getItems()) {
            if(item.equals(element)){
                return false;
            }
        }
        listview.getItems().add(element);
        return true;
    }

    /**
     *
     * @param listView the list view with the selected index
     * @return boolean true if removing the element was successful, ergo (index > -1)
     */
    private boolean removeWithNullCheck(ListView listView){
        int index = listView.getSelectionModel().getSelectedIndex();
        if(index > -1){ //selected index is -1 if nothing is selected
            listView.getItems().remove(index);
            return true;
        }
        return false;
    }
}
