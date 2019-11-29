package Interfaces;
import Model.Entities.Participant;

import java.io.IOException;
import java.util.HashMap;

@FunctionalInterface
public interface Loader
{
    HashMap<String,Participant> load(String path) throws IOException;
}

