package New.CustomControls.Containers;

import New.Controllers.ContentSwitcherController;
import New.Interfaces.Observer.ProjectObserver;
import New.Observables.ObservablePage;
import New.Observables.ObservableParticipant;
import New.Observables.ObservableProject;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;

public class ContentSwitcher extends HBox implements ProjectObserver {

    private ObservableProject project;
    private ObservableParticipant participant;
    private ObservablePage page;

    private ContentSwitcherController contentSwitcherController;

    private ComboBox<String> comboBox_Participants;
    private ComboBox<Integer> comboBox_Pages;

    public ContentSwitcher(ObservableProject project, ObservableParticipant activeParticipant, ObservablePage activePage){
        project.addObserver(this);
        this.contentSwitcherController = new ContentSwitcherController(project, activeParticipant, activePage);

        this.project = project;
        this.participant = activeParticipant;
        this.page = activePage;

        this.comboBox_Participants = new ComboBox<>();
        this.comboBox_Pages = new ComboBox<>();


        this.comboBox_Participants.valueProperty().addListener((observable, oldValue, newValue) -> handleComboBoxParticipantChange(newValue));
        this.comboBox_Pages.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                handleComboBoxPageChange(newValue);
            }
        });

        initializeComboBoxes();
    }

    private void initializeComboBoxes(){
        this.getChildren().clear();
        initializeParticipants();
        //InitializeParticipants will implicitly also call IntitializePages
        this.getChildren().addAll(comboBox_Participants, comboBox_Pages);
    }

    private void initializeParticipants(){
        comboBox_Participants.getItems().clear();
        for(String s : project.getParticipantIDs()){
            comboBox_Participants.getItems().add(s);
        }
        comboBox_Participants.getSelectionModel().select(0);
    }

    private void initializePages(){
        comboBox_Pages.getItems().clear();
        for(int i = 0; i < participant.getNumberOfPages(); i++){
            comboBox_Pages.getItems().add(i + 1);
        }
        comboBox_Pages.getSelectionModel().select(0);

    }

    private void handleComboBoxParticipantChange(String newVal){
        contentSwitcherController.setParticipant(newVal);
        initializePages();
    }

    private void handleComboBoxPageChange(int newVal){
        //The internal indexing of pages is 0-based, however the combo box entries start at 1.
        //Therefore, to pick the right page when switching, newVal needs to be subtracted by 1.
        contentSwitcherController.setPage(newVal - 1);
    }

    @Override
    public void update(ObservableProject sender) {
        initializeComboBoxes();
    }
}
