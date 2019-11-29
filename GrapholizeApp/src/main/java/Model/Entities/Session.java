package Model.Entities;

import util.ZipHelper;

import java.util.HashMap;

public class Session {
    private HashMap<String,Participant> participantDataMap;
    private Participant current_participant;
    private Page current_page;
    ZipHelper zHelper;
}
