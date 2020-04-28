package New.Model.Entities;

import New.util.Import.model.CompressedPage;
import New.util.Import.model.CompressedParticipant;

import java.util.LinkedList;
import java.util.List;

/**
 * The Participant class represents a single participant who took part in a research experiment.
 * The Participant hold a collection of pages.
 * Note that the participant is immutable after creation.
 */
public class Participant {
    private final String ID;
    private List<Page> pages;

    /**
     * Constructor which takes an ID and a list of pages
     * @param ID
     * @param pages
     */
    public Participant(String ID, List<Page> pages) {
        this.ID = ID;
        this.pages = new LinkedList<>(pages);
    }

    /**
     * Constructor which unpacks the compressed participant and initialized the id and the page list.
     * @param cp compressed Page
     */
    public Participant(CompressedParticipant cp) {
        this.ID = cp.Id;
        pages = new LinkedList<>();
        for (CompressedPage cpage : cp.Pages) {
            pages.add(new Page(cpage));
        }
    }

    /**
     * Retrieves Page with the given id.      *
     * @param index index of the page
     * @return Page with the given id.
     */
    public Page getPage(int index) { return pages.get(index); }

    /**
     * @return String which shows the Participant id and the amount of pages held by this participant
     */
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

    public String getID() {
        return ID;
    }

    public List<Page> getPages() {
        return pages;
    }
}
