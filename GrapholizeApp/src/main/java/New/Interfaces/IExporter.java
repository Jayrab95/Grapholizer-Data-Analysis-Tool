package New.Interfaces;

import New.Model.Entities.Project;
import New.util.Export.ExportConfig;

import java.io.IOException;
import java.rmi.server.ExportException;

public interface IExporter {
    /**
     *
     * @param filePath path to the desired output file
     * @param proj the project with all the data to be exported
     * @param config the selection subset of data that should be exported
     * @return boolean, true if write to file was successful
     * @throws IOException when IO interaction failed
     * @throws ExportException when something was wrong with the supplied data
     */
    boolean export(String filePath, Project proj, ExportConfig config) throws IOException, ExportException;
}
