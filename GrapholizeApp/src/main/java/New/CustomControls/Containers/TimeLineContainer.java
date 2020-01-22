package New.CustomControls.Containers;

import New.Controllers.TimeLineContainerController;
import New.CustomControls.TimeLine.CustomTimeLinePane;
import New.CustomControls.TimeLine.StrokeDurationTimeLinePane;
import New.CustomControls.TimeLine.TimeLinePane;
import New.CustomControls.Annotation.AnnotationRectangle;
import New.Execptions.NoTimeLineSelectedException;
import New.Execptions.TimeLineTagException;
import New.Interfaces.Observer.Observer;
import New.Model.Entities.Annotation;
import New.Observables.*;
import New.util.DialogGenerator;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class TimeLineContainer extends VBox {

    //region static strings
    private final static String TXT_TL_CREATION_TITLE = "Create a new timeline";
    private final static String TXT_TL_CREATION_HEADER = "Creation of a new timeline";
    private final static String TXT_TL_CREATION_TEXT = "Create a new timeline by entering a tag. The tag must be unique and cannot be empty.";
    private final static String TXT_TL_TIMELINETAG_LABEL = "Timeline tag:";
    private final static String TXT_TL_TAG_DEFAULTVAL = "New Timeline";
    private final static Color COLOR_TL_TAG_DEFAULTVAL = Color.CADETBLUE;

    private final static String TXT_TL_FILTER_TITLE = "Filter for annotations";
    private final static String TXT_TL_FILTER_HEADER = "Filter for annotations with a specific text";
    private final static String TXT_TL_FILTER_TEXT= "You may search other annotation sets on this page for annotations with a specific text."
            + "\nYou may select a topic and filter the annotation set of this topic for annotations with the given text."
            + "\nIf you leave the checkbox unselected, an empty annotation set will be created.";


    private final static String TXT_TL_EDIT_TITLE = "Edit timeline";
    private final static String TXT_TL_EDIT_HEADER = "Editing a timeline";
    private final static String TXT_TL_EDIT_TEXT = "Change the name of the timeline";

    private final static String TXT_TL_DELETE_TITLE = "Delete timeline";
    private final static String TXT_TL_DELETE_HEADER = "Deletion of a timeline.";
    private final static String TXT_TL_DELETE_TEXT = "Are you sure you want to delete the timeline %s? \nWARNING: Deleting a timeline will remove it and all annotations associated with it from ALL participants and all their pages.";

    private final static String TXT_TL_CREATION_ERROR_TITLE = "Timeline creation error";
    private final static String TXT_TL_CREATION_ERROR_HEADER = "Error while creating timeline";
    //endregion

    private DoubleProperty totalWidth;
    private double timeLinesHeight = 50;
    private DoubleProperty scale;

    private ObservableTimeLine selectedTimeLine;
    private ObservablePage p;

    private TimeLineContainerController timeLineContainerController;

    private ScrollPane scrollPane_timeLineScrollPane;
    private VBox vBox_TimeLineBox;

    private Button btn_CreateNewTimeLine;
    private Button btn_CreateNewTimeLineOutOfSelected;
    private HBox hbox_buttonHBox;

    private Slider scaleSlider;

    public TimeLineContainer(ObservableProject project, ObservablePage page, double initialScale){

        this.p = page;

        page.getPageProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("TimeLineContainer has detected a page change");
            InitializeContainer(project, page);
        });

        AnchorPane.setBottomAnchor(this, 0.0);
        AnchorPane.setLeftAnchor(this, 0.0);
        AnchorPane.setRightAnchor(this, 0.0);
        AnchorPane.setTopAnchor(this, 0.0);

        timeLineContainerController = new TimeLineContainerController(project, page);

        totalWidth = new SimpleDoubleProperty(page.getDuration());
        scale = new SimpleDoubleProperty(initialScale);

        scaleSlider = initializeSlider(initialScale);
        scale.bind(scaleSlider.valueProperty());

        vBox_TimeLineBox = new VBox();
        vBox_TimeLineBox.setPadding(new Insets(5, 0, 10, 0));
        scrollPane_timeLineScrollPane = new ScrollPane();
        scrollPane_timeLineScrollPane.setContent(vBox_TimeLineBox);

        InitializeButtonHBox();
        InitializeContainer(project, page);
    }

    private void InitializeButtonHBox(){
        btn_CreateNewTimeLine = new Button("Create new timeline");
        btn_CreateNewTimeLine.setOnAction(event -> createNewTimeLine());
        btn_CreateNewTimeLineOutOfSelected = new Button("Create new Timeline out of selected annotations");
        btn_CreateNewTimeLineOutOfSelected.setOnAction(event -> handleCreateTLOutOfSelectedClick());
        hbox_buttonHBox = new HBox(btn_CreateNewTimeLine, btn_CreateNewTimeLineOutOfSelected);
    }

    private void InitializeContainer(ObservableProject project, ObservablePage page){
        System.out.println("initialize container called");
        //Step 1: Create the stroke timeline
        //Step 2: For each tag, create a new timeline and pass over the observble Tag and the page. Then create the annotations.
        getChildren().clear();
        getChildren().add(scaleSlider);
        getChildren().add(hbox_buttonHBox);
        getChildren().add(scrollPane_timeLineScrollPane);

        vBox_TimeLineBox.getChildren().clear();

        StrokeDurationTimeLinePane strokePane = new StrokeDurationTimeLinePane(timeLineContainerController.getPage().getDuration(), timeLinesHeight, scale, timeLineContainerController.getPage(), this);
        addTimeLinePane(strokePane);

        //TODO: Observe if A: This actually works and B: if there are any memory leaks when chaning project.
        for(String s : project.getTimeLineTagNames()){
            ObservableTimeLineTag tag = project.getTimeLineTag(s);
            loadTimeLine(tag, page, page.getAnnotationSet(s));
        }

    }

    private Slider initializeSlider(double initScale){
        Slider slider = new Slider(0.001, 1, initScale);
        return slider;
    }

    public ObservableTimeLine getSelectedTimeLine() {
        if(selectedTimeLine == null){
            selectedTimeLine = new ObservableTimeLine();
        }
        return selectedTimeLine;
    }

    private void addTimeLinePane(TimeLinePane timeLinePane){
        vBox_TimeLineBox.getChildren().add(new TimeLineWrapper(timeLinePane));
    }

    private void loadTimeLine(ObservableTimeLineTag t, ObservablePage p, Optional<List<Annotation>> annotations){
        if(annotations.isPresent()){
            CustomTimeLinePane pane;
            Annotation[] array = annotations.get().stream().toArray(n -> new Annotation[n]);
            pane = new CustomTimeLinePane(totalWidth.get(), timeLinesHeight, scale, t, p, this, array);
            addTimeLinePane(pane);
        }
        else{
            addTimeLinePane(createNewTimeLinePane(t, p, Optional.empty()));
        }
    }

    public void createNewTimeLine(){
        Optional<TopicCreationDialogResult> tag = openTimeLineCreationDialog(
                TXT_TL_CREATION_TITLE,
                TXT_TL_CREATION_HEADER,
                TXT_TL_CREATION_TEXT,
                TXT_TL_TIMELINETAG_LABEL,
                TXT_TL_TAG_DEFAULTVAL,
                COLOR_TL_TAG_DEFAULTVAL,
                false);
        Optional<AnnotationFilterDialogResult> filtered = Optional.empty();
        if(timeLineContainerController.getTopics().size() > 0){
            filtered = filterForAnnotationsDialog(
                    TXT_TL_FILTER_TITLE,
                    TXT_TL_FILTER_HEADER,
                    TXT_TL_FILTER_TEXT);
        }
        if(tag.isPresent()){
            if(filtered.isPresent()){
                ObservableTimeLineTag newTag = timeLineContainerController.createNewTimeLineTag(tag.get().topicName, tag.get().topicColor);
                TimeLinePane timeLinePane = createNewTimeLinePaneOutOfAnnotations(newTag, timeLineContainerController.getPage(), filtered.get().getFilteredAnnotations());
                addTimeLinePane(timeLinePane);
            }
            else{
                ObservableTimeLineTag newTag = timeLineContainerController.createNewTimeLineTag(tag.get().topicName, tag.get().topicColor);
                TimeLinePane timeLinePane = createNewTimeLinePane(newTag, timeLineContainerController.getPage(),Optional.empty());
                addTimeLinePane(timeLinePane);
            }
        }
    }

    public void createNewTimeLineOutOfSelectedElements() throws NoTimeLineSelectedException {
        if(!selectedTimeLine.timeLineSelected()){
            throw new NoTimeLineSelectedException();
        }
        Optional<TopicCreationDialogResult> tag = openTimeLineCreationDialog(
                TXT_TL_CREATION_TITLE,
                TXT_TL_CREATION_HEADER,
                TXT_TL_CREATION_TEXT,
                TXT_TL_TIMELINETAG_LABEL,
                TXT_TL_TAG_DEFAULTVAL,
                COLOR_TL_TAG_DEFAULTVAL,
                false);
        if(tag.isPresent()){
            ObservableTimeLineTag newTag = timeLineContainerController.createNewTimeLineTag(tag.get().topicName, tag.get().topicColor);
            TimeLinePane timeLinePane = createNewTimeLineTagOutOfSelected(newTag, timeLineContainerController.getPage());
            addTimeLinePane(timeLinePane);
        }
    }

    public void createNewTimeLineOutOfSelectedDots() throws NoTimeLineSelectedException {
        Optional<TopicCreationDialogResult> tag = openTimeLineCreationDialog(
                TXT_TL_CREATION_TITLE,
                TXT_TL_CREATION_HEADER,
                TXT_TL_CREATION_TEXT,
                TXT_TL_TIMELINETAG_LABEL,
                TXT_TL_TAG_DEFAULTVAL,
                COLOR_TL_TAG_DEFAULTVAL,
                false);
        if(tag.isPresent()){
            ObservableTimeLineTag newTag = timeLineContainerController.createNewTimeLineTag(tag.get().topicName, tag.get().topicColor);
            TimeLinePane timeLinePane = createNewTimeLinePaneOutOfSelectedDots(newTag, timeLineContainerController.getPage());
            addTimeLinePane(timeLinePane);
        }
    }

    public void createNewTimeLineOutOfFilteredAnnotations(){
        Optional<AnnotationFilterDialogResult> filter;
        Optional<TopicCreationDialogResult> tag = openTimeLineCreationDialog(
                TXT_TL_CREATION_TITLE,
                TXT_TL_CREATION_HEADER,
                TXT_TL_CREATION_TEXT,
                TXT_TL_TIMELINETAG_LABEL,
                TXT_TL_TAG_DEFAULTVAL,
                COLOR_TL_TAG_DEFAULTVAL,
                false);
        if(tag.isPresent()){
            ObservableTimeLineTag newTag = timeLineContainerController.createNewTimeLineTag(tag.get().topicName, tag.get().topicColor);
            TimeLinePane timeLinePane = createNewTimeLinePaneOutOfSelectedDots(newTag, timeLineContainerController.getPage());
            addTimeLinePane(timeLinePane);
        }
    }

    public void createNewTimeLineOutOfCombinedElement(Annotation a){
        Optional<TopicCreationDialogResult> tag = openTimeLineCreationDialog(
                TXT_TL_CREATION_TITLE,
                TXT_TL_CREATION_HEADER,
                TXT_TL_CREATION_TEXT,
                TXT_TL_TIMELINETAG_LABEL,
                TXT_TL_TAG_DEFAULTVAL,
                COLOR_TL_TAG_DEFAULTVAL,
                false
        );
        if(tag.isPresent()){
            ObservableTimeLineTag newTag = timeLineContainerController.createNewTimeLineTag(tag.get().topicName, tag.get().topicColor);
            TimeLinePane timeLinePane = createNewTimeLinePaneOutOfCombined(newTag, timeLineContainerController.getPage(), a);
            addTimeLinePane(timeLinePane);
        }
    }

    private TimeLinePane createNewTimeLinePane(ObservableTimeLineTag tag, ObservablePage page, Optional<List<AnnotationRectangle>> annotations){
        CustomTimeLinePane newTimeLine = annotations.isPresent() ?
                new CustomTimeLinePane(timeLineContainerController.getPage().getDuration(), timeLinesHeight, scale, tag, page, this, annotations.get()) :
                new CustomTimeLinePane(timeLineContainerController.getPage().getDuration(), timeLinesHeight, scale, tag, page, this);
        return newTimeLine;
    }

    private TimeLinePane createNewTimeLineTagOutOfSelected(ObservableTimeLineTag newTimeLineTag, ObservablePage page){
        return createNewTimeLinePane(newTimeLineTag, page, Optional.of(selectedTimeLine.getSelectedAnnotations()));
    }

    private TimeLinePane createNewTimeLinePaneOutOfSelectedDots(ObservableTimeLineTag newTimeLineTag, ObservablePage page){
        List<Annotation> annotations = new LinkedList();
        for(List<ObservableDot> segment : page.getSelectedDotSegments()){
            annotations.add(new Annotation(
                    "Generated Annotation",
                    segment.get(0).getTimeStamp(),
                    segment.get(segment.size()-1).getTimeStamp()));
        }
        return new CustomTimeLinePane(timeLineContainerController.getPage().getDuration(), timeLinesHeight, scale, newTimeLineTag, page, this, annotations.toArray(new Annotation[annotations.size()]));
    }

    private TimeLinePane createNewTimeLinePaneOutOfCombined(ObservableTimeLineTag tag, ObservablePage page, Annotation annotation){
        CustomTimeLinePane newTimeLine = new CustomTimeLinePane(timeLineContainerController.getPage().getDuration(), timeLinesHeight, scale, tag, page, this, annotation);
        return newTimeLine;
    }

    private TimeLinePane createNewTimeLinePaneOutOfAnnotations(ObservableTimeLineTag tag, ObservablePage page, Annotation[] annotations){
        return new CustomTimeLinePane(timeLineContainerController.getPage().getDuration(), timeLinesHeight, scale, tag, page, this, annotations);
    }

    private void addTimeLineToChildren(TimeLinePane timeline){
        getChildren().remove(hbox_buttonHBox);
        getChildren().add(timeline);
        getChildren().add(hbox_buttonHBox);
    }

    public void editTimeLine(ObservableTimeLineTag oldTag){
        Optional<TopicCreationDialogResult> tag = openTimeLineCreationDialog(
                TXT_TL_EDIT_TITLE,
                TXT_TL_EDIT_HEADER,
                TXT_TL_EDIT_TEXT,
                TXT_TL_TIMELINETAG_LABEL,
                oldTag.getTag(),
                oldTag.getColor(),
                true);
        if (tag.isPresent()){
            timeLineContainerController.editTimeLineTag(oldTag.getTag(), tag.get().topicName, tag.get().topicColor);
            oldTag.setTag(tag.get().topicName);
            oldTag.setColor(tag.get().topicColor);
        }
    }

    public boolean removeTimeLine(CustomTimeLinePane timeLine){
        if(DialogGenerator.confirmationDialogue(
                TXT_TL_DELETE_TITLE,
                TXT_TL_DELETE_HEADER,
                String.format(TXT_TL_DELETE_TEXT)
        )){
            timeLineContainerController.removeTimeLine(timeLine.getTimeLineName());
            getChildren().remove(timeLine);
            return true;
        }
        return false;
    }

    private void handleCreateTLOutOfSelectedClick(){
        try{
            createNewTimeLineOutOfSelectedElements();
        }
        catch(NoTimeLineSelectedException ex){
            DialogGenerator.simpleErrorDialog(TXT_TL_CREATION_ERROR_TITLE, TXT_TL_CREATION_ERROR_HEADER, ex.toString());
        }
    }



    private Optional<AnnotationFilterDialogResult> filterForAnnotationsDialog(String title, String header, String text){
        CheckBox checkBox_filterForAnnotations = new CheckBox("Filter for annotations");
        ComboBox<String> comboBoxTopics = new ComboBox<>();
        TextField textBox_filterText = new TextField("");

        comboBoxTopics.disableProperty().bind(checkBox_filterForAnnotations.selectedProperty().not());
        textBox_filterText.disableProperty().bind(checkBox_filterForAnnotations.selectedProperty().not());

        comboBoxTopics.getItems().addAll(timeLineContainerController.getTopics());

        comboBoxTopics.getSelectionModel().select(0);

        Dialog<AnnotationFilterDialogResult> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(text);


        GridPane grid = new GridPane();
        grid.add(checkBox_filterForAnnotations, 0, 0);
        grid.add(comboBoxTopics, 0,1);
        grid.add(textBox_filterText, 0, 2);

        ButtonType buttonTypeOk = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(buttonTypeOk);

        dialog.setResultConverter(b -> {
            if (b == buttonTypeOk && checkBox_filterForAnnotations.isSelected()) {
                return new AnnotationFilterDialogResult(comboBoxTopics.getValue(), textBox_filterText.getText());
            }
            return null;
        });

        return dialog.showAndWait();
    }

    //region CreateNewTimeLineDialogue
    //https://code.makery.ch/blog/javafx-dialogs-official/
    //https://examples.javacodegeeks.com/desktop-java/javafx/dialog-javafx/javafx-dialog-example/
    //https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/Dialog.html#resultConverterProperty--
    //TODO: perhaps extract dialog into separate class
    //TODO: onlyCreate is a cheap solution to keep the dialog reusable. Perhaps there's a better solution?
    // Problem: The option to filter for other annotations on a specific timeline should only show up when creating a new timeline
    // and not when using any of the other options to create a timeline.
    // Possible solution: create a separate dialog only for the search/filtering of annotations, then open the regular create dialog.
    // Afterwards, use one of the creation methods that takes an annotation array.
    private Optional<TopicCreationDialogResult> openTimeLineCreationDialog(String dialogTitle, String dialogHeader, String dialogText, String labelText, String defaultValue, Color defaultColor, boolean editCall){
        /*
        This dialog is specific to the creation of timelines and contains some more complex logic
        specific to timelines and the TimelineContainer. Therefore it cannot be moved to DialogGenerator
         */
        Dialog<TopicCreationDialogResult> dialog = new Dialog<>();
        dialog.setTitle(dialogTitle);
        dialog.setHeaderText(dialogHeader);
        dialog.setContentText(dialogText);
        dialog.setResizable(true);

        TextField textBox_TimeLineTag = new TextField(defaultValue);
        ColorPicker colorPicker = new ColorPicker(defaultColor);



        GridPane grid = new GridPane();
        grid.add(new Label(labelText), 1, 1);
        grid.add(textBox_TimeLineTag, 2, 1);
        grid.add(new Label("Timeline color:"), 1, 2);
        grid.add(colorPicker,2,2 );

        ButtonType buttonTypeOk = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(buttonTypeOk, buttonTypeCancel);

        final Button okButton = (Button) dialog.getDialogPane().lookupButton(buttonTypeOk);

        okButton.addEventFilter(ActionEvent.ACTION, ae -> {
            String newTimeLineName = textBox_TimeLineTag.getText();
            //The if-block is entered if the dialog was opened by a creation command OR  if it's an edit command and
            //the old and new value are different.
            //If neither is true, it's an edit call where only the color of the timeline is edited and no name check is required.
            if(!editCall || !defaultValue.equals(newTimeLineName)) {
                try {
                    timeLineContainerController.checkIfTagIsValid(textBox_TimeLineTag.getText());
                } catch (TimeLineTagException ex) {
                    DialogGenerator.simpleErrorDialog(
                            TXT_TL_CREATION_ERROR_TITLE,
                            TXT_TL_CREATION_ERROR_HEADER,
                            ex.toString()
                    );
                    //Consume the event so that the dialog stays open.
                    ae.consume();
                }
            }
        });

        dialog.setResultConverter(b -> {
            if (b == buttonTypeOk) {
                return new TopicCreationDialogResult(textBox_TimeLineTag.getText(), colorPicker.getValue());
            }
            return null;
        });

        return dialog.showAndWait();
    }


    //region private classes

    private class TimeLineInformation extends VBox {
        private TimeLinePane tl;
        private HBox hBox_ButtonsContainer;
        private VBox vBox_UpDownButtonContainer;
        private VBox vBox_EditButtons;
        private HBox hBox_EditAndDeleteContainer;
        private Button btn_addNewTimeline;
        private Button btn_moveTimelineUp;
        private Button btn_moveTimelineDown;
        private Button btn_deleteTimeLine;
        private Button btn_editTimeLine;
        private Label lbl_timeLineName;

        private List<Observer> observers;


        public TimeLineInformation(TimeLinePane tl){
            this.tl = tl;

            InitializeButtons();
            setUpLabel();
            setupTimeLineInformation();

            setPrefWidth(120);
            setMaxWidth(120);
        }

        private void InitializeButtons(){
            btn_addNewTimeline = new Button("+");
            btn_moveTimelineUp = new Button("^");
            btn_moveTimelineDown = new Button("v");
            btn_deleteTimeLine = new Button("-");
            btn_editTimeLine = new Button("E");
        }

        private void setUpLabel(){
            lbl_timeLineName = new Label(tl.getTimeLineName());
            lbl_timeLineName.textProperty().bind(tl.getTimeLineNameProperty());
        }

        private void setupTimeLineInformation(){
            vBox_UpDownButtonContainer = new VBox(btn_moveTimelineUp, btn_moveTimelineDown);
            hBox_EditAndDeleteContainer = new HBox(btn_editTimeLine, btn_deleteTimeLine);
            vBox_EditButtons = new VBox(hBox_EditAndDeleteContainer, btn_addNewTimeline);
            hBox_ButtonsContainer = new HBox(vBox_UpDownButtonContainer, vBox_EditButtons);

            Button b = new Button("Detail view");
            b.setOnAction(event -> showDetailView());


            getChildren().addAll(lbl_timeLineName, b);
        }

        void showDetailView(){
            Stage stage = new Stage();
            stage.setTitle("My New Stage Title");
            stage.setScene(new Scene(new TimeLineDetailContainer(tl, timeLineContainerController.getPage()), 450, 450));
            stage.show();
        }
    }

    private class TimeLineWrapper extends HBox{
        private final TimeLinePane tl;
        private final TimeLineInformation tli;
        TimeLineWrapper(TimeLinePane tl){
            this.tl = tl;
            this.tli = new TimeLineInformation(tl);
            getChildren().addAll(tli, tl);
            setPadding(new Insets(5,0,5,0));
        }
        TimeLinePane getTimeLinePane(){return tl;}
        TimeLineInformation getTimelineInformation(){return tli;}
    }

    private class TopicCreationDialogResult {
        String topicName;
        Color topicColor;
        public TopicCreationDialogResult(String name, Color color){this.topicName = name; this.topicColor = color;}
    }

    private class AnnotationFilterDialogResult{
        String topic;
        String filterText;
        public AnnotationFilterDialogResult(String topic, String filterText){
            this.topic = topic;
            this.filterText = filterText;
        }
        Annotation[] getFilteredAnnotations(){
            return timeLineContainerController.getFilteredAnnotations(topic, filterText);
        }
    }

    //endregion


}
