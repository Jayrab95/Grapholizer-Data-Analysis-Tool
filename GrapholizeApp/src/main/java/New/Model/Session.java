package New.Model;


import New.Model.Entities.Project;
import New.Model.ObservableModel.ObservablePage;
import New.Model.ObservableModel.ObservableParticipant;
import New.Model.ObservableModel.ObservableProject;
import New.util.ZipHelper;
import javafx.beans.property.ObjectProperty;

import java.util.HashMap;
import java.util.Map;

public class Session {

    private final ObservableProject activeProject;
    private final ObservableParticipant activeParticipant;
    private final ObservablePage activePage;
    private ZipHelper z_Helper;

    public Session(Project p){
        this.activeProject = new ObservableProject(p);
        this.activeParticipant = new ObservableParticipant(p.getParticipant(p.getParticipantIDs().iterator().next()));
        this.activePage = activeParticipant.getPage(0);
    }

    public ObservableProject getActiveProject() {
        return activeProject;
    }

    public ObservableParticipant getActiveParticipant() {
        return activeParticipant;
    }

    public ObservablePage getActivePage() {
        return activePage;
    }


    //TODO: Are these needed? The content switcher now does the switching.
    //Should content switcher receive a reference to the session instead? Or keep it the way it is?
    public void switchParticipant(String participantKey){
        activeParticipant.setParticipant(activeProject.getParticipant(participantKey));
        activePage.setPage(activeParticipant.getPage(0));
    }

    public void switchPage(int pageIndex){
        //TODO: Does there need to be a check if the index is legal?
        activePage.setPage(activeParticipant.getPage(pageIndex));
    }

    public ZipHelper getZ_Helper() {
        return z_Helper;
    }

    public void setZ_Helper(ZipHelper z_Helper) {
        this.z_Helper = z_Helper;
    }



}
