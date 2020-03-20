package New.Execptions;

public class SegmentationNameEmptyException extends SegmentationNameException {
    public SegmentationNameEmptyException(String tag) {
        super(tag);
    }
    public String toString(){
        return "The tag cannot be empty.";
    }
}
