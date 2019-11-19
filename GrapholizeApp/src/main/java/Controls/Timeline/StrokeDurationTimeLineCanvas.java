package Controls.Timeline;

import Controls.TimelineElement.FillerTimeLineElement;
import Controls.TimelineElement.StrokeTimeLineElement;
import Model.Entities.Stroke;
import Observables.ObservableStroke;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.List;

public class StrokeDurationTimeLineCanvas extends TimelineCanvas<StrokeTimeLineElement> {
    List<ObservableStroke> strokes;
    private float timeLineScale = 0.1f;

    //The strokeMenuItem is bound to a new clickEvent everytime a new stroke TL-ELement is rightclicked.
    private MenuItem strokeMenuItem;


    protected StrokeDurationTimeLineCanvas(String timeLineName,double width, double height, List<ObservableStroke> strokes) {
        super(timeLineName,width, height);

    }

    //TODO: Timelines need to become observers of which ever class/control saves the timeLineScale.
    private void setUpTimeLine(){
        long strokesStart = strokes.get(0).getTimeStart();
        double lastEnd = 0;
        for (ObservableStroke s : strokes){
            double startDelta = (s.getTimeStart() - strokesStart) * timeLineScale;
            double endDelta = (s.getTimeEnd() - strokesStart) * timeLineScale;
            Color c = new Color(s.getColor().getR(), s.getColor().getG(), s.getColor().getB(), s.getColor().getO());
            StrokeTimeLineElement stle = new StrokeTimeLineElement(startDelta,endDelta, getHeight(), c, s);
            elements.add(stle);
            lastEnd = endDelta;
        }
        draw();
    }

    @Override
    protected void handleMouseClick(MouseEvent e) {
        long firstTimeStamp = strokes.get(0).getTimeStart();
        for (Stroke s : strokes){
            if(e.getX() > s.getTimeStart() - firstTimeStamp && e.getX() < s.getTimeEnd() - firstTimeStamp){
                System.out.println("Found first timeStamp");
            }
        }
    }

    @Override
    protected void handleMouseDragMove(MouseEvent e) {

    }

    @Override
    protected void handleMouseRelease(MouseEvent e) {

    }

    @Override
    protected void InitiateContextMenu() {
        //MenuItem infoItem = new MenuItem("Stroke Info");
        //Create new window for info?
        contextMenu = new ContextMenu();
    }

    private void createStrokeSpecificMenuItem(Stroke s){
        MenuItem stokeInfoItem = new MenuItem("Stroke Info");
    }

}
