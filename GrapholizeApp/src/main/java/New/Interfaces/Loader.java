package New.Interfaces;
import New.Enums.DataRessourceType;
import New.Model.Entities.Project;

import java.io.IOException;
import java.util.HashMap;

public interface Loader
{
    /**
     *
     * @param path to the file to be loaded
     * @return returns the project loaded from file with all the data
     * @throws IOException If something with the file io goes wrong
     */
    Project load(String path) throws IOException;

    DataRessourceType getRessourceType();
}

