package New.Filters;

import New.Model.Entities.Stroke;
import New.Observables.ObservableStroke;

public abstract class StrokeFilter extends Filter {
    public StrokeFilter(String filterName) {
        super(filterName);
    }

    public abstract void applyFilter(ObservableStroke s);
}
