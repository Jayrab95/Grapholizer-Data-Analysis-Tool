package Controls.Timeline;

import Controls.TimelineElement.CommentFillerElement;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class CommentTimeLine extends TimeLine {


    public CommentTimeLine(String tlName, double height){
        super(tlName, height);
        getChildren().add(new CommentFillerElement(0, getWidth(), getHeight(), Color.BLACK));
    }

}
