package New.CustomControls.Containers;

import New.Controllers.SegmentationContainerController;
import New.CustomControls.SegmentationPanes.*;

import New.Dialogues.FilterForSegmentsDialog;
import New.Dialogues.SuperSetDialog;
import New.Execptions.NoSegmentationSelectedException;
import New.Execptions.SegmentationNameException;
import New.Model.Entities.Segment;
import New.Model.Entities.SuperSet;
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
import javafx.stage.Stage;

import java.util.*;

public class SegmentationContainer extends VBox {
    //region static strings
    private final static String TXT_TL_CREATION_TITLE = "Create a new super set";
    private final static String TXT_TL_CREATION_HEADER = "Creation of a new super set";
    private final static String TXT_TL_CREATION_TEXT = "Create a new super set by entering a tag. The tag must be unique and cannot be empty.";
    private final static String TXT_TL_TIMELINETAG_LABEL = "Super set name:";


    private final static String TXT_TL_EDIT_TITLE = "Edit super set";
    private final static String TXT_TL_EDIT_HEADER = "Editing a super set";
    private final static String TXT_TL_EDIT_TEXT = "Change the name of the super set";

    private final static String TXT_TL_DELETE_TITLE = "Delete super set";
    private final static String TXT_TL_DELETE_HEADER = "Deletion of a super set.";
    private final static String TXT_TL_DELETE_TEXT = "Are you sure you want to delete the super set %s? \nWARNING: Deleting a timeline will remove it and all annotations associated with it from ALL participants and all their pages.";

    private final static String TXT_TL_CREATION_ERROR_TITLE = "Super set creation error";
    private final static String TXT_TL_CREATION_ERROR_HEADER = "Error while creating super set";
    //endregion

    //buisiness logic attributes
    private DoubleProperty totalWidth;
    private final static double TIMELINES_HEIGHT = 50;
    private final static double SEGMENTATION_BOX_WIDTH = 800;
    private final static double TIMEUNITPANE_HEIGHT = 20;
    private final static double DEFAULT_ELEMENT_SPACING = 10;


    private DoubleProperty scale;

    private ObservableSegmentation selectedSegmentation;
    private ObservablePage p;

    private SegmentationContainerController segmentationContainerController;
    //endregion buisiness logic
    /**
     * the hash-map ensures that segmentations and their widgets stay connected to each other
     * for removal procedure
     */
    private HashMap<SegmentationPane, SegmentationInformation> segmentationWidgetLink = new HashMap<>();

    //graphical attributes
    private ScrollPane verticalScrollPane = new ScrollPane();
    private ScrollPane horizontalScrollPane = new ScrollPane();
    private VBox segmentationVBox = new VBox();
    private VBox segmentationInfoVBox = new VBox();
    private HBox containerTimelinesHBox = new HBox();
    private TimeUnitPane unitPane;

    private Button createSegmentationButton;
    private Button createNewSegmentationOutOfSelectedButton;
    private HBox buttonHBox;

    private Slider scaleSlider;

    private VBox mainContainer = new VBox();
    //endregion graphical attributes

