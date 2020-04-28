package New.Model;


import New.Model.Entities.Project;
import New.Observables.ObservablePage;
import New.Observables.ObservableParticipant;
import New.Observables.ObservableProject;
import New.util.ZipHelper;

import java.io.IOException;

/**
 * The session object stores a reference to the observable singleton objects. it is created the
 * first time a project or input file is loaded.
 */
public class Session {

    private final ObservableProject activeProject;
    private final ObservableParticipant activeParticipant;
    private final ObservablePage activePage;
    private ZipHelper z_Helper;

    public Session(Project p){
        this.activeProject = new ObservableProject(p);
        this.activeParticipant = new ObservableParticipant(p.getParticipant(p.getParticipantIDs().iterator().next()));
        this.activePage = new ObservablePage(activeParticipant.getPage(0));
    }

    /**
     * Retrieves the active project. Additionally can also cause a cleanup, if the save function
     * calls this method.
     * @param cleanUp boolean which denotes whether a clean up needs to be performed.
     * @return the active project
     */
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

    /**
     * Sets a new active project. Note that this implicitly also causes a participant and page change.
     * @param p the new project
     */
    public void setProject(Project p){
        activeProject.setInnerProject(p);
    }

    /**
     * Cleans up the project by removing any loose ids from the page's segmentation map and the
     * segment's annotation maps.
     */
    public void cleanUp(){
        activeProject.cleanUp();
    }


    public ZipHelper getZ_Helper() {
        return z_Helper;
    }

    public void setZ_Helper(ZipHelper z_Helper) throws IOException {
       /*if(this.z_Helper!=null){
            this.z_Helper.cleanUp();
        }*/
        this.z_Helper = z_Helper;
    }
}
