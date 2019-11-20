package Controls.Container;

import Controls.Timeline.Depricated.TimeLine;
import Interfaces.Observer;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

/*
* The timeline wrapper puts together the timeline with its TimelineInformation component into an HBox
*/
public class TimeLineWrapper extends HBox {
    private TimeLine tl;
    private TimeLineInformation tli;

    private class TimeLineInformation extends VBox{
        private int index;

        private Button btn_addNewTimeline;
        private Button btn_moveTimelineUp;
        private Button btn_moveTimelineDown;
        private Button btn_deleteTimeLine;
        private Button btn_editTimeLine;
        private Label lbl_timeLineName;

        private List<Observer> observers;


        public TimeLineInformation(String timeLineName, int index){
            setUpButtons();
            setUpLabel(timeLineName);
            this.index = index;
        }

        private void setUpButtons(){
            btn_addNewTimeline = new Button("+");
            btn_moveTimelineUp = new Button("^");
            btn_moveTimelineDown = new Button("v");
            btn_deleteTimeLine = new Button("-");
            btn_editTimeLine = new Button("E");



        }

        private void setUpButtonEvents(){
            btn_moveTimelineUp.setOnAction(a -> moveUp());
        }

        private void setUpLabel(String txt){
            lbl_timeLineName = new Label(txt);
        }

        private void moveUp(){
            //Adjust index
        }


    }
}
