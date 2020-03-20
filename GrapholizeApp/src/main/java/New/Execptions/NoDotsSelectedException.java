package New.Execptions;

public class NoDotsSelectedException extends Exception {
    @Override
    public String toString() {
        return "No dots have been selected on the canvas.";
    }
}