    public SegmentationContainer(ObservableProject project, ObservablePage page, double initialScale){
        this.p = page;

        page.getPageProperty().addListener((observable, oldValue, newValue) -> {
            InitializeContainer(project, page);
        });

        AnchorPane.setBottomAnchor(this, 0.0);
        AnchorPane.setLeftAnchor(this, 0.0);
        AnchorPane.setRightAnchor(this, 0.0);
        AnchorPane.setTopAnchor(this, 0.0);

        segmentationContainerController = new SegmentationContainerController(project, page);
        totalWidth = new SimpleDoubleProperty(page.getDuration());
        scale = new SimpleDoubleProperty(initialScale);

        //Initialize Scale Slider
        scaleSlider = initializeSlider(initialScale);
        scale.bind(scaleSlider.valueProperty());
        //Units for timeline
        unitPane = new TimeUnitPane(scale,TIMEUNITPANE_HEIGHT,totalWidth);

        InitializeSegmentationScrollPane();

        InitializeButtonHBox();

        containerTimelinesHBox.getChildren().addAll(segmentationInfoVBox, verticalScrollPane);

        //add everything to parent
        mainContainer.setMaxWidth(SEGMENTATION_BOX_WIDTH + 150);
        mainContainer.setSpacing(10d);
        //mainContainer.getChildren().addAll(scaleSlider,buttonHBox,containerTimelinesHBox);
        horizontalScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        horizontalScrollPane.setContent(containerTimelinesHBox);
        mainContainer.getChildren().addAll(scaleSlider,buttonHBox,horizontalScrollPane);
        this.getChildren().add(mainContainer);
        InitializeContainer(project, page);

        this.setOnKeyPressed(event -> handleKeyStrokeEvent(event));

    }

    private void InitializeContainer(ObservableProject project, ObservablePage page){
        getSelectedSegmentation().setSelectedTimeLine(null);
        //Step 1: Create the stroke timeline
        //Step 2: For each tag, create a new timeline and pass over the observble Tag and the page. Then create the annotations.
        //getChildren().clear();

        totalWidth.set(page.getDuration());
        //unitPane = new TimeUnitPane(scale,20,totalWidth);
        segmentationVBox.getChildren().clear();
        segmentationInfoVBox.getChildren().clear();
        segmentationVBox.getChildren().add(unitPane);
        UnmodifiableSelectableSegmentationPane strokePane = new UnmodifiableSelectableSegmentationPane(
                totalWidth.get(),
                TIMELINES_HEIGHT,
                scale,
                project.getObservableTopicSet(project.getStrokeSetID()),
                page,
                this
        );

        addSegmentationPane(strokePane);

        for(String topicSetID : project.getTopicSetIDs()){
            if(!topicSetID.equals(project.getStrokeSetID())){

                ObservableSuperSet tag = project.getObservableTopicSet(topicSetID);
                loadSegmentation(tag, page, page.getAnnotationSet(topicSetID));

            }
        }
        System.gc();
    }

    private void InitializeSegmentationScrollPane() {
        scale.addListener((observable, oldValue, newValue) -> {
            double hVal = horizontalScrollPane.getHvalue() / oldValue.doubleValue();
            horizontalScrollPane.setHvalue(hVal * newValue.doubleValue());
        });

        segmentationVBox.setSpacing(DEFAULT_ELEMENT_SPACING);

        verticalScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        verticalScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        verticalScrollPane.setMaxWidth(SEGMENTATION_BOX_WIDTH);
        verticalScrollPane.setContent(segmentationVBox);

        segmentationInfoVBox.setSpacing(DEFAULT_ELEMENT_SPACING);
        segmentationInfoVBox.setPadding(new Insets(50,0,0,10));
        segmentationInfoVBox.setMinSize(100, TIMELINES_HEIGHT);
    }


    private void InitializeButtonHBox(){
        createSegmentationButton = new Button("Create new super set");
        createSegmentationButton.setOnAction(event -> createNewSegmentation());
        createNewSegmentationOutOfSelectedButton = new Button("Create new super set out of selected annotations");
        createNewSegmentationOutOfSelectedButton.setOnAction(event -> handleCreateSegmentationOutOfSelectedClick());
        buttonHBox = new HBox(createSegmentationButton, createNewSegmentationOutOfSelectedButton);
        buttonHBox.setSpacing(DEFAULT_ELEMENT_SPACING);
    }

    private Slider initializeSlider(double initScale){
        Slider slider = new Slider(0.05d, 1, initScale);
        slider.setMajorTickUnit(0.05);
        slider.setMinorTickCount(0);
        slider.setShowTickMarks(true);
        slider.setSnapToTicks(true);
        return slider;
    }

