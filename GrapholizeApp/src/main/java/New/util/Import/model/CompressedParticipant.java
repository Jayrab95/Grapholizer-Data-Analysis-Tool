package New.util.Import.model;

import New.util.Import.model.CompressedPage;

import java.util.List;

public class CompressedParticipant {
    public String Id;

    public List<CompressedPage> Pages;

    @Override
    public String toString() {
        return "ID:: " + this.Id + " PageLength:: " + Pages.size() + "\n";
    }
}
