package Controls.Timeline.Pane;

import Controls.TimelineElement.StrokeTimeLineElement;
import Observables.ObservableStroke;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.List;

public class StrokeDurationTimeLinePane extends TimeLinePane<StrokeTimeLineElement> {
    List<ObservableStroke> strokes;

    public StrokeDurationTimeLinePane(String timeLineName, double width, double height, double scale, List<ObservableStroke> strokes) {
        super(timeLineName,width, height, scale);
        this.strokes = strokes;
        setUpTimeLine();
    }

    //Wow....https://bugs.openjdk.java.net/browse/JDK-8089835
    //TODO: Timelines need to become observers of which ever class/control saves the timeLineScale.
    private void setUpTimeLine(){
        long strokesStart = strokes.get(0).getTimeStart();
        double lastEnd = 0;
        for (ObservableStroke s : strokes){
            double startDelta = (s.getTimeStart() - strokesStart) * scale;
            double endDelta = (s.getTimeEnd() - strokesStart) * scale;
            Color c = new Color(s.getColor().getR(), s.getColor().getG(), s.getColor().getB(), s.getColor().getO());
            StrokeTimeLineElement stle = new StrokeTimeLineElement(startDelta,endDelta, getHeight(), c, s);
            this.getChildren().add(stle);
            //elements.add(stle);
            lastEnd = endDelta;
        }
        setOnMousePressed(e -> handleMouseClick(e));
        //draw();
    }

    //@Override
    protected void handleMouseClick(MouseEvent e) {
        System.out.println("handleMouseClick called on StrokeDurationTimeLinePane");
        /*
        double x = e.getX();
        double y = e.getY();
        long firstTimeStamp = strokes.get(0).getTimeStart();
        for (ObservableStroke s : strokes){
            if(e.getX() > (s.getTimeStart() - firstTimeStamp) * scale && e.getX() < (s.getTimeEnd() - firstTimeStamp) * scale){
                System.out.println("Found Stroke. x = " + x);
                timeLineClick(s);
            }
        }

         */
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

    @Override
    protected void InitiateContextMenu() {

    }
}
