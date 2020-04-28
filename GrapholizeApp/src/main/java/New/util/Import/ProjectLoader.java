package New.util.Import;

import New.Enums.DataRessourceType;
import New.Interfaces.Loader;
import New.Model.Entities.Page;
import New.Model.Entities.Participant;
import New.Model.Entities.Project;
import New.util.Export.ProjectSerializer;
import New.util.ZipHelper;
import net.lingala.zip4j.exception.ZipException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ProjectLoader implements Loader {
    ZipHelper zipHelper;
    @Override
    public Project load(String path) throws IOException {
        zipHelper = new ZipHelper(path, true);
        try{
            Loader jsonLoader = new JsonLoader();
            String jsonPath = zipHelper.getPathTempData().toString();
            String timelinePath = zipHelper.getPathTempTimelines().toString();
            Project projData = jsonLoader.load(jsonPath);

            StringBuilder sBuilder = new StringBuilder();
            Files.newBufferedReader(Path.of(timelinePath)).lines().forEach(l -> sBuilder.append(l));
            Project projTimelines = (Project) new ProjectSerializer().deserialize(sBuilder.toString(), projData);

            return mergeDataAndTimelinesJson(projData, projTimelines);
        } catch(ZipException exp) {
            throw new IOException();
        }
    }

    @Override
    public DataRessourceType getRessourceType() {
        return DataRessourceType.PROJECT;
    }

    private Project mergeDataAndTimelinesJson(Project projData, Project projTimelines) {
        projData.putAllTopicSets(projTimelines.getProjectTagsMap());
        projData.getParticipantsMap().forEach((key, dataPart) -> {
            Participant timePart = projTimelines.getParticipantsMap().get(key);
            List<Page> dataPages = dataPart.getPages();
            List<Page> timePages = timePart.getPages();
            for (int i = 0; i < dataPages.size(); i++) {
                dataPages.get(i).getSegmentationsMap().putAll(timePages.get(i).getSegmentationsMap());
            }
        });
        return projData;
    }

    public ZipHelper getZipHelper() {
        return zipHelper;
    }
}
