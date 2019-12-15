package New.Interfaces.Observer;

import New.Model.ObservableModel.ObservableParticipant;

public interface ParticipantObserver {
    void update(ObservableParticipant sender);
}
