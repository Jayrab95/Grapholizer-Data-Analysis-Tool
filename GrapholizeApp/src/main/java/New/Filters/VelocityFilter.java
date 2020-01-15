package New.Filters;

import New.Characteristics.CharacteristicVelocityAverage;
import New.Model.Entities.Dot;
import New.Model.Entities.Stroke;
import New.Observables.ObservableDot;
import New.Observables.ObservablePage;
import New.Observables.ObservableStroke;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class VelocityFilter extends Filter{

    List<List<Double>> velocitySteps;
    Optional<Double> maxVelocity;

    public VelocityFilter(ObservablePage p) {
        super("Velocity Filter", p);
    }

    @Override
    public void applyFilter() {
        if(maxVelocity.isPresent()){
            List<ObservableStroke> strokes = p.getObservableStrokes();
            for (int i = 0; i < strokes.size(); i++) {
                List<ObservableDot> d = strokes.get(i).getObservableDots();
                for (int j = 0; j < d.size(); j++) {
                    double velocity = velocitySteps.get(i).get(j % velocitySteps.get(i).size());
                    double norm = velocity / maxVelocity.get();
                    if(norm < 0){
                        System.out.println("uh oh");
                    }
                    Color c = new Color(0, norm, norm , 1);
                    d.get(j).setColor(c);
                }

            }
        }
    }

    @Override
    public void removeFilter() {
        for(ObservableStroke s : p.getObservableStrokes()){
            for(ObservableDot d : s.getObservableDots()){
                d.setColor(Color.BLACK);
            }
        }
    }

    @Override
    public void calculateMetrics(ObservablePage p) {
        this.velocitySteps = new ArrayList<>();
        for(Stroke s : p.getAllStrokes()){
            velocitySteps.add(CharacteristicVelocityAverage.getVelocitySteps(s.getDots()));
        }
        maxVelocity = velocitySteps.stream()
                .flatMap(doubles -> doubles.stream())
                .max(Comparator.comparingDouble(Double::doubleValue));
    }
}
