package util;
import Model.Entities.Participant;
import com.google.gson.Gson;
import Interfaces.Loader;
import util.Import.CompressedParticipant;
import util.Import.JsonLoadThread;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

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
        int cores = Runtime.getRuntime().availableProcessors();
        ConcurrentLinkedDeque<Participant> concDeque = new ConcurrentLinkedDeque<>();
        List<CompressedParticipant> comParticipant = new LinkedList<>();
        for (CompressedParticipant partic : partics) {
            comParticipant.add(partic);
        }
        comParticipant.parallelStream().forEach(cPar -> concDeque.add(new Participant(cPar)));
        return concDeque.stream().collect(Collectors.toList());
    }
}
