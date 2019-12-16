package New.Execptions;

public class NoTimeLineSelectedException extends Exception {
    public String toString(){
        return "No timeline has been selected yet.";
    }
}
