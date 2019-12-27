package New.util.Import;

import New.Interfaces.Loader;
import New.Model.Entities.Project;
import New.util.Import.JsonLoader;
import New.util.ZipHelper;
import net.lingala.zip4j.exception.ZipException;

import java.io.IOException;

public class ProjectLoader implements Loader {
    ZipHelper zipHelper;
    @Override
    public Project load(String path) throws IOException {
        zipHelper = new ZipHelper(path, true);
        try{
            Loader jsonLoader = new JsonLoader();
            String name = zipHelper.getPathTempData().toString();
            return jsonLoader.load(name);
        } catch(ZipException exp) {
            throw new IOException();
        }
    }

    public ZipHelper getZipHelper() {
        return zipHelper;
    }
}
