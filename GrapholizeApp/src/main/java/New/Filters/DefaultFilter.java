package New.Filters;

import New.Observables.ObservablePage;
import javafx.scene.paint.Color;

public class DefaultFilter extends Filter {
    public DefaultFilter(ObservablePage p) {
        super("No Filter", p);
    }

    @Override
    public void applyFilter() {
        p.getObservableStrokes().stream()
                .flatMap(observableStroke -> observableStroke.getObservableDots().stream())
                .forEach(observableDot -> observableDot.setColor(Color.BLACK));
    }

    @Override
    public void removeFilter() {
        p.getObservableStrokes().stream()
                .flatMap(observableStroke -> observableStroke.getObservableDots().stream())
                .forEach(observableDot -> observableDot.setColor(Color.BLACK));
    }

    @Override
    public void calculateMetrics(ObservablePage p) {

    }
}
