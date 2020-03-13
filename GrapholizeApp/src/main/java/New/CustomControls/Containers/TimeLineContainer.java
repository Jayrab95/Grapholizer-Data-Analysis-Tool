package New.CustomControls.Containers;

import New.Controllers.TimeLineContainerController;
import New.CustomControls.TimeLine.*;

import New.Dialogues.FilterForSegmentsDialog;
import New.Dialogues.TopicSetDialog;
import New.Execptions.NoTimeLineSelectedException;
import New.Execptions.TimeLineTagException;
import New.Interfaces.Observer.Observer;
import New.Model.Entities.Segment;
import New.Model.Entities.TopicSet;
import New.Observables.*;
import New.util.DialogGenerator;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.*;

public class TimeLineContainer extends ScrollPane {
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

    //buisiness logic attributes
    private DoubleProperty totalWidth;
    private double timeLinesHeight = 50;
    private DoubleProperty scale;

    private ObservableSegmentation selectedTimeLine;
    private ObservablePage p;

    private TimeLineContainerController timeLineContainerController;
    //endregion buisiness logic
    /**
     * the hash-map ensures that segmentations and their widgets stay connected to each other
     * for removal procedure
     */
    private HashMap<SegmentationPane, SegmentationInformation> segmentationWidgetLink = new HashMap<>();

    //graphical attributes
    private ScrollPane timelineScrollPane = new ScrollPane();
    private VBox timeLineVBox = new VBox();
    private VBox timelineInfoVBox = new VBox();
    private HBox containerTimelinesHBox = new HBox();
    private TimeUnitPane unitPane;

    private Button createNewTimeLineButton;
    private Button createNewTimeLineOutOfSelectedButton;
    private HBox buttonHBox;

    private Slider scaleSlider;

    private VBox mainContainer = new VBox();
    //endregion graphical attributes

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

        //Initialize Scale Slider
        scaleSlider = initializeSlider(initialScale);
        scale.bind(scaleSlider.valueProperty());
        //Units for timeline
        unitPane = new TimeUnitPane(scale,20,totalWidth);

        InitializeTimelineScrollPane();

        InitializeButtonHBox();

        containerTimelinesHBox.getChildren().addAll(timelineInfoVBox,timelineScrollPane);

        //add everything to parent
        mainContainer = new VBox();

        mainContainer.setMaxWidth(800);
        mainContainer.getChildren().addAll(scaleSlider,buttonHBox,containerTimelinesHBox);
        this.setHbarPolicy(ScrollBarPolicy.NEVER);
        this.setContent(mainContainer);

        InitializeContainer(project, page);

