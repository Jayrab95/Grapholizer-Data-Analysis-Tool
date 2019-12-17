package New.CustomControls.Containers;

import New.Controllers.ContentSwitcherController;
import New.Interfaces.Observer.ProjectObserver;
import New.Model.ObservableModel.ObservablePage;
import New.Model.ObservableModel.ObservableParticipant;
import New.Model.ObservableModel.ObservableProject;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;

public class ContentSwitcher extends HBox implements ProjectObserver {

    private ObservableProject project;
    private ObservableParticipant participant;
    private ObservablePage page;

    private ContentSwitcherController contentSwitcherController;

    private ComboBox<String> participants;
    private ComboBox<Integer> pages;

    public ContentSwitcher(ObservableProject project, ObservableParticipant activeParticipant, ObservablePage activePage){
        project.addObserver(this);
        this.contentSwitcherController = new ContentSwitcherController(project, activeParticipant, activePage);

        this.project = project;
        this.participant = activeParticipant;
        this.page = activePage;

        this.participants = new ComboBox<>();
        this.pages = new ComboBox<>();


        this.participants.valueProperty().addListener((observable, oldValue, newValue) -> handleComboBoxParticipantChange(newValue));
        this.pages.valueProperty().addListener((observable, oldValue, newValue) -> handleComboBoxPageChange(newValue));

        initializeComboBoxes();
    }

    private void initializeComboBoxes(){
        this.getChildren().clear();
        initializeParticipants();
        //InitializeParticipants will implicitly also call IntitializePages
        this.getChildren().addAll(participants, pages);
    }

    private void initializeParticipants(){
        participants.getItems().clear();
        for(String s : project.getParticipantIDs()){
            participants.getItems().add(s);
        }
        participants.getSelectionModel().select(0);
    }

    private void initializePages(){
        pages.getItems().clear();
        for(int i = 0; i < participant.getNumberOfPages(); i++){
            pages.getItems().add(i + 1);
        }
        pages.getSelectionModel().select(0);
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
