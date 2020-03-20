package New.CustomControls.SegmentationPanes.DetailSegmentations;

import New.Characteristics.CharacteristicVelocityAverage;
import New.Model.Entities.Dot;
import New.Observables.ObservablePage;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;

import java.util.*;

public class VelocitySegmentation extends DetailSegmentation {

    public VelocitySegmentation(double totalLength, double height, DoubleProperty scaleProp, StringProperty name, ObservablePage p, String topic) {
        super(totalLength, height, scaleProp, name, p, topic);
        setUp();
    }

    @Override
    protected void setUp() {
        List<List<Dot>> dotSections = page.getAllDotSectionsForTopicSet(topicSetID);

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
                        DetailLine l = new DetailLine(
                                (d1.getTimeStamp()),
                                getHeight() - (velocityNorm1 * getHeight()),
                                (d2.getTimeStamp()),
                                getHeight() - (velocityNorm2 * getHeight()),
                                scale
                        );
                        getChildren().add(l);
                    }
                }
            }
        }
    }
}
