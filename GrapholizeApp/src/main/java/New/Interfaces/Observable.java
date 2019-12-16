package New.Interfaces;

import New.Interfaces.Observer.Observer;

public interface Observable {
    void addObserver(Observer obs);
    void removeObserver(Observer obs);
    void notifyObservers();
}
