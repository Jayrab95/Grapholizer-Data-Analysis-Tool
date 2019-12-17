package New.Controllers;

import New.Model.ObservableModel.ObservablePage;
import New.Model.ObservableModel.ObservableParticipant;
import New.Model.ObservableModel.ObservableProject;

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
