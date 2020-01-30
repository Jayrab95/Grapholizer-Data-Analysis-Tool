package New.util.Export;

import New.Interfaces.IExporter;
import New.Model.Entities.*;

import java.io.IOException;
import java.rmi.server.ExportException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVExporter implements IExporter {
    private Map<String,CSVTableBuilder> csvBuilders;
    private Project project;
    private ExportConfig config;
    public CSVExporter() {
        csvBuilders = new HashMap<>();
    }
    @Override
    public boolean export(String filePath, Project proj, ExportConfig config) throws IOException, ExportException {
        proj.getTopicSetIDs().forEach(ID -> csvBuilders.put(ID, new CSVTableBuilder()));
        csvBuilders.forEach((k, csvBuilder) -> {
            csvBuilder.addColumnHeader(k);
        });
        project = proj;
        this.config = config;
        proj.getParticipantsMap().forEach((partID,part) -> processParticipant(part));
        return false;
    }

    private void processParticipant(Participant part) {
        part.getPages().forEach(page -> processPage(part,page));
    }

    private void processPage(Participant part, Page page) {
        page.getTimeLines().forEach((topicSetID, segmentations) -> {
            processSegmentation(part.getID(), topicSetID, segmentations, csvBuilders.get(topicSetID));
        });
    }

    private void processSegmentation(String partID, String topicSetID
            , List<Segment> segmentations, CSVTableBuilder tableBuilder){
        segmentations.forEach(segment -> {
            int index = tableBuilder.addRow(partID);
            addSegmentToCSV(segment, tableBuilder, index, topicSetID);
        });
    }

    private void addSegmentToCSV(Segment seg, CSVTableBuilder builder, int rowIndex, String topicSetId) {
        //initialize the headers if it has not been done yet
        initializeHeaders(builder, topicSetId);
        //Add all Topics of the segment to the csv start with the main topic
        TopicSet topicSet = project.getTopicSet(topicSetId);
        
        //Calculate all Characteristics on the Segment
        //add them to CSV
    }

    private void addTopicsToCSV(int rowIndex, Topic top) {

    }

    private void addCharacteristicsToCSV(int rowIndex, ExportConfig config) {

    }

    private void initializeHeaders(CSVTableBuilder builder, String topicSetID) {
        if(!builder.hasInitializedHeaders()) {
            //Get Topics
            TopicSet topicSet = project.getTopicSet(topicSetID);
            builder.addColumnHeader(topicSet.getMainTopicID());
            topicSet.getTopics().forEach(topic -> {
                if(topicSet.getMainTopicID() != topic.getTopicID()) builder.addColumnHeader(topic.getTopicID());
            });
            //add all Characteristics
            config.characteristicList.forEach(characteristic -> builder.addColumnHeader(characteristic.getName()));
        }
    }
}
