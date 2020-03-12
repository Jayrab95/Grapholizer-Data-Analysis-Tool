package New.CustomControls.Containers;

import New.CustomControls.TimeLine.SegmentationPane;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class SimpleTimeLineWrapper extends HBox {
    private final SegmentationPane tl;
    public SimpleTimeLineWrapper(SegmentationPane tl){
        this.tl = tl;
        Label  l =new Label(tl.getTimeLineName());
        l.setMinWidth(150);
        getChildren().addAll(l, tl);
        setPadding(new Insets(5,0,5,0));
    }
    SegmentationPane getTimeLinePane(){return tl;}
}
