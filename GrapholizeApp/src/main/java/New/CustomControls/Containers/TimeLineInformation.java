package New.CustomControls.Containers;

import New.CustomControls.TimeLine.SegmentationPane;
import New.Interfaces.Observer.Observer;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class TimeLineInformation extends VBox {
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


    public TimeLineInformation(SegmentationPane tl){
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
        getChildren().addAll(lbl_timeLineName);
    }
}
