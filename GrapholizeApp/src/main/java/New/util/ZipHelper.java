package New.util;
import Model.TimeLinesModel;
import New.util.Export.JsonSerializer;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


public class ZipHelper {

    private final String RAW_DATA_FILE_NAME = "data.json";
    private final String TIMELINE_FILE_NAME = "timelines.json";
    private ZipFile zipFile;
    private ZipParameters parameters;
    private Path tempDirPath;
    private Path pathTempData;
    private Path pathTempTimelines;
    private boolean isInitialized;

    public ZipHelper(String filePath){
        parameters = new ZipParameters();
        zipFile = new ZipFile(filePath);
        isInitialized = false;
    }

    public void init() throws IOException {
        tempDirPath = Files.createTempDirectory("grapholizer");
        String absPathTempDir = tempDirPath.toAbsolutePath().toString();

        zipFile.extractFile(RAW_DATA_FILE_NAME, absPathTempDir);
        zipFile.extractFile(TIMELINE_FILE_NAME, absPathTempDir);

        pathTempData = Path.of(absPathTempDir, File.separator, RAW_DATA_FILE_NAME);
        pathTempTimelines = Path.of(absPathTempDir, File.separator, TIMELINE_FILE_NAME);
        isInitialized = true;
    }

    public void saveTimelines(List<TimeLinesModel> timeLines) throws IOException{
        String serData = new JsonSerializer().serialize(timeLines);
        //TODO output to file
    }

    /*
    Cleans up the temporary files created by init(). Should always be called after using ZipHelper
     */
    public void cleanUp() throws IOException {
        Files.delete(pathTempData);
        Files.delete(pathTempTimelines);
        Files.delete(tempDirPath);
        isInitialized = false;
        replaceData();
        replaceTimelines();
    }

    public void replaceTimelines() throws ZipException {
        remove(TIMELINE_FILE_NAME);
        add(new File(String.valueOf(pathTempTimelines)));
    }

    public void replaceData() throws ZipException {
        remove(RAW_DATA_FILE_NAME);
        add(new File(String.valueOf(pathTempData)));
    }

    public void remove(String fileName) throws ZipException{
        zipFile.removeFile(fileName);
    }

    public void add(File file) throws ZipException{
        zipFile.addFile(file , parameters);
    }

    public Path getPathTempData() {
        return pathTempData;
    }

    public void setPathTempData(Path pathTempData) {
        this.pathTempData = pathTempData;
    }

    public Path getPathTempTimelines() {
        return pathTempTimelines;
    }

    public void setPathTempTimelines(Path pathTempTimelines) {
        this.pathTempTimelines = pathTempTimelines;
    }

}
