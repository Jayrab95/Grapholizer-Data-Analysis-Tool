package Controls.Container;

import Controls.Timeline.Pane.CommentTimeLinePane;
import Controls.Timeline.Pane.TimeLinePane;
import Controls.TimelineElement.CommentTimeLineElement;
import Controls.TimelineElement.TimeLineElement;
import Interfaces.Observer;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//https://stackoverflow.com/questions/17761415/how-to-change-order-of-children-in-javafx for children swapping

/*Idea: The TLContainer replaces the current scrollpane with timelines. When adding a new timeline, the container wraps it
into a TimeLineWrapper which contains the visual timeline and Timeline information (name and some control buttons) and then adds it into
the VBox that has all the timelines.
 */
public class TimeLineContainer extends HBox {


    private final static String TXT_TL_CREATION_TITLE = "Create a new timeline";
    private final static String TXT_TL_CREATION_HEADER = "Creation of a new timeline";
    private final static String TXT_TL_CREATION_TEXT = "Create a new timeline by entering a tag. The tag must be unique and cannot be empty.";
    private final static String TXT_TL_TIMELINETAG_LABEL = "Timeline tag:";
    private final static String TXT_TL_TAG_DEFAULTVAL = "New Timeline";
    private final static String TXT_TL_EDIT_TITLE = "Edit timeline";
    private final static String TXT_TL_EDIT_HEADER = "Editing a timeline";
    private final static String TXT_TL_EDIT_TEXT = "Change the name of the timeline";
    private final static String TXT_TL_CREATION_ERROR_TITLE = "Timeline creation error";
    private final static String TXT_TL_CREATION_ERROR_HEADER = "Error while creating timeline";
    private final static String TXT_TL_EDIT_ERROR_TITLE = "Timeline edit error";
    private final static String TXT_TL_EDIT_ERROR_HEADER = "Error while editing timeline";
    private final static String TXT_TL_ERROR_NAMEEMPTY = "The tag cannot be empty.";
    private final static String TXT_TL_ERROR_NAME_NOT_UNIQUE = "This tag already exists. Please choose another tag.";
    private final static String TXT_TL_ERROR_CANNOT_BE_DELETED = "This timeline cannot be deleted. Only custom timelines can be deleted.";
    private final static String TXT_TL_ERROR_CANNOT_BE_EDITED = "This timeline cannot be edited. Only custom timelines can be edited.";
    private final static String TXT_TL_DELETE_ERROR_TITLE = "Delete timeline error";
    private final static String TXT_TL_DELETE_ERROR_HEADER = "Error while deleting timeline";


    private double scale;
    private double length;

    //Left side contains the Timeline information and buttons. Right side contains the timeline.
    //Downside of doing this split approach: Timelines now consist of 2 separate nodes that need to be separately managed...
    //private SplitPane splitPane_splitContainer;
    private VBox vbox_timeLineInfoContainer;
    //private ScrollPane scrollpane_timeLines;
    private VBox vbox_timeLines;

    private Button btn_CreateNewTimeLine;

    private TimeLinePane selectedTimeLine;

    //TODO: The container should read/update the length and scale of the timeline (determined by the strokes) from a model class
    public TimeLineContainer(double length, double scale){
        this.length = length;
        this.scale = scale;
        setup();
    }

    private void setup(){
        vbox_timeLineInfoContainer = new VBox();
        vbox_timeLines = new VBox();

        btn_CreateNewTimeLine = new Button("Create new TimeLine");
        btn_CreateNewTimeLine.setOnAction(e -> handleCreateNewTimeLineClick());
        vbox_timeLineInfoContainer.getChildren().add(btn_CreateNewTimeLine);

        getChildren().addAll(vbox_timeLineInfoContainer, vbox_timeLines);
    }

    public void createNewCustomTimeLine(String name, Color c, Optional<List<TimeLineElement>> tleList){
        //Currently: Since the StrokeTimeLine is always available, take the width of this element
        //TODO: Think of better solution. Perhaps the TimeLineContainer class can manage the total width?
        //Creation of timeline
        TimeLinePane ctlp = new CommentTimeLinePane(name, length, 50, scale, c);
        if(tleList.isPresent()){
            for(TimeLineElement tle : tleList.get()){
                CommentTimeLineElement ctle = new CommentTimeLineElement(c, tle);
                //TODO: perhaps all TLEs can have a comment? It's just empty on stroke elements
                if(tle.getClass().equals(CommentTimeLineElement.class)){
                    ctle.setComment(((CommentTimeLineElement)tle).getComment());
                }
                ctlp.getChildren().add(ctle);
            }
        }
        addTimeLine(ctlp);
    }

