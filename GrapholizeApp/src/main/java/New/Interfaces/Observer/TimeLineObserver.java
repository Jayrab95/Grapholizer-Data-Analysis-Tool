package New.Interfaces.Observer;

import New.Observables.ObservableTimeLine;

public interface TimeLineObserver {
    void update(ObservableTimeLine sender);
}
