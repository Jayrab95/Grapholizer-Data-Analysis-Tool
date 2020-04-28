package New.util.Import.model;

import New.Model.Entities.Page;
import New.Model.Entities.Participant;
import New.util.Import.model.CompressedPage;

import java.util.LinkedList;
import java.util.List;

public class CompressedParticipant {
    public String Id;

    public List<CompressedPage> Pages;

    @Override
    public String toString() {
        return "ID:: " + this.Id + " PageLength:: " + Pages.size() + "\n";
    }

    public CompressedParticipant(Participant participant){
        Id = participant.getID();
        Pages = new LinkedList<>();
        for (Page temp : participant.getPages()) {
           Pages.add(new CompressedPage(temp));
        }
    }
}
