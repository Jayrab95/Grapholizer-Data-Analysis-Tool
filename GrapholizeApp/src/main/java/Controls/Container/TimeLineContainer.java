package Controls.Container;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;

//https://stackoverflow.com/questions/17761415/how-to-change-order-of-children-in-javafx for children swapping

/*Idea: The TLContainer replaces the current scrollpane with timelines. When adding a new timeline, the container wraps it
into a TimeLineWrapper which contains the visual timeline and Timeline information (name and some control buttons) and then adds it into
the VBox that has all the timelines.
 */
public class TimeLineContainer extends ScrollPane {

    //Left side contains the Timeline information and buttons. Right side contains the timeline.
    //Downside of doing this split approach: Timelines now consist of 2 separate nodes that need to be separately managed...
    private SplitPane splitPane_splitContainer;
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

        splitPane_splitContainer = new SplitPane(vbox_timeLineInfoContainer, scrollpane_timeLines);
        setContent(splitPane_splitContainer);
    }

    public void addTimeLine(){

    }
}
