package Model.Entities;

import util.ZipHelper;

import java.util.HashMap;

public class Session {
    private HashMap<String,Participant> participantDataMap;
    private Participant current_participant;
    private Page current_page;
    ZipHelper z_Helper;


    public Session() {}

    public Session(Page current_page) {
        this.current_page = current_page;
    }

    public HashMap<String, Participant> getParticipantDataMap() {
        return participantDataMap;
    }

    public void setParticipantDataMap(HashMap<String, Participant> participantDataMap) {
        this.participantDataMap = participantDataMap;
    }

    public Participant getCurrent_participant() {
        return current_participant;
    }

    public void setCurrent_participant(Participant current_participant) {
        this.current_participant = current_participant;
    }

    public Page getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(Page current_page) {
        this.current_page = current_page;
    }

    public ZipHelper getZ_Helper() {
        return z_Helper;
    }

    public void setZ_Helper(ZipHelper z_Helper) {
        this.z_Helper = z_Helper;
    }
}
