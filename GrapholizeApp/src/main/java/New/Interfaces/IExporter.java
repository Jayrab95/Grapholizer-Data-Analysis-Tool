package New.Interfaces;

import New.Model.Entities.Project;
import New.util.Export.ExportConfig;

import java.io.IOException;
import java.rmi.server.ExportException;

public interface IExporter {
    boolean export(String filePath, Project proj, ExportConfig config) throws IOException, ExportException;
}
