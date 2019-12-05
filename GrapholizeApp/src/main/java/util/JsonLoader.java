package util;
import Model.Entities.Participant;
import com.google.gson.Gson;
import Interfaces.Loader;
import util.Import.CompressedParticipant;
import util.Import.JsonLoadThread;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class JsonLoader implements Loader {
    @Override
    public HashMap<String,Participant> load(String path) throws IOException
    {
        CompressedParticipant[] partics;
        try(BufferedReader stream = new BufferedReader(new InputStreamReader(new FileInputStream(path)))){
            Gson gson = new Gson();
            StringBuilder sBuilder = new StringBuilder();
            stream.lines().forEach(s -> sBuilder.append(s));
            partics = gson.fromJson(sBuilder.toString(),CompressedParticipant[].class);
        }
        return turnToInternalDataStructur(partics);
    }

    private HashMap<String,Participant> turnToInternalDataStructur(CompressedParticipant[] partics)
    {
        ConcurrentMap<String,Participant> concMap = new ConcurrentHashMap<>();
        List<CompressedParticipant> comParticipant = new LinkedList<>();
        for (CompressedParticipant partic : partics) {
            comParticipant.add(partic);
        }
        comParticipant.parallelStream().forEach(cPar -> {
            Participant p = new Participant(cPar);
            concMap.put(p.getID(),p);
        });
        HashMap<String, Participant> result = new HashMap<>();
        concMap.forEach((k,p) -> result.put(k,p));
        return result;
    }
}
