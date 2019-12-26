package New.util.Import;

import New.Interfaces.Loader;
import New.Model.Entities.Participant;
import New.Model.Entities.Project;
import New.Model.Entities.TimeLineTag;
import New.util.Import.CompressedParticipant;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class JsonLoader implements Loader {
    @Override
    public Project load(String path) throws IOException
    {
        CompressedParticipant[] partics;
        try(BufferedReader stream = new BufferedReader(new InputStreamReader(new FileInputStream(path)))){
            Gson gson = new Gson();
            StringBuilder sBuilder = new StringBuilder();
            stream.lines().forEach(s -> sBuilder.append(s));
            partics = gson.fromJson(sBuilder.toString(), CompressedParticipant[].class);
        }
        return turnToInternalDataStructur(partics);
    }

    //TODO: Load the timeline tags
    private Project turnToInternalDataStructur(CompressedParticipant[] partics)
    {
        List<Participant> participants = new ArrayList<>();
        List<TimeLineTag> tags;
        Arrays.stream(partics).parallel().forEach(cPar -> {
            Participant p = new Participant(cPar);
            participants.add(p);
        });
        return new Project(participants, List.of());
    }
}
