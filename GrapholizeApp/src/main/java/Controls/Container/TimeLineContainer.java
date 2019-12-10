package Controls.Container;

import Controllers.TimeLinesController;
import Controls.Timeline.Pane.CommentTimeLinePane;
import Controls.Timeline.Pane.StrokeDurationTimeLinePane;
import Controls.Timeline.Pane.TimeLinePane;
import Controls.TimelineElement.TimeLineElementRect;
import Interfaces.Observable;
import Interfaces.Observer;
import Model.Entities.Page;
import Model.Entities.Project;
import Model.ObservableActiveState;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import util.DialogGenerator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//https://stackoverflow.com/questions/17761415/how-to-change-order-of-children-in-javafx for children swapping

/*Idea: The TLContainer replaces the current scrollpane with timelines. When adding a new timeline, the container wraps it
into a TimeLineWrapper which contains the visual timeline and Timeline information (name and some control buttons) and then adds it into
the VBox that has all the timelines.
 */
public class TimeLineContainer extends VBox implements Observer{




    private TimeLinesController timeLinesController;

    private Button btn_CreateNewTimeLine;

    private TimeLinePane selectedTimeLine;

    //TODO: The container should read/update the length and scale of the timeline (determined by the strokes) from a model class
    public TimeLineContainer(Project p, ObservableActiveState state){
        this.timeLinesController = new TimeLinesController(p, 0.05, 50, state);
        state.addObserver(this);
        setup();
    }

    private void setup(){
        generateTimeLines();
        btn_CreateNewTimeLine = new Button("Create new TimeLine");
        btn_CreateNewTimeLine.setOnAction(e -> handleCreateNewTimeLineClick());
        getChildren().add(btn_CreateNewTimeLine);
    }

    public void createNewCustomTimeLine(String name, Color c, Optional<List<TimeLineElementRect>> tleList){
        addTimeLine(timeLinesController.createNewCustomTimeLine(name, c, tleList));
    }

    public void addTimeLine(TimeLinePane tl){
        getChildren().remove(btn_CreateNewTimeLine);

        tl.setOnMouseClicked(event -> handleTimeLineClick(event, tl));
        tl.setOnContextMenuRequested(contextMenuEvent -> GenerateTimelineContextMenu(tl).show(tl, contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY()));

        getChildren().add(new TimeLineWrapper(tl, new TimeLineInformation(tl)));
        getChildren().add(btn_CreateNewTimeLine);
    }



    //TODO: Consider binding events to Wrapper and not to timeline...
    private void removeTimeLine(TimeLinePane tl){
        for(Node n : getChildren()){
            if(n.getClass() == TimeLineWrapper.class && ((TimeLineWrapper)n).getTimeLinePane() == tl){
                getChildren().remove(n);
                break;
            }
        }
    }




    /**
     * Initiates a new ContextMenu with two default commands: "Create new Timeline" and "Create new Timeline out of selected items.
     * The context menu is passed down to each timeline Pane and its children, where new commands are added.
     * @param t
     * @return
     */
    private ContextMenu GenerateTimelineContextMenu(TimeLinePane t) {
        //TODO: Add Move up/Move down functionality
        //TODO: Consider disabling some menuitems
        MenuItem menuItem_CreateNewTimeLine = new MenuItem("Create new timeline below");
        menuItem_CreateNewTimeLine.setOnAction(event -> handleCreateNewTimeLineClick());

        MenuItem menuItem_CreateNewTimeLineOutOfSelected = new MenuItem("Create new timeline out of selected items");
        menuItem_CreateNewTimeLineOutOfSelected.setOnAction(event -> handleCreateNewTimeLineOutOfSelectedClick(t));

        MenuItem menuItem_EditTimeLine = new MenuItem("Edit timeline");
        menuItem_EditTimeLine.setOnAction(event -> handleEditTimeLineClick(t));

        MenuItem menuItem_DeleteTimeLine = new MenuItem("Delete timeline");
        menuItem_DeleteTimeLine.setOnAction(event -> handleRemoveTimeLineClick(t));

        MenuItem menuItem_CreateAnnotationOutOfSelected = new MenuItem("Create annotation out of selected elements");
        menuItem_CreateAnnotationOutOfSelected.setOnAction(event -> handleCreateNewAnnotationOutOfSelectedClick(t));
        if(selectedTimeLine == null || selectedTimeLine == t || t.getClass() == StrokeDurationTimeLinePane.class){
            menuItem_CreateAnnotationOutOfSelected.setDisable(true);
        }

        return new ContextMenu(
                menuItem_CreateAnnotationOutOfSelected,
                menuItem_CreateNewTimeLine,
                menuItem_CreateNewTimeLineOutOfSelected,
                menuItem_EditTimeLine,
                menuItem_DeleteTimeLine);
        //return  new ContextMenu(menuItem_CreateNewTimeLine, menuItem_CreateNewTimeLineOutOfSelected);
    }
    //TODO: Add functionality that allows for timeline creation below an existing timeline.

