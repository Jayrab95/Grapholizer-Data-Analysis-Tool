package New.Observables;

import New.Filters.Filter;
import New.Interfaces.Observer.FilterObserver;

import java.util.LinkedList;
import java.util.List;

public class ObservableFilterCollection {
    private Filter[] filters;


    public ObservableFilterCollection(Filter... filters){
        this.filters = filters;
    }

    public Filter[] getFilters() {
        return filters;
    }

}
