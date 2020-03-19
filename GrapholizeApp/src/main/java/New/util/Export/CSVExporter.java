package New.util.Export;

import New.Interfaces.IExporter;
import New.Model.Entities.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.rmi.server.ExportException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CSVExporter implements IExporter {

    private Map<String,CSVTableBuilder> csvBuilders;
    private Project project;
    private ExportConfig config;

    public CSVExporter() {
        csvBuilders = new HashMap<>();
    }

    @Override
    public boolean export(String filePath, Project proj, ExportConfig config) throws IOException, ExportException {
        config.topicSetIDs.forEach(ID -> csvBuilders.put(ID, new CSVTableBuilder()));
        csvBuilders.forEach((k, csvBuilder) -> {
            csvBuilder.addColumnHeader(proj.getTopicSet(k).getTag());
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
        csvBuilders.forEach((s, csvTableBuilder) -> {
            csvTableBuilder.addEmptyRow();
            generatedCSV.append(csvTableBuilder.build());
        });

        //Create File if it doesn't exist
        if(!Files.exists(Path.of(filePath))) {
            Files.createFile(Path.of(filePath));
        }

        BufferedWriter buffWriter = Files.newBufferedWriter(Path.of(filePath), StandardOpenOption.WRITE);
        buffWriter.write(generatedCSV.toString());
        buffWriter.flush();
        buffWriter.close();
        return true;
    }

    private void processParticipant(Participant part) {
        System.out.println("process Participant");
        part.getPages().forEach(page -> processPage(part,page));
    }

    private void processPage(Participant part, Page page) {
        page.getSegmentationsMap().forEach((topicSetID, segmentations) -> {
            if(config.topicSetIDs.contains(topicSetID))
            processSegmentation(part.getID(), topicSetID, segmentations, csvBuilders.get(topicSetID), page);
        });
    }

    private void processSegmentation(String partID, String topicSetID
            , Set<Segment> segmentations, CSVTableBuilder tableBuilder, Page page){
        segmentations.forEach(segment -> {
            int index = tableBuilder.addRow(partID);
            addSegmentToCSV(segment, tableBuilder, index, topicSetID, page);
        });

    }

    private void addSegmentToCSV(Segment seg, CSVTableBuilder builder, int rowIndex, String topicSetId, Page page) {
        initializeHeadersIfNotPresent(builder, topicSetId);

        AddAnnotations(seg, builder, rowIndex, topicSetId);

        calculateAndAddCharacteristics(seg, builder, rowIndex, page);
    }

    private void AddAnnotations(Segment seg, CSVTableBuilder builder, int rowIndex, String topicSetId) {
        SuperSet superSet = project.getTopicSet(topicSetId);
        String mainTopicID = superSet.getMainTopicID();
        builder.addDataToRow(rowIndex,seg.getAnnotation(mainTopicID));
        superSet.getTopics().forEach(topic -> {
            if(topic.getTopicID() != mainTopicID){
                builder.addDataToRow(rowIndex, seg.getAnnotation(topic.getTopicID()));
            }
        });
    }

    private void calculateAndAddCharacteristics(Segment seg, CSVTableBuilder builder, int rowIndex, Page page) {
        config.characteristicList.forEach(characteristic -> {
            Number result = characteristic.calculate(seg,page.getStrokes());
            builder.addDataToRow(rowIndex, result.toString());
        });
    }

    private void initializeHeadersIfNotPresent(CSVTableBuilder builder, String topicSetID) {
        if(!builder.hasInitializedHeaders()) {
            //Get Topics
            SuperSet superSet = project.getTopicSet(topicSetID);
            //Add the maintopic always first if there is one defined otherwise leaf it empty
            if(superSet.getMainTopic() == null){
                builder.addColumnHeader(" ");
            }else {
                builder.addColumnHeader(superSet.getMainTopic().getTopicName());
            }
            //Fill all remaining topics into the columns
            superSet.getTopics().forEach(topic -> {
                if(superSet.getMainTopicID() != topic.getTopicID()) builder.addColumnHeader(topic.getTopicName());
            });
            //add all Characteristics
            config.characteristicList.forEach(characteristic -> {
                StringBuilder sBuilder = new StringBuilder(characteristic.getName());
                sBuilder.append(" ");
                sBuilder.append(characteristic.getUnitName());
                builder.addColumnHeader(sBuilder.toString());
            });
        }
    }
}
