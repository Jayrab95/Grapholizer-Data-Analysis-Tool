package New.Interfaces.Observer;

import New.Model.ObservableModel.ObservableProject;

public interface ProjectObserver {
    void update(ObservableProject sender);
}
