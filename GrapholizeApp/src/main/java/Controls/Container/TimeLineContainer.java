package Controls.Container;

import Controls.Timeline.Depricated.CommentTimeLine;
import Controls.Timeline.Depricated.TimeLine;
import Controls.Timeline.Pane.CommentTimeLinePane;
import Controls.Timeline.Pane.TimeLinePane;
import Controls.TimelineElement.CommentTimeLineElement;
import Controls.TimelineElement.TimeLineElement;
import Interfaces.Observable;
import Interfaces.Observer;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

//https://stackoverflow.com/questions/17761415/how-to-change-order-of-children-in-javafx for children swapping

/*Idea: The TLContainer replaces the current scrollpane with timelines. When adding a new timeline, the container wraps it
into a TimeLineWrapper which contains the visual timeline and Timeline information (name and some control buttons) and then adds it into
the VBox that has all the timelines.
 */
public class TimeLineContainer extends HBox implements Observer {

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
        InitiateContextMenu(tl);
        tl.setOnMouseClicked(event -> handleTimeLineClick(event, tl));

        vbox_timeLineInfoContainer.getChildren().add(new TimeLineWrapper(tl, new TimeLineInformation(tl)));
        vbox_timeLineInfoContainer.getChildren().add(btn_CreateNewTimeLine);
    }

    public void removeTimeLine(TimeLineInformation source){
        //Ask if TL should actually be removed
        vbox_timeLineInfoContainer.getChildren().remove(source);
    }

    //region Getters
    public VBox getTimeLines(){
        return vbox_timeLines;
    }
    public VBox getTimeLineInfos(){
        return vbox_timeLineInfoContainer;
    }
    //endregion

    /**
     * Initiates a new ContextMenu with two default commands: "Create new Timeline" and "Create new Timeline out of selected items.
     * The context menu is passed down to each timeline Pane and its children, where new commands are added.
     * @param t
     * @return
     */
    protected void InitiateContextMenu(TimeLinePane t) {
        //TODO: Add Move up/Move down functionality
        MenuItem menuItem_CreateNewTimeLine = new MenuItem("Create new timeline below");
        menuItem_CreateNewTimeLine.setOnAction(event -> handleCreateNewTimeLineClick());

        MenuItem menuItem_CreateNewTimeLineOutOfSelected = new MenuItem("Create new timeline out of selected items");
        menuItem_CreateNewTimeLineOutOfSelected.setOnAction(event -> handleCreateNewTimeLineOutOfSelectedClick(t));

        t.getContextMenu().getItems().addAll(menuItem_CreateNewTimeLine, menuItem_CreateNewTimeLineOutOfSelected);

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
        Optional<String> timeLineName = openTimeLineCreationDialog();
        if(timeLineName.isPresent()){
            createNewCustomTimeLine(timeLineName.get(), Color.BROWN, Optional.empty());
        }
    }


    /**
     * Handles a click event on buttons/menuitems that create new Timelines with selected items
     */
    private void handleCreateNewTimeLineOutOfSelectedClick(TimeLinePane tl){
        Optional<String> newTimeLineName = openTimeLineCreationDialog();
        if(newTimeLineName.isPresent()){
            List<TimeLineElement> tles = tl.getChildren().stream()
                    .map(node -> (TimeLineElement)node)//TODO: Maybe there's a better solution? (Should there be a separate List with the TLE in the timeline?)
                    .filter(tle -> ((TimeLineElement)tle).isSelected())
                    .collect(Collectors.toList());
            createNewCustomTimeLine(newTimeLineName.get(), Color.BROWN, Optional.of(tles));
        }
    }
    //TODO: Let user choose from colors!

    //region CreateNewTimeLineDialogue
    //https://code.makery.ch/blog/javafx-dialogs-official/
    //https://examples.javacodegeeks.com/desktop-java/javafx/dialog-javafx/javafx-dialog-example/
    //https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/Dialog.html#resultConverterProperty--
    private Optional<String> openTimeLineCreationDialog(){

        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Create new TimeLine");
        dialog.setHeaderText("Create a new timeline by giving it a tag. (The tag must be unique and cannot be empty)");
        dialog.setResizable(true);

        Label label1 = new Label("Timeline tag (must be unique): ");
        Label label_Error = new Label();
        TextField text1 = new TextField();

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
                    "Timeline creation error",
                    "Error while creating timeline",
                    "The timeline name must not be empty.");
            return false;
        }
        else if(!nameUnique){
            openErrorDialogue(
                    "Timeline creation error",
                    "Error while creation timeline",
                    "The entered timeline tag already exists. Please enter a unique tag.");
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

    @Override
    public void update(Observable sender) {

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
