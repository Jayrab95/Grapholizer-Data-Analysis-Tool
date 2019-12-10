package New.Interfaces;
import New.Model.Entities.Project;

import java.io.IOException;
import java.util.HashMap;

@FunctionalInterface
public interface Loader
{
    Project load(String path) throws IOException;
}

