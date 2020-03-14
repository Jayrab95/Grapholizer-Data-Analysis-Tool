package New.util.Import;

import New.Interfaces.Loader;
import New.Model.Entities.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PageDataReader implements Loader {

    private final String PREFIX_PARTICIPANT_ID = "PAGEDATA";

    public Project load(String path) throws IOException{
        try(FileInputStream stream = new FileInputStream(path)) {
            if (IsFileValid(stream)) {
                PageMetaData pmd = ReadMetaData(stream);
                List<Stroke> strokes = ParseContentBody(pmd.getNumberOfStrokes(), stream);
                ReadTrailingData(stream);
                Participant newPart = new Participant(PREFIX_PARTICIPANT_ID + "_"
                        + (pmd.getCreateTimeStamp()));
                //TODO: Replace ID with actual ID
                newPart.addPage(new Page(pmd, strokes, "ID"));
                return new Project(List.of(newPart), List.of());
            }else {
                throw new IOException();
            }
        }
    }

    /**
     * Read the GUID string at the end of the page and discard it
     * @param fis fileInput from which is read
     * @throws IOException when the data is corrupt end the file ending length doesn't match what is expected
     */
    private void ReadTrailingData(FileInputStream fis) throws IOException {
        byte[] byteBuffer = new byte[4];
        int lengthOfTail = ReadInteger(fis, byteBuffer);
        try {
            for (int i = 0; i < lengthOfTail; i++) {
                fis.readNBytes(lengthOfTail);
            }
        }catch(IOException ex){
            throw new IOException("Error occured while reading trailing data, file might be corrupt");
        }
    }

    private static boolean IsFileValid(FileInputStream st) throws IOException{
        //TODO Check other hints that might prove the validity of the file
        byte[] neoSignalWord = new byte[3];
        st.read(neoSignalWord, 0, 3);
        String signalWord = new String(neoSignalWord);
        return signalWord.equals("neo");
    }

    private static PageMetaData ReadMetaData(FileInputStream st) throws IOException{
        byte[] byteBuffer = new byte[4];
        byte[] byteLongBuffer = new byte[8];
        int fileVersion = ReadInteger(st, byteBuffer);
        int noteId = ReadInteger(st, byteBuffer);
        int pageNum = ReadInteger(st, byteBuffer);
        float pageWidth = ReadFloat(st, byteBuffer);
        float pageHeight = ReadFloat(st, byteBuffer);
        long createdTimeStamp = ReadLong(st, byteLongBuffer);
        long modifiedTimeStamp = ReadLong(st, byteLongBuffer);
        int dirtyBit = st.read();
        int numOfStrokes = ReadInteger(st, byteBuffer);

        return new PageMetaData(fileVersion, pageNum, noteId
                , pageWidth, pageHeight, createdTimeStamp
                , modifiedTimeStamp, dirtyBit, numOfStrokes);

    }

    /**
     *
     * @param numOfStrokes the number of strokes to be read
     * @param st  filestream from which is read
     * @return a list of strokes
     * @throws IOException throw exception if any of the data does not match the expected layout
     */
    private static List<Stroke> ParseContentBody(int numOfStrokes, FileInputStream st) throws IOException {
        List<Stroke> strokes = new ArrayList<>();
        long[] result = ReadInitialStroke(strokes, st, numOfStrokes);
        if(strokes.get(0) != null){
            long timeStart = result[0];
            for (int i = 0; i < result[1]; i++) {
                Stroke s = ReadStroke(st, timeStart);
                //Check and add valid strokes
                if(s.getDots().size() > 0 && s.getDuration() > 0) {
                    strokes.add(s);
                }
            }
        }
        return strokes;
    }

    /**
     * adds a valid first stroke to the list or leaves it empty if there is no such stroke
     * @param strokes list where the valid first stroke should be saved to
     * @param fileStream from which the method should read
     * @param numOfStrokes the number of strokes that can be read
     * @return an long array with the [1] the start time stamp and [0] the number of strokes left to read
     * @throws IOException when the filestream encounters problems
     */
    private static long[] ReadInitialStroke(List<Stroke> strokes, FileInputStream fileStream, int numOfStrokes) throws IOException{
        int trackCount = 0;
        long[] returnValue = new long[2];
        for (int i = 0; i < numOfStrokes; i++) {
            trackCount = i;
            int signalBit = fileStream.read();
            if (signalBit == 1) throw new IOException("Voice Memo found in data, cannot parse this kind of data");
            JumpAmount(5, fileStream); //Skip over color and thickness
            byte[] integerBuffer = new byte[4];
            byte[] longBuffer = new byte[8];

            //Read Number of Dots
            int numOfDots = ReadInteger(fileStream, integerBuffer);

            //Read Timestamps
            long timeStamp = ReadLong(fileStream, longBuffer);

            //Read Dots
            Stroke stroke = null;
            if(numOfDots != 0){
                stroke = ReadDots(0, numOfDots, fileStream);
            }

            //Read Extra Data
            int extraDataNum = fileStream.read();
            if (extraDataNum == 0) JumpAmount(-1, fileStream); //Reset the read ahead
            JumpAmount(extraDataNum, fileStream);

            //Decide if this is a valid first stroke
            if(numOfDots != 0 && timeStamp != 0) {
                strokes.add(stroke);
                returnValue[0] = timeStamp;
                break;
            }
        }
        returnValue[1] = numOfStrokes - trackCount;
        return returnValue;
    }

    private static Stroke ReadStroke(FileInputStream st, long start) throws IOException{
        int signalBit = st.read();
        if (signalBit == 1) throw new IOException("Voice Memo found in data, cannot parse this kind of data");
        JumpAmount(5, st); //Skip over color and thickness
        byte[] integerBuffer = new byte[4];
        byte[] longBuffer = new byte[8];
        //Read Number of Dots
        int numOfDots = ReadInteger(st, integerBuffer);

        //Read Timestamps
        long timeStamp = ReadLong(st, longBuffer) - start;

        //Read Dots
        Stroke stroke = ReadDots(timeStamp, numOfDots, st);

        //At the end of every stroke the occurrence of extra data must be jumped over
        int extraDataNum = st.read();
        if (extraDataNum == 0) JumpAmount(-1, st); //Reset the read ahead
        JumpAmount(extraDataNum, st);

        return stroke;
    }

    /*Fills Stroke Object with dots while advancing the filePointer*/
    private static Stroke ReadDots(long timeStamp, int numberOfDots, FileInputStream st) throws IOException{
        byte[] floatBuffer = new byte[4];
        List<Dot> dots = new ArrayList<>();
        long totalTime = timeStamp;
        for (int i = 0; i < numberOfDots; i++) {
            //(x or y dot code from N2) / MAX(width, height)
            float x = ReadFloat(st, floatBuffer);
            float y = ReadFloat(st, floatBuffer);
            float force = ReadFloat(st, floatBuffer); //this might be a shitty idea

            int timeDiff = st.read();
            long dotTimeStamp = totalTime += timeDiff;
            Dot dot = new Dot(x, y, force, dotTimeStamp);
            dots.add(dot);
        }
        Stroke res = new Stroke(timeStamp, totalTime, dots);
        return res;
    }

    private static void JumpAmount(long byteCount, FileInputStream st) throws IOException{
        st.skip(byteCount);
    }

    private static int ReadInteger(FileInputStream st, byte[] buffer) throws IOException {
        st.read(buffer, 0, 4);
        return ByteArrayToUIntLSF(buffer);
    }

    private static long ReadLong(FileInputStream st, byte[] buffer) throws IOException {
        st.read(buffer, 0, 8);
        return ByteArrayToLongLSF(buffer);
    }

    private static float ReadFloat(FileInputStream st, byte[] buffer) throws IOException {
        st.read(buffer, 0, 4);
        //return ByteBuffer.wrap(buffer).getFloat();
        return Float.intBitsToFloat(ByteArrayToUIntLSF(buffer));
    }

    //Geht nach dem Least Significant Bit Reihenfolge vor
    private static int ByteArrayToUIntLSF(byte[] bytes) {
        int result = 0;
        for(byte i = 0; i < 4; i++) {
            int temp = bytes[i];
            //Because Java only stores signed bytes, a conversion is necessary.
            if(temp < 0) temp = temp & 0xFF;
            temp = temp << i*8;
            result += temp;
        }
        return result;
    }

    private static long ByteArrayToLongLSF(byte[] bytes) {
        long result = 0;
        for (byte i = 0; i < 8; i++) {
            long temp = bytes[i];
            //Because Java only stores signed bytes, a conversion is necessary.
            if(temp < 0) temp = temp & 0xFF;
            temp = temp << i * 8;
            result += temp;
        }
        return result;
    }
}
