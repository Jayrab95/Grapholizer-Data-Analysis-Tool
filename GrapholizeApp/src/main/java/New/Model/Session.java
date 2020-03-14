package New.Model;


import New.Model.Entities.Project;
import New.Model.Entities.TopicSet;
import New.Observables.ObservablePage;
import New.Observables.ObservableParticipant;
import New.Observables.ObservableProject;
import New.util.ZipHelper;

import java.io.IOException;

public class Session {

    private final ObservableProject activeProject;
    private final ObservableParticipant activeParticipant;
    private final ObservablePage activePage;
    private ZipHelper z_Helper;

    public Session(Project p){
        System.out.println("Session constructor called");
        this.activeProject = new ObservableProject(p);
        this.activeParticipant = new ObservableParticipant(p.getParticipant(p.getParticipantIDs().iterator().next()));
        this.activePage = new ObservablePage(activeParticipant.getPage(0));
    }

    public ObservableProject getActiveProject(boolean cleanUp) {
        if(cleanUp){this.cleanUp();}
        return activeProject;
    }



    public ObservableParticipant getActiveParticipant() {
        return activeParticipant;
    }

    public ObservablePage getActivePage() {
        return activePage;
    }

    public void setProject(Project p){
        activeProject.setInnerProject(p);
    }

    public void cleanUp(){
        activeProject.cleanUp();
    }


    public ZipHelper getZ_Helper() {
        return z_Helper;
    }

    public void setZ_Helper(ZipHelper z_Helper) throws IOException {
        if(this.z_Helper!=null){
            this.z_Helper.cleanUp();
        }
        this.z_Helper = z_Helper;
    }



}