    /**
     * Retrieves the ObservableSegmentation singleton object
     * @return the observableSegmentation
     */
    public ObservableSegmentation getSelectedSegmentation() {
        if(selectedSegmentation == null){
            selectedSegmentation = new ObservableSegmentation();
        }
        return selectedSegmentation;
    }

    private void addSegmentationPane(SegmentationPane segmentationPane){
        //Create new Toolbar for the Segmentation
        SegmentationInformation info = new SegmentationInformation(segmentationPane);
        segmentationInfoVBox.getChildren().add(info);
        //Add the actual timeline
        segmentationVBox.getChildren().add(segmentationPane);
        segmentationWidgetLink.put(segmentationPane,info);
    }


    private void loadSegmentation(ObservableSuperSet t, ObservablePage p, Optional<Set<Segment>> annotations){
        if(annotations.isPresent()){
            CustomSegmentationPane pane;
            pane = new CustomSegmentationPane(totalWidth.get(), TIMELINES_HEIGHT, scale, t, p, this);
            addSegmentationPane(pane);
        }
        else{
            addSegmentationPane(createNewSegmentationPane(t, p, false));
        }
    }


    /**
     * This method calls the Creation dialog. If resolved successfully, a new , empty segmentation will be added.
     */
    public void createNewSegmentation(){
        Optional<SuperSet> tag = createTopicSetDialog();
        Optional<FilterForSegmentsDialog.FilterDialogResult> filtered = Optional.empty();

        if(tag.isPresent()){

            ObservableSuperSet newTag = segmentationContainerController.createNewTimeLineTag(tag.get());

            SegmentationPane segmentationPane = createNewSegmentationPane(newTag, segmentationContainerController.getPage(),false);

            addSegmentationPane(segmentationPane);

        }
    }

    /**
     * This method calls the Creation dialog. If resolved successfully, a new segmentation containing copies of selected segments will be added.
     * @throws NoSegmentationSelectedException if no segmentation is currently selected
     */
    public void createNewSegmentationOutOfSelectedElements() throws NoSegmentationSelectedException {
        if(!selectedSegmentation.selectedSegmentationAvailable()){
            throw new NoSegmentationSelectedException();
        }
        Optional<SuperSet> tag =createTopicSetDialog();
        if(tag.isPresent()){

            ObservableSuperSet newTag = segmentationContainerController.createNewTimeLineTag(tag.get());

            SegmentationPane segmentationPane = createNewSegmentationTagOutOfSelected(newTag, segmentationContainerController.getPage());
            addSegmentationPane(segmentationPane);
        }
    }


    /**
     * Creates a segmentation, that is the negative image of the given topicset on all pages of the participant
     * @param originSetID
     */
    public void createNegativeSegmentation(String originSetID){
        Optional<SuperSet> tag = createTopicSetDialog();
        if(tag.isPresent()){
            ObservableSuperSet newTag = segmentationContainerController.createNewTimeLineTag(tag.get());
            SegmentationPane segmentationPane = createNewSegmentationPaneOutOfAnnotations(newTag, p, p.getNegativeSegmentation(originSetID));
            addSegmentationPane(segmentationPane);
        }
    }


    /**
     * Creates a a segmentation from the selected canvas dots (Observable dots with Property selected = true)
     */
    public void createNewSegmentationOutOfSelectedDots() {
        Optional<SuperSet> tag = createTopicSetDialog();
                if(tag.isPresent()){
                    ObservableSuperSet newTag = segmentationContainerController.createNewTimeLineTag(tag.get());
            SegmentationPane segmentationPane = createNewSegmentationPaneOutOfSelectedDots(newTag, segmentationContainerController.getPage());
            addSegmentationPane(segmentationPane);
        }
    }


