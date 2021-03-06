package New.CustomControls.SegmentationPanes.DetailSegmentations;

import New.CustomControls.SegmentationPanes.SelectableSegmentationPane;
import New.Model.Entities.Dot;
import New.Observables.ObservablePage;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;

import java.util.List;

public class PressureSegmentation extends DetailSegmentation {

    public PressureSegmentation(double totalLength, double height, DoubleProperty scaleProp, StringProperty name, ObservablePage p, SelectableSegmentationPane parent, String topic) {
        super(totalLength, height, scaleProp, name, p, topic);
        setUp();
    }

    @Override
    protected void setUp(){
        List<List<Dot>> dotSections = page.getAllDotSectionsForTopicSet(topicSetID);
        for(List<Dot> dots : dotSections){
            //At least 2 dots are required so that a line can be drawn
            if(dots.size() >=2){
                for(int i = 0; i < dots.size() - 1; i++){
                    Dot d1 = dots.get(i);
                    Dot d2 = dots.get(i + 1);
                    DetailLine l = new DetailLine(
                            (d1.getTimeStamp()),
                            getHeight() - (d1.getForce() * getHeight()),
                            (d2.getTimeStamp()),
                            getHeight() - (d2.getForce() * getHeight()),
                            scale
                    );
                    getChildren().add(l);
                }
            }
        }
    }

}
