package New.Interfaces.Observer;

import New.Model.ObservableModel.ObservableTimeLine;

public interface TimeLineObserver {
    void update(ObservableTimeLine sender);
}
