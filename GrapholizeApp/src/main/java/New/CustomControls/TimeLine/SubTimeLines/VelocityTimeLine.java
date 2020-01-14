package New.CustomControls.TimeLine.SubTimeLines;

import New.Characteristics.CharacteristicVelocityAverage;
import New.CustomControls.TimeLine.TimeLinePane;
import New.Model.Entities.Dot;
import New.Observables.ObservablePage;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.shape.Line;

import java.util.*;

public class VelocityTimeLine extends DetailTimeLine {

    public VelocityTimeLine(double totalLength, double height, DoubleProperty scaleProp, StringProperty name, ObservablePage p, String topic) {
        super(totalLength, height, scaleProp, name, p, topic);

    }

    @Override
    protected void setUp() {
        List<List<Dot>> dotSections = page.getDotSectionsForAnnotations(topic);

        List<List<Double>> velocitySteps = new ArrayList<>();
        for(List<Dot> dotSection : dotSections){
            velocitySteps.add(CharacteristicVelocityAverage.getVelocitySteps(dotSection));
        }

        Optional<Double> maxVelocity = velocitySteps.stream()
                .flatMap(doubles -> doubles.stream())
                .max(Comparator.comparingDouble(Double::doubleValue));

        if(maxVelocity.isPresent()){
            for (int i = 0; i < dotSections.size(); i++) {
                List<Dot> dotSection = dotSections.get(i);
                List<Double> velocityStepsForDotSection = velocitySteps.get(i);
                //A dot section needs at least 2 dots so that a line can be drawn
                if(dotSection.size() >=2){
                    for (int j = 0; j < dotSection.size()-1; j++) {
                        Dot d1 = dotSection.get(j);
                        Dot d2 = dotSection.get(j+1);
                        double velocityNorm1 = velocityStepsForDotSection.get(j) / maxVelocity.get();
                        double velocityNorm2 = velocityStepsForDotSection.get((j+1) % velocityStepsForDotSection.size()) / maxVelocity.get();
                        Line l = new Line(
                                (d1.getTimeStamp()) * scale.get(),
                                getHeight() - (velocityNorm1 * getHeight()),
                                (d2.getTimeStamp()) * scale.get(),
                                getHeight() - (velocityNorm2 * getHeight())
                        );
                        getChildren().add(l);
                    }
                }
            }
        }



    }
}
