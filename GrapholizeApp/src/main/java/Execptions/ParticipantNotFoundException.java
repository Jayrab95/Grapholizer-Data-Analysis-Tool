package Execptions;

public class ParticipantNotFoundException extends Exception {
    String participantID;
    public ParticipantNotFoundException(String participantID){
        this.participantID = participantID;
    }
    public String toString(){
        return "The participant with the id " + participantID + " was not found.";
    }
}
