package New.CustomControls.Containers;

import New.CustomControls.TimeLine.TimeLinePane;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class SimpleTimeLineWrapper extends HBox {
    private final TimeLinePane tl;
    public SimpleTimeLineWrapper(TimeLinePane tl){
        this.tl = tl;
        Label  l =new Label(tl.getTimeLineName());
        l.setMinWidth(150);
        getChildren().addAll(l, tl);
        setPadding(new Insets(5,0,5,0));
    }
    TimeLinePane getTimeLinePane(){return tl;}
}
