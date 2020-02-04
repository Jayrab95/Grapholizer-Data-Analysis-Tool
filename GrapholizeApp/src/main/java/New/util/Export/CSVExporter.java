package New.util.Export;

import New.Interfaces.IExporter;
import New.Model.Entities.*;
import New.util.PageUtil;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
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

        //Create the CSV for every topicset
        try {
            config.participantID.forEach((partID) -> processParticipant(proj.getParticipant(partID)));
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        //Collect the generated tables to one String
        StringBuilder generatedCSV = new StringBuilder();
        csvBuilders.forEach((s, csvTableBuilder) -> generatedCSV.append(csvTableBuilder.build()));

        //Write the file
        if(!Files.exists(Path.of(filePath))) {
            Files.createFile(Path.of(filePath));
        }
        BufferedWriter buffWriter = Files.newBufferedWriter(Path.of(filePath), StandardOpenOption.WRITE);
        buffWriter.write(generatedCSV.toString());
        buffWriter.flush();
        return true;
    }

    private void processParticipant(Participant part) {
        System.out.println("process Participant");
        part.getPages().forEach(page -> processPage(part,page));
    }

    private void processPage(Participant part, Page page) {
        System.out.println("process Page");
        page.getTimeLines().forEach((topicSetID, segmentations) -> {
            if(config.topicSetIDs.contains(topicSetID))
            processSegmentation(part.getID(), topicSetID, segmentations, csvBuilders.get(topicSetID), page);
        });
    }

    private void processSegmentation(String partID, String topicSetID
            , List<Segment> segmentations, CSVTableBuilder tableBuilder, Page page){
        System.out.println("process Segmentation");
        segmentations.forEach(segment -> {
            int index = tableBuilder.addRow(partID);
            addSegmentToCSV(segment, tableBuilder, index, topicSetID, page);
        });
        tableBuilder.addEmptyRow(tableBuilder.columnNumber());

    }

    private void addSegmentToCSV(Segment seg, CSVTableBuilder builder, int rowIndex, String topicSetId, Page page) {
        System.out.println("add Segment To CSV");
        initializeHeaders(builder, topicSetId);

        AddAnnotations(seg, builder, rowIndex, topicSetId);

        calculateAndAddCharacteristics(seg, builder, rowIndex, page);
    }

    private void AddAnnotations(Segment seg, CSVTableBuilder builder, int rowIndex, String topicSetId) {
        TopicSet topicSet = project.getTopicSet(topicSetId);
        String mainTopicID = topicSet.getMainTopicID();
        builder.addDataToRow(rowIndex,seg.getAnnotation(mainTopicID));
        topicSet.getTopics().forEach(topic -> {
            if(topic.getTopicID() != mainTopicID){
                builder.addDataToRow(rowIndex, seg.getAnnotation(topic.getTopicID()));
            }
        });
    }

    private void calculateAndAddCharacteristics(Segment seg, CSVTableBuilder builder, int rowIndex, Page page) {
        config.characteristicList.forEach(characteristic -> {
            List<List<Dot>> allDots = PageUtil.getDotSectionsForAnnotation(seg,page.getStrokes());
            Number result = characteristic.calculate(allDots);
            builder.addDataToRow(rowIndex, result.toString());
        });
    }

    private void initializeHeaders(CSVTableBuilder builder, String topicSetID) {
        if(!builder.hasInitializedHeaders()) {
            //Get Topics
            TopicSet topicSet = project.getTopicSet(topicSetID);
            //Add the maintopic always first
            builder.addColumnHeader(topicSet.getMainTopicID());
            topicSet.getTopics().forEach(topic -> {
                if(topicSet.getMainTopicID() != topic.getTopicID()) builder.addColumnHeader(topic.getTopicID());
            });
            //add all Characteristics
            config.characteristicList.forEach(characteristic -> builder.addColumnHeader(characteristic.getName()));
        }
    }
}
