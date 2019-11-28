package Controls.Timeline.Pane;

import Controls.TimelineElement.TimeLineElementRect;
import Model.Entities.Dot;
import Model.Entities.Stroke;
import Model.Entities.TimeLineElement;
import Observables.ObservableStroke;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.List;
import java.util.stream.Collectors;

public class PressureTimeLinePane extends TimeLinePane {
    private List<ObservableStroke> strokes;
    private TimeLinePane parentPane;

    public PressureTimeLinePane(String timeLineName, double width, double height, double scale, Color c, List<ObservableStroke> strokes, TimeLinePane parent) {
        super(timeLineName, width, height, scale, c);
        this.strokes = strokes;
        this.parentPane = parent;
        draw();
    }

    private void draw(){
        List<TimeLineElement> parentTimeLineElements = parentPane.timeline.getTimeLineElements();

        if(parentTimeLineElements.size() > 0 && strokes.size() > 0){
            double lowerBound = parentTimeLineElements.get(0).getTimeStart() / scale;
            double upperBound = parentTimeLineElements.get(parentTimeLineElements.size()-1).getTimeStop() / scale;

            //TODO: Warning. Currently the timestamp in the dots  uses the longformat. this needs to be changed once the timstamps start from 0
            //Once this is done, simply remove the -strokesStart in the filter.
            double strokesStart = strokes.get(0).getTimeStart();
            List<Dot> requiredDots = strokes.stream()
                    .map(observableStroke -> observableStroke.getDots())
                    .flatMap(dots -> dots.stream()
                    .filter(dot -> dot.getTimeStamp() - strokesStart >= lowerBound && dot.getTimeStamp() - strokesStart<= upperBound))
                    .collect(Collectors.toList());

            List<List<Dot>> requiredDots2 = strokes.stream()
                    .map(observableStroke -> observableStroke.getDots())
                    .map(dots -> dots.stream()
                            .filter(dot -> dot.getTimeStamp() - strokesStart >= lowerBound && dot.getTimeStamp() - strokesStart<= upperBound)
                            .collect(Collectors.toList()))
                    .collect(Collectors.toList());




            //At least 2 dots are required so that a line can be drawn
            for(List<Dot> dots : requiredDots2){
                if(dots.size() >=2){
                    for(int i = 0; i < requiredDots.size() - 1; i++){
                        Dot d1 = requiredDots.get(i);
                        Dot d2 = requiredDots.get(i + 1);
                        Line l = new Line(
                                (d1.getTimeStamp() - strokesStart) * scale,
                                d1.getForce() * getHeight(),
                                (d2.getTimeStamp() - strokesStart) * scale,
                                d2.getForce() * getHeight()
                        );
                        getChildren().add(l);
                    }
                }
            }



        }


    }





}
