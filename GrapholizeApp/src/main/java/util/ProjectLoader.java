package util;

import Interfaces.Loader;
import Model.Entities.Participant;
import net.lingala.zip4j.exception.ZipException;

import java.io.IOException;
import java.util.List;

public class ProjectLoader implements Loader {
    @Override
    public List<Participant> load(String path) throws IOException {
        ZipHelper zipHelper = new ZipHelper(path);
        try{
            zipHelper.init();
            Loader jsonLoader = new JsonLoader();
            String name = zipHelper.getPathData().toString();
            return jsonLoader.load(name);
        } catch(ZipException exp) {
            exp.printStackTrace();
            throw new IOException();
        } finally {
            zipHelper.cleanUp();
        }
    }
}
