package Controls.Timeline.Pane;

import Controls.TimelineElement.StrokeTimeLineElementRect;
import Observables.ObservableStroke;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.List;

public class StrokeDurationTimeLinePane extends TimeLinePane {
    List<ObservableStroke> strokes;

    public StrokeDurationTimeLinePane(String timeLineName, double width, double height, double scale, List<ObservableStroke> strokes) {
        super(timeLineName,width, height, scale, Color.BLACK);
        this.strokes = strokes;
        setUpTimeLine();
    }

    //TODO: Timelines need to become observers of which ever class/control saves the timeLineScale.
    private void setUpTimeLine(){
        long strokesStart = strokes.get(0).getTimeStart();
        for (ObservableStroke s : strokes){
            //TODO: The elements need to know about the timeline scale
            //Elements need to become listeners of whichever class manages the timeline scale.
            //Their width is updated on scale change.
            //Time start and Time end should be saved as is in the TLE. The TLE should then applies the scale for the width
            double startDelta = (s.getTimeStart() - strokesStart) * scale;
            double endDelta = (s.getTimeEnd() - strokesStart) * scale;
            Color c = new Color(s.getColor().getR(), s.getColor().getG(), s.getColor().getB(), s.getColor().getO());

            StrokeTimeLineElementRect stle = new StrokeTimeLineElementRect(startDelta,endDelta, getHeight(), c, s);
            stle.setOnMouseClicked(e -> timeLineClick(e, s));
            s.addObserver(stle);
            addTimeLineElement(stle);
        }
        setOnMousePressed(e -> handleMouseClick(e));
    }

    protected void handleMouseClick(MouseEvent e) {
        //The TimelineClick is currently not used.
        //TODO: Later on, it can be used for context menu actions. (Create new timeline etc)
        System.out.println("handleMouseClick called on StrokeDurationTimeLinePane");
    }

    //Replace this with an onclick defined in TimeLIneELement base
    private void timeLineClick(MouseEvent e, ObservableStroke s){
        if(e.getButton() == MouseButton.PRIMARY){
            if(e.isControlDown()){
                s.toggleSelected();
            }
            else {
                //TODO: Current behavior: If multiple elements are selected and an already selected element is clicked on again, everything is deselected
                for (ObservableStroke st : strokes) {
                    //TODO: This will cause a separate redraw.
                    if(st != s){
                        st.setSelected(false);
                    }

                }
                s.toggleSelected();
            }
        }


        /*
        Highlight the stroke
        => Set selected on stroke element to true.
        => Cause a redraw of teh canvas.
        Perhaps also change the color of the element a bit.
         */
    }



}
