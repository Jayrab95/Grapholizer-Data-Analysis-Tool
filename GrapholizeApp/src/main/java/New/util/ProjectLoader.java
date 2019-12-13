package New.util;

import New.Interfaces.Loader;
import New.Model.Entities.Participant;
import New.Model.Entities.Project;
import net.lingala.zip4j.exception.ZipException;

import java.io.IOException;
import java.util.HashMap;

public class ProjectLoader implements Loader {
    ZipHelper zipHelper;
    @Override
    public Project load(String path) throws IOException {
        zipHelper = new ZipHelper(path);
        try{
            zipHelper.init();
            Loader jsonLoader = new JsonLoader();
            String name = zipHelper.getPathTempData().toString();
            return jsonLoader.load(name);
        } catch(ZipException exp) {
            exp.printStackTrace();
            throw new IOException();
        }
    }

    public ZipHelper getZipHelper() {
        return zipHelper;
    }
}
