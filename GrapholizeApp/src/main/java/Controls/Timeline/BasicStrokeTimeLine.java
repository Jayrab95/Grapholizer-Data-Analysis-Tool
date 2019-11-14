package Controls.Timeline;

import Controllers.MainSceneController;
import Controls.TimelineElement.FillerTimeLineElement;
import Controls.TimelineElement.StrokeTimeLineElement;
import Model.Entities.Stroke;
import Observables.ObservableStroke;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import java.util.List;

public class BasicStrokeTimeLine extends TimeLine {

    private List<ObservableStroke> strokes;
    private MainSceneController c;

    public BasicStrokeTimeLine(List<ObservableStroke> s, double height, MainSceneController c){
        this.strokes = s;
        long totalDuration = strokes.get(strokes.size()-1).getTimeEnd() - strokes.get(0).getTimeStart();
        this.c = c;
        setWidth(totalDuration);
        setHeight(height);
        initTimeLine();
    }

    private void initTimeLine(){
        long strokesStart = strokes.get(0).getTimeStart();
        double lastEnd = 0;
        for (ObservableStroke s : strokes){
            double startDelta = (s.getTimeStart() - strokesStart) * c.getTimeLineScale();
            double endDelta = (s.getTimeEnd() - strokesStart) * c.getTimeLineScale();
            StrokeTimeLineElement stle = new StrokeTimeLineElement(startDelta,endDelta, getHeight(), this, s);
            stle.setOnMouseClicked(new EventHandler<MouseEvent>()
            {
                @Override
                public void handle(MouseEvent t) {
                    timeLineClick(stle.getElementStroke());
                }
            });
            getChildren().add(new FillerTimeLineElement(lastEnd, startDelta, getHeight(), this));
            getChildren().add(stle);
            lastEnd = endDelta;
        }
    }



    private void timeLineClick(ObservableStroke s){
        if(!s.isSelected()) {
            for (ObservableStroke st : strokes) {
                //TODO: This will cause a separate redraw.
                st.setSelected(false);
            }
        }
        s.setSelected(!s.isSelected());
        //c.reDraw();
        /*
        Highlight the stroke
        => Set selected on stroke element to true.
        => Cause a redraw of teh canvas.
        Perhaps also change the color of the element a bit.

         */

    }

    /*
            setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent t) {
                timeLineClick();
            }
        });
     */
}
