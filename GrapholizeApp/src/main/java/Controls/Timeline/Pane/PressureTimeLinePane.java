package Controls.Timeline.Pane;


import Model.Entities.Dot;

import Model.Entities.TimeLineElement;

import Model.StrokesModel;
import Model.TimeLinesModel;
import Observables.ObservableStroke;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.List;

/**
 * A PressureTimeLinePane is a subTimeLine which visualizes the pressure applied with each stroke for the time areas marked with
 * the TimeLineElements on the parentTimeline
 */
public class PressureTimeLinePane extends SubTimeLinePane {

    public PressureTimeLinePane(String timeLineName, double width, double height, double scale, Color c, TimeLinePane parent, List<ObservableStroke> strokes) {
        super(timeLineName, width, height, scale, c, parent);
        this.timeLinesModel = new TimeLinesModel(new StrokesModel(strokes), scale);
        draw();
    }

    /**
     * Gets all relevant dots from the StrokesModel and draws the pressure as a graph.
     */
    private void draw(){
        List<TimeLineElement> parentTimeLineElements = parentTimeLine.timeline.getTimeLineElements();
        List<List<Dot>> reqDots = timeLinesModel.getStrokesModel().getDotSectionsForElements(parentTimeLineElements);
            for(List<Dot> dots : reqDots){
                //At least 2 dots are required so that a line can be drawn
                if(dots.size() >=2){
                    for(int i = 0; i < dots.size() - 1; i++){
                        Dot d1 = dots.get(i);
                        Dot d2 = dots.get(i + 1);
                        Line l = new Line(
                                (d1.getTimeStamp()) * scale,
                                getHeight() - (d1.getForce() * getHeight()),
                                (d2.getTimeStamp()) * scale,
                                getHeight() - (d2.getForce() * getHeight())
                        );
                        getChildren().add(l);
                    }
                }
            }
    }





}
