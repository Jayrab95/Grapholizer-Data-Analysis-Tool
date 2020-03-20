package New.Observables;

import New.Filters.Filter;

public class ObservableFilterCollection {
    private Filter[] filters;


    public ObservableFilterCollection(Filter... filters){
        this.filters = filters;
    }

    public Filter[] getFilters() {
        return filters;
    }

}
