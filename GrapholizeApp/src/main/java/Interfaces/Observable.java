package Interfaces;

public interface Observable {
    void addListener(Interfaces.Observer obs);
    void removeListener(Interfaces.Observer obs);
    void notifyListeners();
}
