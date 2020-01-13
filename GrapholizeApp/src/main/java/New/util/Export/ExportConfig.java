package New.util.Export;

import New.Characteristics.Characteristic;

import java.util.List;

public class ExportConfig {
    List<String> participantID;
    List<String> topics;
    List<Characteristic> characteristicList;

    public ExportConfig(List<String> participantID, List<String> topics, List<Characteristic> characteristicList) {
        this.participantID = participantID;
        this.topics = topics;
        this.characteristicList = characteristicList;
    }
}
