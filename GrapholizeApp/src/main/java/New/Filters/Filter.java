package New.Filters;

import New.Model.Entities.Page;
import New.Observables.ObservablePage;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.paint.Color;

public abstract class Filter {
    protected BooleanProperty filterActiveProperty;
    protected ObjectProperty<Page> pageProperty;
    protected final String filterName;
    protected final ObservablePage p;

    public Filter(String filterName, ObservablePage p){
        this.filterActiveProperty = new SimpleBooleanProperty(false);
        this.filterActiveProperty.addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                applyFilter();
            } else {
                removeFilter();
            }
        });
        this.filterName = filterName;
        this.p = p;
        this.pageProperty = p.getPageProperty();
        this.pageProperty.addListener((observable, oldValue, newValue) -> handlePageChange(p));
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

    protected void handlePageChange(ObservablePage p){
        calculateMetrics(p);
        if(isActive()){
            applyFilter();
        }
    }
    public abstract void applyFilter();

    public abstract void removeFilter();

    public abstract void calculateMetrics(ObservablePage p);


}
