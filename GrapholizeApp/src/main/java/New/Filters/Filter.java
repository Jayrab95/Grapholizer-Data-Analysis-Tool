package New.Filters;

import New.Observables.ObservablePage;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.paint.Color;

public abstract class Filter {
    protected BooleanProperty filterActiveProperty;
    protected final String filterName;
    protected final ObservablePage p;

    public Filter(String filterName, ObservablePage p){
        filterActiveProperty = new SimpleBooleanProperty(false);
        filterActiveProperty.addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                applyFilter();
            } else {
                removeFilter();
            }
        });
        this.filterName = filterName;
        this.p = p;
        p.getPageProperty().addListener((observable, oldValue, newValue) -> calculateMetrics(p));
        calculateMetrics(p);
    }

    public String getFilterName(){
        return filterName;
    }

    public boolean isActive(){
        return filterActiveProperty.get();
    }

    public BooleanProperty getFilterActiveProperty(){
        return this.filterActiveProperty;
    }

    public abstract void applyFilter();

    public abstract void removeFilter();

    public abstract void calculateMetrics(ObservablePage p);
}
