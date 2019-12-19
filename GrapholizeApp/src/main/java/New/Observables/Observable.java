package New.Observables;

import java.util.List;

public abstract class Observable<T> {
    protected List<T>observers;

    public Observable(){}

    public void addObserver(T observer){
        observers.add(observer);
    }

    public void removeObserver(T observer){
        observers.remove(observer);
    }

    protected abstract void notifyObservers();
}
