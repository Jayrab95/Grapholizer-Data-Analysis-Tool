package util;
import Model.Entities.Participant;
import com.google.gson.Gson;
import Interfaces.Loader;
import util.Import.CompressedParticipant;
import util.Import.JsonLoadThread;

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
        int cores = Runtime.getRuntime().availableProcessors();
        List<Participant> resultList = new LinkedList<>();
        /*Object lock = new Object();
        if(cores > 2) {
            try {
                int half = (int) partics.length / 2;
                List<Thread> threads = new LinkedList<>();
                threads.add(new Thread(new JsonLoadThread(cPartic, resultList, lock)));
                threads.add(new Thread(new JsonLoadThread(cPartic, resultList, lock)));
                threads.forEach(t -> t.start());
                for (Thread thread : threads) {
                    thread.join();
                }
            } catch (InterruptedException exp) {
                exp.printStackTrace();
            }
        }else {
            for (CompressedParticipant cPartic : partics) {
                resultList.add(new Participant(cPartic));
            }
        }*/
        for (CompressedParticipant cPartic : partics) {
            resultList.add(new Participant(cPartic));
        }
        return resultList;
    }
}
