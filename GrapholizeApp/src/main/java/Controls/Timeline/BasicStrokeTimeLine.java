package Controls.Timeline;

import Controllers.MainSceneController;
import Controls.TimelineElement.FillerTimeLineElement;
import Controls.TimelineElement.StrokeTimeLineElement;
import Model.Stroke;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import java.util.List;

public class BasicStrokeTimeLine extends TimeLine {

    private List<Stroke> strokes;
    private MainSceneController c;
    public BasicStrokeTimeLine(List<Stroke> s, double height, MainSceneController c){
        this.strokes = s;
        long totalDuration = strokes.get(strokes.size()-1).getTimeEnd() - strokes.get(0).getTimeStart();
        this.c = c;
        setWidth(totalDuration);
        setHeight(height);
        initTimeLine();
    }

    private void initTimeLine(){
        long strokesStart = strokes.get(0).getTimeStart();
        long lastEnd = 0;
        for (Stroke s : strokes){
            long startDelta = s.getTimeStart() - strokesStart;
            long endDelta = s.getTimeEnd() - strokesStart;
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



    private void timeLineClick(Stroke s){
        if(!s.isSelected()) {
            for (Stroke st : strokes) {
                st.setSelected(false);
            }
        }
        s.setSelected(!s.isSelected());
        c.reDraw();
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