    //region click handler methods
    /**
     * Handler-method for a mouse click event on a TimeLinePane. The event is assigned this method in addTimeLine().
     * It Primarily handles left clicks on a timeline (selection)
     * @param e
     * @param timeLine
     */
    private void handleTimeLineClick(MouseEvent e, TimeLinePane timeLine){
        System.out.println("handleTimeLineClick called from TimeLineContainer");
        //If a new timeline is clicked => Deselect all items from other timelines
        if(e.getButton().equals(MouseButton.PRIMARY) && timeLine != selectedTimeLine){
            if(selectedTimeLine != null){
                selectedTimeLine.deselectTimeLine();
            }
            selectedTimeLine = timeLine;
        }
    }

    /**
     * Handles a click event on buttons/menuitems that create new Timelines
     */
    private void handleCreateNewTimeLineClick(){
        Optional<DialogResult> dialogResult = openTimeLineCreationDialog(TXT_TL_CREATION_TITLE, TXT_TL_CREATION_HEADER, TXT_TL_CREATION_TEXT, TXT_TL_TIMELINETAG_LABEL, TXT_TL_TAG_DEFAULTVAL, Color.CADETBLUE);
        if(dialogResult.isPresent()){
            createNewCustomTimeLine(dialogResult.get().timeLinename, dialogResult.get().timeLineColor, Optional.empty());
        }
    }
    /**
     * Handles a click event on buttons/menuitems that create new Timelines with selected items
     */
    private void handleCreateNewTimeLineOutOfSelectedClick(TimeLinePane tl){
        Optional<DialogResult> dialogResult = openTimeLineCreationDialog(TXT_TL_CREATION_TITLE, TXT_TL_CREATION_HEADER, TXT_TL_CREATION_TEXT, TXT_TL_TIMELINETAG_LABEL, TXT_TL_TAG_DEFAULTVAL, Color.CADETBLUE);
        if(dialogResult.isPresent()){
            List<TimeLineElementRect> tles = tl.getChildren().stream()
                    .map(node -> (TimeLineElementRect)node)//TODO: Maybe there's a better solution? (Should there be a separate List with the TLE in the timeline?)
                    .filter(tle -> ((TimeLineElementRect)tle).isSelected())
                    .collect(Collectors.toList());
            createNewCustomTimeLine(dialogResult.get().timeLinename, dialogResult.get().timeLineColor, Optional.of(tles));
        }
    }
    //TODO: Let user choose from colors!

