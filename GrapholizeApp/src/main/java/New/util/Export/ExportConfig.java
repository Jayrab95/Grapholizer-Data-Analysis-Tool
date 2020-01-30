package New.util.Export;

import New.Characteristics.Characteristic;

import java.util.List;

public class ExportConfig {
    public final List<String> participantID;
    public final List<String> topicSetIDs;
    public final List<Characteristic> characteristicList;

    public ExportConfig(List<String> participantID, List<String> topics, List<Characteristic> characteristicList) {
        this.participantID = participantID;
        this.topicSetIDs = topics;
        this.characteristicList = characteristicList;
    }
}
