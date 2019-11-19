package Controls.Timeline;

import Controllers.MainSceneController;
import Controls.TimelineElement.FillerTimeLineElement;
import Controls.TimelineElement.StrokeTimeLineElement;
import Model.Entities.Stroke;
import Observables.ObservableStroke;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.List;

public class StrokeDurationTimeLine extends TimeLine {

    private List<ObservableStroke> strokes;
    private MainSceneController c;

    public StrokeDurationTimeLine(List<ObservableStroke> s, double height, MainSceneController c){
        super("Stroke Duration", height);
        this.strokes = s;
        //TODO: Idea, move this out of stroke timeline, since all timeline have the same width and height
        long totalDuration = strokes.get(strokes.size()-1).getTimeEnd() - strokes.get(0).getTimeStart();
        this.c = c;
        setWidth(totalDuration);
        setHeight(height);
        initTimeLine();
    }

    private void initTimeLine(){
        long strokesStart = strokes.get(0).getTimeStart();
        double previousStrokeEnd = 0;
        for (ObservableStroke s : strokes){
            double startDelta = (s.getTimeStart() - strokesStart) * c.getTimeLineScale();
            double endDelta = (s.getTimeEnd() - strokesStart) * c.getTimeLineScale();
            Color co = new Color(s.getColor().getR(), s.getColor().getG(), s.getColor().getB(), s.getColor().getO());
            StrokeTimeLineElement stle = new StrokeTimeLineElement(startDelta,endDelta, getHeight(), co, s);

            getChildren().add(new FillerTimeLineElement(previousStrokeEnd, startDelta, getHeight(), Color.WHITESMOKE));
            getChildren().add(stle);
            previousStrokeEnd = endDelta;
        }
    }



    //Replace this with an onclick defined in TimeLIneELement base
    private void timeLineClick(ObservableStroke s){
        if(!s.isSelected()) {
            for (ObservableStroke st : strokes) {
                //TODO: This will cause a separate redraw.
                st.setSelected(false);
            }
        }
        s.setSelected(!s.isSelected());
        /*
        Highlight the stroke
        => Set selected on stroke element to true.
        => Cause a redraw of teh canvas.
        Perhaps also change the color of the element a bit.
         */
    }

}