    private void handleEditTimeLineClick(TimeLinePane tl){
        if(tl.getClass() == CommentTimeLinePane.class){
            Optional<DialogResult> dialogResult = openTimeLineCreationDialog(TXT_TL_EDIT_TITLE, TXT_TL_EDIT_HEADER, TXT_TL_EDIT_TEXT, TXT_TL_TIMELINETAG_LABEL, tl.getTimeLineName(), tl.getTimeLineColor());
            if(dialogResult.isPresent()){
                tl.setTimeLineName(dialogResult.get().timeLinename);
                tl.setTimeLineColor(dialogResult.get().timeLineColor);
            }
        }
        else{
            DialogGenerator.simpleErrorDialog(TXT_TL_EDIT_ERROR_TITLE, TXT_TL_EDIT_ERROR_HEADER, TXT_TL_ERROR_CANNOT_BE_EDITED);
        }

    }

    public void handleRemoveTimeLineClick(TimeLinePane source){
        //Ask if TL should actually be removed
        if(source.getClass() == CommentTimeLinePane.class){
            if(timeLinesController.deleteConfirmation(source.getTimeLineName())){
                removeTimeLine(source);
            }
        }
        else{
            DialogGenerator.simpleErrorDialog(TXT_TL_DELETE_ERROR_TITLE, TXT_TL_DELETE_ERROR_HEADER, TXT_TL_ERROR_CANNOT_BE_DELETED);
        }
    }

    public void handleCreateNewAnnotationOutOfSelectedClick(TimeLinePane source){
        createCopyAnnotationDialogue(source);
    }
    //endregion

    public void createCopyAnnotationDialogue(TimeLinePane tl){
        Dialog dialog = new Dialog<>();
        dialog.setTitle("Copy annotations");
        dialog.setHeaderText("Copying selected annotations");
        dialog.setContentText(
                  "The selected annotations will be copied into the timeline " + tl.getTimeLineName() + ".\n"
                + "You can choose to combine the selected annotations into a single annotation. If so, you may enter a new annotation text."
        );
        dialog.setResizable(true);

        CheckBox cbox_joinedAnnotation = new CheckBox("Combine selected elements into one annotation");

        Label label1 = new Label("Annotation text: (Only applied if combine option is selected.)");
        TextField text1 = new TextField("New annotation text.");
        text1.disableProperty().bind(cbox_joinedAnnotation.selectedProperty().not());


        GridPane grid = new GridPane();
        grid.add(cbox_joinedAnnotation, 1, 1);
        grid.add(label1, 1, 2);
        grid.add(text1, 2, 2);
        dialog.getDialogPane().setContent(grid);

        ButtonType buttonTypeOk = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        dialog.getDialogPane().getButtonTypes().addAll(buttonTypeOk, buttonTypeCancel);

        final Button okButton = (Button) dialog.getDialogPane().lookupButton(buttonTypeOk);

        dialog.setResultConverter(b -> {
            if (b == buttonTypeOk) {
                timeLinesController.createCopyAnnotations(tl, cbox_joinedAnnotation.isSelected(), text1.getText());
            }

            return null;
        });

        dialog.showAndWait();
    }

    //region CreateNewTimeLineDialogue
    //https://code.makery.ch/blog/javafx-dialogs-official/
    //https://examples.javacodegeeks.com/desktop-java/javafx/dialog-javafx/javafx-dialog-example/
    //https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/Dialog.html#resultConverterProperty--
    private Optional<DialogResult> openTimeLineCreationDialog(String dialogTitle, String dialogHeader, String dialogText, String labelText, String defaultValue, Color defaultColor){

        /*
        This dialog is specific to the creation of timelines and contains some more complex logic
        specific to timelines and the TimelineContainer. Therefore it cannot be moved to DialogGenerator
         */
        Dialog<DialogResult> dialog = new Dialog<>();
        dialog.setTitle(dialogTitle);
        dialog.setHeaderText(dialogHeader);
        dialog.setContentText(dialogText);
        dialog.setResizable(true);

        TextField text1 = new TextField(defaultValue);
        ColorPicker colorPicker = new ColorPicker(defaultColor);

        GridPane grid = new GridPane();
        grid.add(new Label(labelText), 1, 1);
        grid.add(text1, 2, 1);
        grid.add(new Label("Timeline color:"), 1, 2);
        grid.add(colorPicker,2,2 );
        dialog.getDialogPane().setContent(grid);

        ButtonType buttonTypeOk = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        dialog.getDialogPane().getButtonTypes().addAll(buttonTypeOk, buttonTypeCancel);

        final Button okButton = (Button) dialog.getDialogPane().lookupButton(buttonTypeOk);

        okButton.addEventFilter(ActionEvent.ACTION, ae -> {
            if (!stringIsUniqueAndValid(text1.getText())) {
                ae.consume(); //not valid
            }
        });

        dialog.setResultConverter(b -> {
            if (b == buttonTypeOk) {
                return new DialogResult(text1.getText(), colorPicker.getValue());
            }

            return null;
        });

        return dialog.showAndWait();
    }

