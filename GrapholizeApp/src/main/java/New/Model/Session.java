package New.Model;

import New.Model.Entities.Page;
import New.Model.Entities.Participant;
import New.Model.Entities.Project;
import New.Model.ObservableModel.ObservableActiveState;
import New.util.ZipHelper;

import java.util.HashMap;
import java.util.Map;

public class Session {
    private Project project;
    private Map<String, Participant> participantDataMap;
    private ObservableActiveState activeState;
    ZipHelper z_Helper;


    public Session() {}

    public Session(Project p){

        project = p;
    }

    public Map<String, Participant> getParticipantDataMap() {
        return participantDataMap;
    }

    public void setParticipantDataMap(HashMap<String, Participant> participantDataMap) {
        this.participantDataMap = participantDataMap;
    }

    public Participant getCurrent_participant() {
        return activeState.getActiveParticipant();
    }

    public void setCurrent_participant(Participant current_participant) {
        activeState.setActiveParticipant(current_participant);
    }

    public Page getCurrent_page() {
        return activeState.getActivePage();
    }

    public void setCurrent_page(int current_page) {
        activeState.setActivePage(current_page);
    }

    public ZipHelper getZ_Helper() {
        return z_Helper;
    }

    public void setZ_Helper(ZipHelper z_Helper) {
        this.z_Helper = z_Helper;
    }

    public ObservableActiveState getActiveState() {
        return activeState;
    }

    public Session setActiveState(ObservableActiveState activeState) {
        this.activeState = activeState;
        return this;
    }
}
