package Interfaces;

public interface Observable {
    void addObserver(Interfaces.Observer obs);
    void removeObserver(Interfaces.Observer obs);
    void notifyObservers();
}
