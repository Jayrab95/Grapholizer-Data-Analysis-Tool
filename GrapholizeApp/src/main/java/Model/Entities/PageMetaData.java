package Model.Entities;

public class PageMetaData {

    private final int fileVersion;
    private final int pageNumber;
    private final int noteID;
    private final int dirtyBit;
    private final int numberOfStrokes;
    private final float pageWidth;
    private final float pageHeight;
    private final long modifiedTimeStamp;
    private final long createTimeStamp;



    public PageMetaData(int fileVersion, int pageNumber, int noteID
            , float pageWidth, float pageHeight, long createTimeStamp
            , long modifiedTimeStamp, int dirtyBit, int numberOfStrokes) {
        this.fileVersion = fileVersion;
        this.pageNumber = pageNumber;
        this.noteID = noteID;
        this.pageWidth = pageWidth;
        this.pageHeight = pageHeight;
        this.createTimeStamp = createTimeStamp;
        this.modifiedTimeStamp = modifiedTimeStamp;
        this.dirtyBit = dirtyBit;
        this.numberOfStrokes = numberOfStrokes;
    }

    public int getFileVersion() {
        return fileVersion;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getNoteID() {
        return noteID;
    }

    public int getDirtyBit() {
        return dirtyBit;
    }

    public int getNumberOfStrokes() {
        return numberOfStrokes;
    }

    public float getPageWidth() {
        return pageWidth;
    }

    public float getPageHeight() {
        return pageHeight;
    }

    public long getModifiedTimeStamp() {
        return modifiedTimeStamp;
    }

    public long getCreateTimeStamp() {
        return createTimeStamp;
    }
}