    /**
     * This method calls the Creation dialog. If resolved successfully, a segmentation containing the given set of segments will be added.
     * @param a
     */
    public void createNewSegmentationOutOfSet(Set<Segment> a){
            Optional<SuperSet> tag = createTopicSetDialog();

        if(tag.isPresent()){
            ObservableSuperSet newTag = segmentationContainerController.createNewTimeLineTag(tag.get());
            SegmentationPane segmentationPane = createNewSegmentationPaneOutOfCombined(newTag, segmentationContainerController.getPage(), a);
            addSegmentationPane(segmentationPane);
        }
    }




    private SegmentationPane createNewSegmentationPane(ObservableSuperSet tag, ObservablePage page, boolean copyFromSelected){
        CustomSegmentationPane result;
        if(copyFromSelected){
            selectedSegmentation.getMissingTopics(tag).forEach(topic -> tag.addTopic(topic));
            result = new CustomSegmentationPane(segmentationContainerController.getPage().getDuration(), TIMELINES_HEIGHT, scale, tag, page, this, selectedSegmentation.generateMissingSegments(tag.getTopicsObservableList()));
        }
        else{
            result = new CustomSegmentationPane(segmentationContainerController.getPage().getDuration(), TIMELINES_HEIGHT, scale, tag, page, this);
        }
        return result;
    }


    private SegmentationPane createNewSegmentationTagOutOfSelected(ObservableSuperSet newTimeLineTag, ObservablePage page){
        return createNewSegmentationPane(newTimeLineTag, page, true);
    }

    private SegmentationPane createNewSegmentationPaneOutOfSelectedDots(ObservableSuperSet newTimeLineTag, ObservablePage page){
        Set<Segment> segments = new TreeSet<>();
        for(List<ObservableDot> segment : page.getSelectedDotSections()){
            segments.add(new Segment(
                    segment.get(0).getTimeStamp(),
                    segment.get(segment.size()-1).getTimeStamp()));
        }
        return new CustomSegmentationPane(segmentationContainerController.getPage().getDuration(), TIMELINES_HEIGHT, scale, newTimeLineTag, page, this, segments);
    }

    private SegmentationPane createNewSegmentationPaneOutOfCombined(ObservableSuperSet tag, ObservablePage page, Set<Segment> segments){
        CustomSegmentationPane newTimeLine = new CustomSegmentationPane(segmentationContainerController.getPage().getDuration(), TIMELINES_HEIGHT, scale, tag, page, this, segments);
        return newTimeLine;
    }

    private SegmentationPane createNewSegmentationPaneOutOfAnnotations(ObservableSuperSet tag, ObservablePage page, Set<Segment> segments){
        return new CustomSegmentationPane(segmentationContainerController.getPage().getDuration(), TIMELINES_HEIGHT, scale, tag, page, this, segments);
    }

    /**
     * Calls the edit dialog applies changes if the dialog is resolved successfully
     * @param oldSuperSet the old ObservableSuperSet which needs to be edited
     */
    public void editSegmentation(ObservableSuperSet oldSuperSet){
        Optional<SuperSet> tag = editDialog(oldSuperSet);
        if (tag.isPresent()){
            segmentationContainerController.editSuperSet(oldSuperSet, tag.get());
        }
    }

    private Optional<SuperSet> createTopicSetDialog(){
        return openSegmentationCreationDialog(
                TXT_TL_CREATION_TITLE,
                TXT_TL_CREATION_HEADER,
                TXT_TL_CREATION_TEXT,
                TXT_TL_TIMELINETAG_LABEL,
                Optional.empty(),
                false);
    }

    private Optional<SuperSet> editDialog(ObservableSuperSet oldTag){
        return openSegmentationCreationDialog(
                TXT_TL_EDIT_TITLE,
                TXT_TL_EDIT_HEADER,
                TXT_TL_EDIT_TEXT,
                TXT_TL_TIMELINETAG_LABEL,
                Optional.of(oldTag.getInner()),
                true);
    }

