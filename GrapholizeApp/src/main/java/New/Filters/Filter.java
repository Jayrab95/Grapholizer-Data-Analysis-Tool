package New.Filters;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.paint.Color;

public abstract class Filter {
    protected BooleanProperty filterActiveProperty;
    protected final String filterName;

    public Filter(String filterName){
        filterActiveProperty = new SimpleBooleanProperty(false);
        this.filterName = filterName;
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

    public abstract Color applyFilter(Color c);
}
