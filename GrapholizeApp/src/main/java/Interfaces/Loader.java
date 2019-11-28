package Interfaces;
import Model.Entities.Participant;

import java.io.IOException;
import java.util.List;

@FunctionalInterface
public interface Loader
{
    List<Participant> load(String path) throws IOException;
}