    public void addTimeLine(TimeLinePane tl){
        vbox_timeLineInfoContainer.getChildren().remove(btn_CreateNewTimeLine);

        //TODO: This is a pretty bad solution. Before this is called, there isn't really a context menu for the timeline.
        //GenerateTimelineContextMenu(tl);
        tl.setOnMouseClicked(event -> handleTimeLineClick(event, tl));
        tl.setOnContextMenuRequested(contextMenuEvent -> GenerateTimelineContextMenu(tl).show(tl, contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY()));

        vbox_timeLineInfoContainer.getChildren().add(new TimeLineWrapper(tl, new TimeLineInformation(tl)));
        vbox_timeLineInfoContainer.getChildren().add(btn_CreateNewTimeLine);
    }

    public void handleRemoveTimeLineClick(TimeLinePane source){
        //Ask if TL should actually be removed
        if(source.getClass() == CommentTimeLinePane.class){
            if(deleteConfirmation(source.getTimeLineName())){
                removeTimeLine(source);
            }
        }
        else{
            openErrorDialogue(TXT_TL_DELETE_ERROR_TITLE, TXT_TL_DELETE_ERROR_HEADER, TXT_TL_ERROR_CANNOT_BE_DELETED);
        }
    }

    //TODO: Consider binding events to Wrapper and not to timeline...
    private void removeTimeLine(TimeLinePane tl){
        for(Node n : vbox_timeLineInfoContainer.getChildren()){
            if(n.getClass() == TimeLineWrapper.class && ((TimeLineWrapper)n).getTimeLinePane() == tl){
                vbox_timeLineInfoContainer.getChildren().remove(n);
                break;
            }
        }
    }


    //TODO: maybe move this to a "Dialog creator class"
    private boolean deleteConfirmation(String tlname){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Timeline");
        alert.setHeaderText("Delete timeline?");
        alert.setContentText("Are you sure you want to delete the timeline " + tlname + "? This action cannot be undone.");
        Optional<ButtonType> option = alert.showAndWait();
        if(option.isPresent() && option.get() == ButtonType.OK){
            return true;
        }
        return false;
    }



