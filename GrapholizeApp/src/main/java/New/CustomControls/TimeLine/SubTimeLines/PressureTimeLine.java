package New.CustomControls.TimeLine.SubTimeLines;

import New.CustomControls.TimeLine.SelectableTimeLinePane;
import New.CustomControls.TimeLine.TimeLinePane;
import New.Model.Entities.Dot;
import New.Observables.ObservablePage;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.shape.Line;

import java.util.List;

public class PressureTimeLine extends TimeLinePane {

    ObservablePage p;
    SelectableTimeLinePane parent;

    public PressureTimeLine(double totalLength, double height, DoubleProperty scaleProp, StringProperty name, ObservablePage p, SelectableTimeLinePane parent) {
        super(totalLength, height, scaleProp, name);
        this.p = p;
        this.parent = parent;
        setUp();
    }

    private void setUp(){
        List<List<Dot>> dots = p.getDotSectionsForElements(parent.getAnnotations());
        for(List<Dot> dotList : dots){
            //At least 2 dots are required so that a line can be drawn
            if(dotList.size() >=2){
                for(int i = 0; i < dotList.size() - 1; i++){
                    Dot d1 = dotList.get(i);
                    Dot d2 = dotList.get(i + 1);
                    Line l = new Line(
                            (d1.getTimeStamp()) * scale.get(),
                            getHeight() - (d1.getForce() * getHeight()),
                            (d2.getTimeStamp()) * scale.get(),
                            getHeight() - (d2.getForce() * getHeight())
                    );
                    getChildren().add(l);
                }
            }
        }
    }

}
