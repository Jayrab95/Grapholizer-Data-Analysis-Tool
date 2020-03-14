package New.Filters;

import New.Model.Entities.Dot;
import New.Observables.ObservableDot;
import New.Observables.ObservablePage;
import New.Observables.ObservableStroke;
import javafx.scene.paint.Color;

import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

public class PressureFilter extends Filter {
    Optional<Float> maxPressure;
    public PressureFilter(ObservablePage p) {
        super("Pressure filter", p);
    }

    @Override
    public void applyFilter() {
        if(maxPressure.isPresent()){
            for(ObservableDot oDot : p.getObservableStrokes().stream().flatMap(s -> s.getObservableDots().stream()).collect(Collectors.toList())){
                oDot.setColor(new Color(1, 0, 0, oDot.getForce() / maxPressure.get()));
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
        maxPressure = p.getAllStrokes().stream()
                .flatMap(s -> s.getDots().stream())
                .map(dot -> dot.getForce())
                .max(Comparator.comparing(Float::doubleValue));
    }
}