    /**
     * Removes the given custom segmentation pane from the container. Note that this implicitly also
     * removes the super set and consequently all segmentations defined under this super set from the project.
     * @param customSegmentationPane segmentation pane that is to be deleted
     * @return true if the segentation and superset were successfully deleted, false otherwise.
     */
    public boolean removeSegmentation(CustomSegmentationPane customSegmentationPane){
        if(DialogGenerator.confirmationDialogue(
                TXT_TL_DELETE_TITLE,
                TXT_TL_DELETE_HEADER,
                String.format(TXT_TL_DELETE_TEXT, customSegmentationPane.getTimeLineName())
        )){
            segmentationContainerController.removeSuperSet(customSegmentationPane.getTopicSetID());
            segmentationVBox.getChildren().remove(customSegmentationPane);
            segmentationInfoVBox.getChildren().remove(segmentationWidgetLink.get(customSegmentationPane));
            return true;
        }
        return false;
    }

    private void handleCreateSegmentationOutOfSelectedClick(){
        try{
            createNewSegmentationOutOfSelectedElements();
        }
        catch(NoSegmentationSelectedException ex){
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
        if(selectedSegmentation.selectedSegmentationIsCustom()
                && selectedSegmentation.getSelectedSegmentRectangles().size() > 0
                && DialogGenerator.confirmationDialogue(
                        "Delete selected segments",
                "Delete selected segments from " + selectedSegmentation.getSegmentationName(),
                "Are you sure you want to delete all selected segments from the segmentation " + selectedSegmentation.getSegmentationName() + "?")){
            selectedSegmentation.deleteSelectedSegments();
        }
    }


    private Optional<FilterForSegmentsDialog.FilterDialogResult> filterForAnnotationsDialog(String title, String header, String text){
        FilterForSegmentsDialog dialog = new FilterForSegmentsDialog(segmentationContainerController.getTopicSets());

        dialog.setResultConverter(b -> {
            if (b == dialog.getButtonType_ok()) {
                return dialog.getDialogResult();
            }
            return null;
        });

        return dialog.showAndWait();
    }

    //region CreateNewTimeLineDialogue

    private Optional<SuperSet> openSegmentationCreationDialog(String dialogTitle, String dialogHeader, String dialogText, String labelText, Optional<SuperSet> optional, boolean editCall){
        /*
        This dialog is specific to the creation of timelines and contains some more complex logic
        specific to timelines and the TimelineContainer. Therefore it cannot be moved to DialogGenerator
         */
        SuperSetDialog dialog = new SuperSetDialog(dialogTitle, dialogHeader, dialogText, optional);

        //defaultValue is only evaluated if editCall is true, in which case the optional is also present.
        String defaultValue = optional.isPresent() ? optional.get().getSuperSetName() : "";

        dialog.okButton().addEventFilter(ActionEvent.ACTION, ae -> {
            String newTimeLineName = dialog.getTopicSetText();
            //The if-block is entered if the dialog was opened by a creation command OR  if it's an edit command and
            //the old and new value are different.
            //If neither is true, it's an edit call where only the color of the timeline is edited and no name check is required.
            if(!editCall || !defaultValue.equals(newTimeLineName)) {
                try {
                    segmentationContainerController.checkIfSuperSetNameIsValid(newTimeLineName);
                    dialog.topicsDefined();
                } catch (SegmentationNameException ex) {
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


        public SegmentationInformation(SegmentationPane tl){
            this.tl = tl;

            InitializeButtons();
            setUpLabel();
            setupSegmentationInformation();

            setMinHeight(TIMELINES_HEIGHT);
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

        private void setupSegmentationInformation(){
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
            stage.setTitle("Detail segmentation view for" + tl.getTimeLineName());
            stage.setScene(new Scene(new SegmentationDetailContainer(tl, segmentationContainerController.getPage()), 450, 450));
            stage.show();
        }
    }
}
