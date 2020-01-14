package New.util.Export;

import New.Interfaces.IExporter;
import New.Model.Entities.Project;

public class CSVExporter implements IExporter {
    CSVBuilder csvBuilder = new CSVBuilder();
    @Override
    public boolean export(String filePath, Project proj, Object data) {
        return false;
    }
}
