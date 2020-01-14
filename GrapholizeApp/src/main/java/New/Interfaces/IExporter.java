package New.Interfaces;

import New.Model.Entities.Project;

public interface IExporter {
    boolean export(String filePath, Project proj, Object data);
}
