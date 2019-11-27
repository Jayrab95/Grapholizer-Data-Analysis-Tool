package util;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class ZipHelper {

    private final String DATA_FILE_NAME = "data.json";
    private final String TIMELINE_FILE_NAME = "timelines.json";
    private ZipFile zipFile;
    private ZipParameters parameters;
    private Path tempDirPath;
    private Path pathData;
    private Path pathTimelines;

    public ZipHelper(String filePath){
        parameters = new ZipParameters();
        zipFile = new ZipFile(filePath);
    }

    public void init() throws IOException {
        tempDirPath = Files.createTempDirectory("grapholizer");
        String absPathTempDir = tempDirPath.toAbsolutePath().toString();

        zipFile.extractFile(DATA_FILE_NAME, absPathTempDir);
        zipFile.extractFile(TIMELINE_FILE_NAME, absPathTempDir);

        pathData = Path.of(absPathTempDir, File.separator, DATA_FILE_NAME);
        pathTimelines = Path.of(absPathTempDir, File.separator, TIMELINE_FILE_NAME);
    }

    public void cleanUp() throws IOException {
        Files.delete(pathData);
        Files.delete(pathTimelines);
        Files.delete(tempDirPath);
    }

/*    public void replaceFile(File file) throws ZipException {
        remove(file.getName());
        add(file);
    }

    public void remove(String fileName) throws ZipException{
        zipFile.removeFile(fileName);
    }

    public void add(File file) throws ZipException{
        zipFile.addFile(file , parameters);
    }*/

    public Path getPathData() {
        return pathData;
    }

    public void setPathData(Path pathData) {
        this.pathData = pathData;
    }

    public Path getPathTimelines() {
        return pathTimelines;
    }

    public void setPathTimelines(Path pathTimelines) {
        this.pathTimelines = pathTimelines;
    }

}
