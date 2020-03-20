package New.Execptions;

public class NoSegmentationSelectedException extends Exception {
    public String toString(){
        return "No timeline has been selected yet.";
    }
}