    /**
     * Initiates a new ContextMenu with two default commands: "Create new Timeline" and "Create new Timeline out of selected items.
     * The context menu is passed down to each timeline Pane and its children, where new commands are added.
     * @param t
     * @return
     */
    private ContextMenu GenerateTimelineContextMenu(TimeLinePane t) {
        //TODO: Add Move up/Move down functionality
        MenuItem menuItem_CreateNewTimeLine = new MenuItem("Create new timeline below");
        menuItem_CreateNewTimeLine.setOnAction(event -> handleCreateNewTimeLineClick());

        MenuItem menuItem_CreateNewTimeLineOutOfSelected = new MenuItem("Create new timeline out of selected items");
        menuItem_CreateNewTimeLineOutOfSelected.setOnAction(event -> handleCreateNewTimeLineOutOfSelectedClick(t));

        MenuItem menuItem_EditTimeLine = new MenuItem("Edit timeline");
        menuItem_EditTimeLine.setOnAction(event -> System.out.println("Edit to be implemented"));

        MenuItem menuItem_DeleteTimeLine = new MenuItem("Delete timeline");
        menuItem_DeleteTimeLine.setOnAction(event -> handleRemoveTimeLineClick(t));

        return new ContextMenu(
                menuItem_CreateNewTimeLine,
                menuItem_CreateNewTimeLineOutOfSelected,
                menuItem_EditTimeLine,
                menuItem_DeleteTimeLine);

        //return  new ContextMenu(menuItem_CreateNewTimeLine, menuItem_CreateNewTimeLineOutOfSelected);
    }
    //TODO: Add functionality that allows for timeline creation below an existing timeline.

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
                selectedTimeLine.getChildren().stream()
                        .map(node -> (TimeLineElement)node)
                        .forEach(tle -> tle.setSelected(false));
            }
            selectedTimeLine = timeLine;
        }
    }

    /**
     * Handles a click event on buttons/menuitems that create new Timelines
     */
    private void handleCreateNewTimeLineClick(){
        Optional<String> timeLineName = openTimeLineCreationDialog(TXT_TL_CREATION_TITLE, TXT_TL_CREATION_HEADER, TXT_TL_CREATION_TEXT, TXT_TL_TIMELINETAG_LABEL, TXT_TL_TAG_DEFAULTVAL);
        if(timeLineName.isPresent()){
            createNewCustomTimeLine(timeLineName.get(), Color.BROWN, Optional.empty());
        }
    }

    /**
     * Handles a click event on buttons/menuitems that create new Timelines with selected items
     */
    private void handleCreateNewTimeLineOutOfSelectedClick(TimeLinePane tl){
        Optional<String> newTimeLineName = openTimeLineCreationDialog(TXT_TL_CREATION_TITLE, TXT_TL_CREATION_HEADER, TXT_TL_CREATION_TEXT, TXT_TL_TIMELINETAG_LABEL, TXT_TL_TAG_DEFAULTVAL);
        if(newTimeLineName.isPresent()){
            List<TimeLineElement> tles = tl.getChildren().stream()
                    .map(node -> (TimeLineElement)node)//TODO: Maybe there's a better solution? (Should there be a separate List with the TLE in the timeline?)
                    .filter(tle -> ((TimeLineElement)tle).isSelected())
                    .collect(Collectors.toList());
            createNewCustomTimeLine(newTimeLineName.get(), Color.BROWN, Optional.of(tles));
        }
    }
    //TODO: Let user choose from colors!

    private void handleEditTimeLineClick(TimeLinePane tl){
        if(tl.getClass() == CommentTimeLinePane.class){
            Optional<String> newTimeLineName = openTimeLineCreationDialog(TXT_TL_EDIT_TITLE, TXT_TL_EDIT_HEADER, TXT_TL_EDIT_TEXT, TXT_TL_TIMELINETAG_LABEL, tl.getTimeLineName());
            if(newTimeLineName.isPresent()){
                tl.setTimeLineName(newTimeLineName.get());
            }
        }
        else{
            openErrorDialogue(TXT_TL_EDIT_ERROR_TITLE, TXT_TL_EDIT_ERROR_HEADER, TXT_TL_ERROR_CANNOT_BE_EDITED);
        }

    }

    //region CreateNewTimeLineDialogue
    //https://code.makery.ch/blog/javafx-dialogs-official/
    //https://examples.javacodegeeks.com/desktop-java/javafx/dialog-javafx/javafx-dialog-example/
    //https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/Dialog.html#resultConverterProperty--
    private Optional<String> openTimeLineCreationDialog(String dialogTitle, String dialogHeader, String dialogText, String labelText, String defaultValue){

        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle(dialogTitle);
        dialog.setHeaderText(dialogHeader);
        dialog.setContentText(dialogText);
        dialog.setResizable(true);

        Label label1 = new Label(labelText);
        TextField text1 = new TextField(defaultValue);


        GridPane grid = new GridPane();
        grid.add(label1, 1, 1);
        grid.add(text1, 2, 1);
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
                return text1.getText();
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
            openErrorDialogue(
                    TXT_TL_CREATION_ERROR_TITLE,
                    TXT_TL_CREATION_ERROR_HEADER,
                    TXT_TL_ERROR_NAMEEMPTY);
            return false;
        }
        else if(!nameUnique){
            openErrorDialogue(
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
        List<String> timelineTags = vbox_timeLineInfoContainer.getChildren().stream()
                .filter(elem -> elem.getClass() == TimeLineWrapper.class) //TODO: Maybe there is a better solution?
                .map(tlwrapper -> ((TimeLineWrapper)tlwrapper).getTimeLinePane().getTimeLineName())
                .collect(Collectors.toList()
        );
        return !timelineTags.contains(s);
    }

    private void openErrorDialogue(String title, String header, String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
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

    //endregion
}