    //TODO: Define static strings for title etc.
    private boolean stringIsUniqueAndValid(String s){
        boolean notEmpty = stringNotEmpty(s);
        boolean nameUnique = stringUnique(s);
        if(!notEmpty){
            DialogGenerator.simpleErrorDialog(
                    TXT_TL_CREATION_ERROR_TITLE,
                    TXT_TL_CREATION_ERROR_HEADER,
                    TXT_TL_ERROR_NAMEEMPTY);
            return false;
        }
        else if(!nameUnique){
            DialogGenerator.simpleErrorDialog(
                    TXT_TL_CREATION_ERROR_TITLE,
                    TXT_TL_CREATION_ERROR_HEADER,
                    TXT_TL_ERROR_NAME_NOT_UNIQUE);
            return false;
        }
        return true;
    }

    private boolean stringNotEmpty(String s){
        return !s.isBlank();
    }
    private boolean stringUnique(String s){
        List<String> timelineTags = getChildren().stream()
                .filter(elem -> elem.getClass() == TimeLineWrapper.class) //TODO: Maybe there is a better solution?
                .map(tlwrapper -> ((TimeLineWrapper)tlwrapper).getTimeLinePane().getTimeLineName())
                .collect(Collectors.toList()
        );
        return !timelineTags.contains(s);
    }

    @Override
    public void update(Observable sender) {
        generateTimeLines();
    }

    private void generateTimeLines(){
        getChildren().clear();
        for(TimeLinePane tlp : timeLinesController.generateTimeLinePanes()){
            getChildren().add(new TimeLineWrapper(tlp, new TimeLineInformation(tlp)));
        }
    }

    //endregion


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
            setUpLabel(tl.getTimeLineName());
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

        private void setUpLabel(String txt){
            lbl_timeLineName = new Label(txt);
            lbl_timeLineName.textProperty().bind(tl.getTimeLineNameProperty());
        }

        private void setupTimeLineInformation(){
            vBox_UpDownButtonContainer = new VBox(btn_moveTimelineUp, btn_moveTimelineDown);
            hBox_EditAndDeleteContainer = new HBox(btn_editTimeLine, btn_deleteTimeLine);
            vBox_EditButtons = new VBox(hBox_EditAndDeleteContainer, btn_addNewTimeline);
            hBox_ButtonsContainer = new HBox(vBox_UpDownButtonContainer, vBox_EditButtons);
            getChildren().addAll(lbl_timeLineName, hBox_ButtonsContainer);
        }
    }

    private class TimeLineWrapper extends HBox{
        private final TimeLinePane tl;
        private final TimeLineInformation tli;
        TimeLineWrapper(TimeLinePane tl, TimeLineInformation tli){
            this.tl = tl;
            this.tli = tli;
            getChildren().addAll(tli, tl);
        }
        TimeLinePane getTimeLinePane(){return tl;}
        TimeLineInformation getTimelineInformation(){return tli;}
    }

    private class DialogResult{
        String timeLinename;
        Color timeLineColor;
        public DialogResult(String name, Color color){this.timeLinename = name; this.timeLineColor = color;}

    }

    //endregion

}
