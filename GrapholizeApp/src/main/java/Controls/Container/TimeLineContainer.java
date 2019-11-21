package Controls.Container;

import Controls.Timeline.Pane.TimeLinePane;
import Interfaces.Observer;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

//https://stackoverflow.com/questions/17761415/how-to-change-order-of-children-in-javafx for children swapping

/*Idea: The TLContainer replaces the current scrollpane with timelines. When adding a new timeline, the container wraps it
into a TimeLineWrapper which contains the visual timeline and Timeline information (name and some control buttons) and then adds it into
the VBox that has all the timelines.
 */
public class TimeLineContainer extends HBox {

    //Left side contains the Timeline information and buttons. Right side contains the timeline.
    //Downside of doing this split approach: Timelines now consist of 2 separate nodes that need to be separately managed...
    //private SplitPane splitPane_splitContainer;
    private VBox vbox_timeLineInfoContainer;
    private ScrollPane scrollpane_timeLines;
    private VBox vbox_timeLines;

    public TimeLineContainer(){
        setup();
    }

    private void setup(){
        vbox_timeLineInfoContainer = new VBox();
        vbox_timeLines = new VBox();

        scrollpane_timeLines = new ScrollPane();
        scrollpane_timeLines.setContent(vbox_timeLines);
        getChildren().addAll(vbox_timeLineInfoContainer, vbox_timeLines);
    }

    public void addTimeLine(TimeLinePane tl){
        vbox_timeLineInfoContainer.getChildren().add(new TimeLineInformation(tl));
        vbox_timeLines.getChildren().add(tl);
    }

    public void removeTimeLine(TimeLineInformation source){
        //Ask if TL should actually be removed
        vbox_timeLineInfoContainer.getChildren().remove(source);

    }

    public VBox getTimeLines(){
        return vbox_timeLines;
    }
    public VBox getTimeLineInfos(){
        return vbox_timeLineInfoContainer;
    }

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
}