        this.setOnKeyPressed(event -> handleKeyStrokeEvent(event));

    }

    //TODO: Potential memory leak
    // Children are cleared but might still be listening to certain properties.
    private void InitializeContainer(ObservableProject project, ObservablePage page){
        System.out.println("initialize timeline_container called");
        getSelectedTimeLine().setSelectedTimeLine(null);
        //Step 1: Create the stroke timeline
        //Step 2: For each tag, create a new timeline and pass over the observble Tag and the page. Then create the annotations.
        //getChildren().clear();

        totalWidth.set(page.getDuration());
        //unitPane = new TimeUnitPane(scale,20,totalWidth);
        timeLineVBox.getChildren().clear();
        timelineInfoVBox.getChildren().clear();
        timeLineVBox.getChildren().add(unitPane);
        //TODO: POtential memory leak: Do generated segments still listen to external proprty?
        UnmodifiableSelectableSegmentationPane strokePane = new UnmodifiableSelectableSegmentationPane(
                totalWidth.get(),
                timeLinesHeight,
                scale,
                project.getObservableTopicSet(project.getStrokeSetID()),
                page,
                this
        );

        addTimeLinePane(strokePane);

        for(String topicSetID : project.getTopicSetIDs()){
            if(!topicSetID.equals(project.getStrokeSetID())){
                ObservableTopicSet tag = project.getObservableTopicSet(topicSetID);
                loadSegmentation(tag, page, page.getAnnotationSet(topicSetID));
            }
        }
        System.gc();
    }

    private void InitializeTimelineScrollPane() {
        timeLineVBox.setPadding(new Insets(0, 0, 0, 0));
        timeLineVBox.setSpacing(10d);
        timelineScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        timelineScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        timelineScrollPane.setContent(timeLineVBox);

        timelineInfoVBox.setSpacing(10d);
        timelineInfoVBox.setPadding(new Insets(50,0,0,10));
        timelineInfoVBox.setMinSize(100,timeLinesHeight);
    }


    private void InitializeButtonHBox(){
        createNewTimeLineButton = new Button("Create new timeline");
        createNewTimeLineButton.setOnAction(event -> createNewSegmentation());
        createNewTimeLineOutOfSelectedButton = new Button("Create new Timeline out of selected annotations");
        createNewTimeLineOutOfSelectedButton.setOnAction(event -> handleCreateTLOutOfSelectedClick());
        buttonHBox = new HBox(createNewTimeLineButton, createNewTimeLineOutOfSelectedButton);
    }

    private Slider initializeSlider(double initScale){
        Slider slider = new Slider(0.0d, 1, initScale);
        slider.setMajorTickUnit(0.05);
        slider.setMinorTickCount(0);
        slider.setShowTickMarks(true);
        slider.setSnapToTicks(true);
        return slider;
    }

    public ObservableSegmentation getSelectedTimeLine() {
        if(selectedTimeLine == null){
            selectedTimeLine = new ObservableSegmentation();
        }
        return selectedTimeLine;
    }

    private void addTimeLinePane(SegmentationPane segmentationPane){
        //Create new Toolbar for the Segmentation
        SegmentationInformation info = new SegmentationInformation(segmentationPane);
        timelineInfoVBox.getChildren().add(info);
        //Add the actual timeline
        timeLineVBox.getChildren().add(segmentationPane);
        segmentationWidgetLink.put(segmentationPane,info);
    }

    private void loadSegmentation(ObservableTopicSet t, ObservablePage p, Optional<Set<Segment>> annotations){
        if(annotations.isPresent()){
            CustomSegmentationPane pane;
            pane = new CustomSegmentationPane(totalWidth.get(), timeLinesHeight, scale, t, p, this);
            addTimeLinePane(pane);
        }
        else{
            addTimeLinePane(createNewSegmentationPane(t, p, false));
        }
    }


    public void createNewSegmentation(){
        Optional<TopicSet> tag = createTopicSetDialog();
        Optional<FilterForSegmentsDialog.FilterDialogResult> filtered = Optional.empty();
        //TODO: Until filter has been reworked, this functionality is offline
        /*
        if(timeLineContainerController.getTopicSetIDs().size() > 0){
            filtered = filterForAnnotationsDialog(
                    TXT_TL_FILTER_TITLE,
                    TXT_TL_FILTER_HEADER,
                    TXT_TL_FILTER_TEXT);
        }
         */
        if(tag.isPresent()){

            if(filtered.isPresent()){
                /*
                FilterForSegmentsDialog.FilterDialogResult res = filtered.get();

                ObservableTopicSet newTag = timeLineContainerController.createNewTimeLineTag(tag.get());
                TimeLinePane timeLinePane = createNewTimeLinePaneOutOfAnnotations(newTag, timeLineContainerController.getPage(), filtered.get().getFilteredAnnotations());
                addTimeLinePane(timeLinePane);

                 */
            }
            else{
                ObservableTopicSet newTag = timeLineContainerController.createNewTimeLineTag(tag.get());
                SegmentationPane segmentationPane = createNewSegmentationPane(newTag, timeLineContainerController.getPage(),false);
                addTimeLinePane(segmentationPane);
            }
        }
    }

    public void createNewSegmentationOutOfSelectedElements() throws NoTimeLineSelectedException {
        if(!selectedTimeLine.timeLineSelected()){
            throw new NoTimeLineSelectedException();
        }
        Optional<TopicSet> tag =createTopicSetDialog();
        if(tag.isPresent()){
            ObservableTopicSet newTag = timeLineContainerController.createNewTimeLineTag(tag.get());
            SegmentationPane segmentationPane = createNewSegmentationTagOutOfSelected(newTag, timeLineContainerController.getPage());
            addTimeLinePane(segmentationPane);
        }
    }

    /**
     * Creates a segmentation, that is the negative image of the given topicset on all pages of the participant
     * @param originSetID
     */
    public void createNegativeSegmentation(String originSetID){
        Optional<TopicSet> tag = createTopicSetDialog();
        if(tag.isPresent()){
            ObservableTopicSet newTag = timeLineContainerController.createNewTimeLineTag(tag.get());
            SegmentationPane segmentationPane = createNewTimeLinePaneOutOfAnnotations(newTag, p, p.getNegativeSegmentation(originSetID));
            addTimeLinePane(segmentationPane);
        }
    }

    public void createNewSegmentationOutOfSelectedDots() throws NoTimeLineSelectedException {
        Optional<TopicSet> tag = createTopicSetDialog();
        if(tag.isPresent()){
            ObservableTopicSet newTag = timeLineContainerController.createNewTimeLineTag(tag.get());
            SegmentationPane segmentationPane = createNewSegmentationPaneOutOfSelectedDots(newTag, timeLineContainerController.getPage());
            addTimeLinePane(segmentationPane);
        }
    }

    /*public void createNewTimeLineOutOfFilteredAnnotations(){
        Optional<AnnotationFilterDialogResult> filter;
        Optional<TopicSet> tag = createTopicSetDialog();
        if(tag.isPresent()){
            ObservableTopicSet newTag = timeLineContainerController.createNewTimeLineTag(tag.get());
            SegmentationPane segmentationPane = createNewSegmentationPaneOutOfSelectedDots(newTag, timeLineContainerController.getPage());
            addTimeLinePane(segmentationPane);
        }
    }*/

    public void createNewSegmentationOutOfSet(Set<Segment> a){
        Optional<TopicSet> tag = createTopicSetDialog();
        if(tag.isPresent()){
            ObservableTopicSet newTag = timeLineContainerController.createNewTimeLineTag(tag.get());
            SegmentationPane segmentationPane = createNewTimeLinePaneOutOfCombined(newTag, timeLineContainerController.getPage(), a);
            addTimeLinePane(segmentationPane);
        }
    }



    private SegmentationPane createNewSegmentationPane(ObservableTopicSet tag, ObservablePage page, boolean copyFromSelected){
        CustomSegmentationPane result;
        if(copyFromSelected){
            selectedTimeLine.getMissingTopics(tag).forEach(topic -> tag.addTopic(topic));
            result = new CustomSegmentationPane(timeLineContainerController.getPage().getDuration(), timeLinesHeight, scale, tag, page, this, selectedTimeLine.generateMissingSegments(tag.getTopicsObservableList()));
        }
        else{
            result = new CustomSegmentationPane(timeLineContainerController.getPage().getDuration(), timeLinesHeight, scale, tag, page, this);
        }
        return result;
    }

    private SegmentationPane createNewSegmentationTagOutOfSelected(ObservableTopicSet newTimeLineTag, ObservablePage page){
        return createNewSegmentationPane(newTimeLineTag, page, true);
    }

    private SegmentationPane createNewSegmentationPaneOutOfSelectedDots(ObservableTopicSet newTimeLineTag, ObservablePage page){
        Set<Segment> segments = new TreeSet<>();
        for(List<ObservableDot> segment : page.getSelectedDotSections()){
            segments.add(new Segment(
                    segment.get(0).getTimeStamp(),
                    segment.get(segment.size()-1).getTimeStamp()));
        }
        return new CustomSegmentationPane(timeLineContainerController.getPage().getDuration(), timeLinesHeight, scale, newTimeLineTag, page, this, segments);
    }

    private SegmentationPane createNewTimeLinePaneOutOfCombined(ObservableTopicSet tag, ObservablePage page, Set<Segment> segments){
        CustomSegmentationPane newTimeLine = new CustomSegmentationPane(timeLineContainerController.getPage().getDuration(), timeLinesHeight, scale, tag, page, this, segments);
        return newTimeLine;
    }

    private SegmentationPane createNewTimeLinePaneOutOfAnnotations(ObservableTopicSet tag, ObservablePage page, Set<Segment> segments){
        return new CustomSegmentationPane(timeLineContainerController.getPage().getDuration(), timeLinesHeight, scale, tag, page, this, segments);
    }

    public void editSegmentation(ObservableTopicSet oldTag){
        Optional<TopicSet> tag = editDialog(oldTag);
        if (tag.isPresent()){
            timeLineContainerController.editTimeLineTag(oldTag, tag.get());
        }
    }

    private Optional<TopicSet> createTopicSetDialog(){
        return openTimeLineCreationDialog(
                TXT_TL_CREATION_TITLE,
                TXT_TL_CREATION_HEADER,
                TXT_TL_CREATION_TEXT,
                TXT_TL_TIMELINETAG_LABEL,
                Optional.empty(),
                false);
    }

    private Optional<TopicSet> editDialog(ObservableTopicSet oldTag){
        return openTimeLineCreationDialog(
                TXT_TL_EDIT_TITLE,
                TXT_TL_EDIT_HEADER,
                TXT_TL_EDIT_TEXT,
                TXT_TL_TIMELINETAG_LABEL,
                Optional.of(oldTag.getInner()),
                true);
    }

    public boolean removeSegmentation(CustomSegmentationPane timeLine){
        if(DialogGenerator.confirmationDialogue(
                TXT_TL_DELETE_TITLE,
                TXT_TL_DELETE_HEADER,
                String.format(TXT_TL_DELETE_TEXT, timeLine.getTimeLineName())
        )){
            timeLineContainerController.removeTimeLine(timeLine.getTopicSetID());
            timeLineVBox.getChildren().remove(timeLine);
            timelineInfoVBox.getChildren().remove(segmentationWidgetLink.get(timeLine));
            return true;
        }
        return false;
    }

    private void handleCreateTLOutOfSelectedClick(){
        try{
            createNewSegmentationOutOfSelectedElements();
        }
        catch(NoTimeLineSelectedException ex){
            DialogGenerator.simpleErrorDialog(TXT_TL_CREATION_ERROR_TITLE, TXT_TL_CREATION_ERROR_HEADER, ex.toString());
        }
    }

    private void handleKeyStrokeEvent(KeyEvent event){
        switch(event.getCode()){
            case DELETE:
                handleDelete();
                break;
            default:
                break;
        }
    }

    private void handleDelete(){
        if(selectedTimeLine.selectedSegmentationIsCustom()
                && selectedTimeLine.getSelectedSegmentRectangles().size() > 0
                && DialogGenerator.confirmationDialogue(
                        "Delete selected segments",
                "Delete selected segments from " + selectedTimeLine.getSegmentationName(),
                "Are you sure you want to delete all selected segments from the segmentation " + selectedTimeLine.getSegmentationName() + "?")){
            selectedTimeLine.deleteSelectedSegments();
        }
    }


    private Optional<FilterForSegmentsDialog.FilterDialogResult> filterForAnnotationsDialog(String title, String header, String text){
        FilterForSegmentsDialog dialog = new FilterForSegmentsDialog(timeLineContainerController.getTopicSets());

        dialog.setResultConverter(b -> {
            if (b == dialog.getButtonType_ok()) {
                return dialog.getDialogResult();
            }
            return null;
        });

        return dialog.showAndWait();
    }

    //region CreateNewTimeLineDialogue

    private Optional<TopicSet> openTimeLineCreationDialog(String dialogTitle, String dialogHeader, String dialogText, String labelText, Optional<TopicSet> optional, boolean editCall){
        /*
        This dialog is specific to the creation of timelines and contains some more complex logic
        specific to timelines and the TimelineContainer. Therefore it cannot be moved to DialogGenerator
         */
        TopicSetDialog dialog = new TopicSetDialog(dialogTitle, dialogHeader, dialogText, optional);

        //defaultValue is only evaluated if editCall is true, in which case the optional is also present.
        String defaultValue = optional.isPresent() ? optional.get().getTag() : "";

        dialog.okButton().addEventFilter(ActionEvent.ACTION, ae -> {
            String newTimeLineName = dialog.getTopicSetText();
            //The if-block is entered if the dialog was opened by a creation command OR  if it's an edit command and
            //the old and new value are different.
            //If neither is true, it's an edit call where only the color of the timeline is edited and no name check is required.
            if(!editCall || !defaultValue.equals(newTimeLineName)) {
                try {
                    timeLineContainerController.checkIfTagIsValid(newTimeLineName);
                    dialog.topicsDefined();
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
            if (b == dialog.getButtonTypeOK()) {
                return dialog.getTopicSet();
            }
            return null;
        });

        return dialog.showAndWait();
    }

    //region private classes

    private class SegmentationInformation extends VBox {
        private SegmentationPane tl;

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


        public SegmentationInformation(SegmentationPane tl){
            this.tl = tl;

            InitializeButtons();
            setUpLabel();
            setupTimeLineInformation();

            setMinHeight(timeLinesHeight);
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

    /*private class TimeLineWrapper extends HBox{
        private final SegmentationPane tl;
        private final TimeLineInformation tli;
        TimeLineWrapper(SegmentationPane tl){
            this.tl = tl;
            this.tli = new TimeLineInformation(tl);
            getChildren().addAll(tli, tl);
            setPadding(new Insets(5,0,5,0));
        }
        SegmentationPane getTimeLinePane(){return tl;}
        TimeLineInformation getTimelineInformation(){return tli;}
    }*/

    /*private class TopicCreationDialogResult {
        String topicName;
        Color topicColor;
        public TopicCreationDialogResult(String name, Color color){this.topicName = name; this.topicColor = color;}
    }*/

    private class AnnotationFilterDialogResult{
        String topic;
        String filterText;
        public AnnotationFilterDialogResult(String topic, String filterText){
            this.topic = topic;
            this.filterText = filterText;
        }
        Segment[] getFilteredAnnotations(){
            return timeLineContainerController.getFilteredAnnotations(topic, filterText);
        }
    }
    //endregion
}
