package New.Interfaces.structure;

import New.Model.Entities.Page;
import New.Model.Entities.Participant;

public interface IContextOperationsObservable {
    void changePage(int index);
    void changeParticipant(String partID);
    void notifyListeners();
}
