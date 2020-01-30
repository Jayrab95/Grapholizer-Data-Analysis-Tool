package New.Execptions;

public class ExporterException extends Exception {
    public String toString(){
        return "While preparing for export an error happened.";
    }
}
