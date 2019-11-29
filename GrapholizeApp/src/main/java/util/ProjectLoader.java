package util;

import Interfaces.Loader;
import Model.Entities.Participant;
import net.lingala.zip4j.exception.ZipException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectLoader implements Loader {
    ZipHelper zipHelper;
    @Override
    public HashMap<String,Participant> load(String path) throws IOException {
        zipHelper = new ZipHelper(path);
        try{
            zipHelper.init();
            Loader jsonLoader = new JsonLoader();
            String name = zipHelper.getPathTempData().toString();
            return (HashMap<String, Participant>) jsonLoader.load(name);
        } catch(ZipException exp) {
            exp.printStackTrace();
            throw new IOException();
        }
    }

    public ZipHelper getZipHelper() {
        return zipHelper;
    }
}
