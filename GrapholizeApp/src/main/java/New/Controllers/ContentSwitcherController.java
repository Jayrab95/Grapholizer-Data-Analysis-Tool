package New.Controllers;


import New.Observables.ObservablePage;
import New.Observables.ObservableParticipant;
import New.Observables.ObservableProject;

/**
 * The ContentSwitcherController takes calls from the ContentSwitcher graphical component and is responsible
 * for switching the active participant and page.
 */
public class ContentSwitcherController {

    private ObservableProject project;
    private ObservableParticipant participant;
    private ObservablePage page;

    public ContentSwitcherController(ObservableProject project, ObservableParticipant participant, ObservablePage page) {
        this.project = project;
        this.participant = participant;
        this.page = page;
    }

    /**
     * Switches the active participant by using the participant key as a lookup value.
     * Note that this will implicitly cause a page change, as the new active page will be set to page 1 of this
     * participant
     * @param participantKey the participant key of the new, active participant
     */
    public void setParticipant(String participantKey){
        participant.setParticipant(project.getParticipant(participantKey));
    }

    /**
     * Sets the active page to the page with the given index
     * @param index the index of the new active page
     */
    public void setPage(int index){
        page.setPage(participant.getPage(index));
    }
}
