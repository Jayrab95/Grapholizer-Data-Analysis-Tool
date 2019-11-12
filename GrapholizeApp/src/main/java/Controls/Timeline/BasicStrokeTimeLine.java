package Controls.Timeline;

import Controls.TimelineElement.FillerTimeLineElement;
import Controls.TimelineElement.StrokeTimeLineElement;
import Model.Stroke;

import java.util.List;

public class BasicStrokeTimeLine extends TimeLine {

    private List<Stroke> strokes;
    public BasicStrokeTimeLine(List<Stroke> s, double height){
        this.strokes = s;
        long totalDuration = strokes.get(strokes.size()-1).getTimeEnd() - strokes.get(0).getTimeStart();
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
            getChildren().add(new FillerTimeLineElement(lastEnd, startDelta, getHeight()));
            getChildren().add(new StrokeTimeLineElement(startDelta,endDelta, getHeight(), s));
            lastEnd = endDelta;
        }
    }
}
