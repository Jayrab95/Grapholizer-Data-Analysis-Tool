package Controls.Timeline.Pane;

import Controls.TimelineElement.TimeLineElement;
import Model.Entities.Dot;
import Model.Entities.Stroke;
import Observables.ObservableStroke;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.List;

public class PressureTimeLinePane extends TimeLinePane {
    private List<ObservableStroke> strokes;
    private TimeLinePane parentPane;

    protected PressureTimeLinePane(String timeLineName, double width, double height, double scale, Color c, List<ObservableStroke> strokes) {
        super(timeLineName, width, height, scale, c);
        this.strokes = strokes;
    }

    private void draw(){
        List<Dot> requiredDots;
        for(Node n : parentPane.getChildren()){
            TimeLineElement e = (TimeLineElement)n;
        }
    }





}
