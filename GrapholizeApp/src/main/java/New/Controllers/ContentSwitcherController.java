package New.Controllers;


import New.Observables.ObservablePage;
import New.Observables.ObservableParticipant;
import New.Observables.ObservableProject;

public class ContentSwitcherController {

    private ObservableProject project;
    private ObservableParticipant participant;
    private ObservablePage page;

    public ContentSwitcherController(ObservableProject project, ObservableParticipant participant, ObservablePage page) {
        this.project = project;
        this.participant = participant;
        this.page = page;
    }

    public void setParticipant(String participantKey){
        participant.setParticipant(project.getParticipant(participantKey));
    }
    public void setPage(int index){
        page.setPage(participant.getPage(index));
    }
}
