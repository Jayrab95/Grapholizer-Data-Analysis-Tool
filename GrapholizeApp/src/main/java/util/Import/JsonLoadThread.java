package util.Import;

import Model.Entities.Participant;

import java.util.List;

public class JsonLoadThread implements Runnable{

    private Object lock;
    List<Participant> result;
    private List<CompressedParticipant> cParticipants;
    public JsonLoadThread(List<CompressedParticipant> cParticipants, List<Participant> result, Object lock) {
        this.cParticipants = cParticipants;
        this.lock = lock;
        this.result = result;
    }
    @Override
    public void run() {
        for (CompressedParticipant cParticipant : cParticipants) {
            Participant p = new Participant(cParticipant);
            synchronized (lock) {
                result.add(p);
            }
        }
    }
}
