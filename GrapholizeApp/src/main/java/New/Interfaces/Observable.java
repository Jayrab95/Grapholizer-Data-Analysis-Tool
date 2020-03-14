package New.Interfaces;

import New.Interfaces.Observer.Observer;
@Deprecated
/**
 * The Observable interface is outdated. The tool uses the JavaFX properties to implement observable functionality.
 */
public interface Observable {
    void addObserver(Observer obs);
    void removeObserver(Observer obs);
    void notifyObservers();
}
