package New.Observables;

import New.Filters.Filter;
import New.Interfaces.Observer.FilterObserver;

import java.util.LinkedList;
import java.util.List;

public class ObservableFilterCollection {
    private Filter[] filters;
    private List<FilterObserver> observers;

    public ObservableFilterCollection(Filter... filters){
        this.filters = filters;
        this.observers = new LinkedList<>();
        for (Filter filter : filters) {
            filter.getFilterActiveProperty().addListener((observable, oldValue, newValue) -> notifyObservers());
        }
    }

    public Filter[] getFilters() {
        return filters;
    }

    public void addObserver(FilterObserver obs){
        observers.add(obs);
    }
    public void removeObserver(FilterObserver obs){
        observers.remove(obs);
    }
    public void notifyObservers(){
        for (FilterObserver observer : observers) {
            observer.update(this);
        }
    }
}
