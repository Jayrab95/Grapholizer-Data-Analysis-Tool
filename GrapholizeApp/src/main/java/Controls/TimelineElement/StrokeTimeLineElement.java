package Controls.TimelineElement;

import Model.Entities.Stroke;
import Observables.ObservableStroke;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class StrokeTimeLineElement extends TimeLineElement {
    ObservableStroke s;
    public StrokeTimeLineElement(double tStart, double tEnd, double parentHeight, Color c, ObservableStroke s) {
        super(tStart, tEnd, parentHeight, c);
        this.s = s;
    }



    public ObservableStroke getElementStroke(){
        return this.s;
    }


    @Override
    protected void handleMouseClick(MouseEvent e) {
        //Todo: instead of overwriting click of parent, the click event is passed over from the timeline itself. Thisway, the entire list can be updated as well.
        System.out.println("HandleMouseClick in StrokeTimeLineElement");
        s.toggleSelected();
        /*
        if(!s.isSelected()) {
            for (ObservableStroke st : strokes) {
                //TODO: This will cause a separate redraw.
                st.setSelected(false);
            }
        }
        s.setSelected(!s.isSelected());

         */
    }
}
