package Model.Entities;

import util.Import.CompressedPage;
import util.Import.CompressedParticipant;

import java.util.LinkedList;
import java.util.List;

public class Participant {
    private String ID;
    private List<Page> pages;

    public Participant() {

    }

    public Participant(CompressedParticipant cp) {
        this.ID = cp.Id;
        pages = new LinkedList<>();
        for (CompressedPage cpage : cp.Pages) {
            pages.add(new Page(cpage));
        }
    }

    @Override
    public String toString() {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("ParticipantID:: ");
        sBuilder.append(this.ID);
        sBuilder.append(" Pages LENGTH:: ");
        sBuilder.append(this.pages.size());
        sBuilder.append("\n");
        return sBuilder.toString();
    }
}
