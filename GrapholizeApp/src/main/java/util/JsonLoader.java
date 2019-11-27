package util;
import Model.Entities.Participant;
import com.google.gson.Gson;
import Interfaces.Loader;
import util.Import.CompressedParticipant;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class JsonLoader implements Loader {
    @Override
    public List<Participant> load(String path) throws IOException {
        CompressedParticipant[] partics;
        try(BufferedReader stream = new BufferedReader(new InputStreamReader(new FileInputStream(path)))){
            Gson gson = new Gson();
            String content = stream.lines().reduce("", String::concat);
            partics = gson.fromJson(content,CompressedParticipant[].class);
        }
        return turnToInternalDataStructur(partics);
    }

    private List<Participant> turnToInternalDataStructur(CompressedParticipant[] partics) {
        List<Participant> resultList = new LinkedList<>();
        for (CompressedParticipant cPartic : partics) {
            resultList.add(new Participant(cPartic));
        }
        return resultList;
    }
}
