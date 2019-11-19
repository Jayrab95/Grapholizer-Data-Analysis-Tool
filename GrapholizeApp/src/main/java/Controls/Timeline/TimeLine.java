package Controls.Timeline;

import Controls.TimelineElement.TimeLineElement;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import util.Selector;

public abstract class TimeLine extends HBox {

    protected String timeLineName;

    //Todo: perhaps reference style from a style sheet.
    protected String style = "-fx-padding: 10; -fx-border-style: solid inside; -fx-border-width: 2; -fx-border-insets: 5; -fx-border-radius: 5; -fx-border-color: blue;";

    protected TimeLine(String timeLineName, double height){
        this.timeLineName = timeLineName;
        setHeight(height);
        InitiateTimeLine();
    }

    private void InitiateTimeLine(){
        setStyle(style);
    }



    //Probably not necessary?
    public void add(TimeLineElement elem){

        getChildren().add(elem);

    }
}
