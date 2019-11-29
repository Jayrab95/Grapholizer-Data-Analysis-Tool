package util;

import Interfaces.Loader;
import Model.Entities.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PageDataReader implements Loader {
    private final String PREFIX_PARTICIPANT_ID = "PAGEDATA";

    public List<Participant> load(String path) throws IOException{
        List<Participant> result = new LinkedList<>();
        try(FileInputStream stream = new FileInputStream(path)){
            if(IsFileValid(stream)){
                PageMetaData pmd = ReadMetaData(stream);
                List<Stroke> strokes = ParseContentBody(pmd.getNumberOfStrokes(), stream);

                Participant newPart = new Participant(PREFIX_PARTICIPANT_ID + "_" + (Long.toString(pmd.getCreateTimeStamp())));
                newPart.addPage(new Page(pmd, strokes));
                result.add(newPart);
                return result;
            }
        }
        return null;
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

    private static List<Stroke> ParseContentBody(int numOfStrokes, FileInputStream st) throws IOException {
        List<Stroke> strokes = new ArrayList<>();
        long timeStart = 0;
        //Stroke[] strokes = new Stroke[numOfStrokes];
        for (int i = 0; i < numOfStrokes; i++) {
            if(i == 0){
                //TODO create a sensible first stroke
                int signalBit = st.read();
                if (signalBit == 1) {
                    JumpAmount(108, st); //Skip the length of a voice memo
                }
                JumpAmount(5, st); //Skip over color and thickness
                byte[] integerBuffer = new byte[4];
                byte[] longBuffer = new byte[8];
                //Read Number of Dots
                int numOfDots = ReadInteger(st, integerBuffer);
                //Read Timestamps
                long timeStamp = ReadLong(st, longBuffer);
                timeStart = timeStamp;
                timeStamp = 0;
                //Read Dots
                strokes.add(ReadDots(timeStamp, numOfDots, st, timeStart));
            }
            else{
                strokes.add(ReadStroke(st, timeStart));
            }
            //strokes[i] = ReadStroke(st);

        }
        return strokes;
    }

    private static Stroke ReadStroke(FileInputStream st, long start) throws IOException{
        //TODO create a sensible first stroke
        int signalBit = st.read();
        if (signalBit == 1) {
            JumpAmount(108, st); //Skip the length of a voice memo
        }
        JumpAmount(5, st); //Skip over color and thickness
        byte[] integerBuffer = new byte[4];
        byte[] longBuffer = new byte[8];
        //Read Number of Dots
        int numOfDots = ReadInteger(st, integerBuffer);
        //Read Timestamps
        long timeStamp = ReadLong(st, longBuffer) - start;
        //Read Dots
        return ReadDots(timeStamp, numOfDots, st, start);
    }

    /*Fills Stroke Object with dots while advancing the filePointer*/
    private static Stroke ReadDots(long timeStamp, int numberOfDots, FileInputStream st, long start) throws IOException{

        byte[] signalBuffer = new byte[4];
        byte[] floatBuffer = new byte[4];
        List<Dot> dots = new ArrayList<Dot>();
        long totalTime = timeStamp;
        for (int i = 0; i < numberOfDots; i++) {

            //TODO normalisierung der x , y Koordinaten, überhaupt nötig bei M1 Stift?
            //(x or y dot code from N2) / MAX(width, height)
            float x = ReadFloat(st, floatBuffer);
            float y = ReadFloat(st, floatBuffer);

            float force = ReadFloat(st, floatBuffer); //this might be a shitty idea

            int timeDiff = st.read();
            long dotTimeStamp = totalTime += timeDiff;
            Dot dot = new Dot(x, y, force, dotTimeStamp);
            dots.add(dot);
        }


        int extraDataNum = st.read();
        if (extraDataNum == 0) JumpAmount(-1, st); //Reset the read ahead
        JumpAmount(extraDataNum, st); // Skip the extra data if it exists

        Stroke res = new Stroke(timeStamp, totalTime, dots);
        return res;
    }

    private static void JumpAmount(long byteCount, FileInputStream st) throws IOException{
        st.skip(byteCount);
    }

    /*
    private int ReadInteger(FileInputStream st, byte[] buffer) throws IOException {
        st.read(buffer, 0, 4);
        return ByteBuffer.wrap(buffer).getInt();
    }
     */

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

    //TODO extract all methods below to a utility function
    //TODO eliminate code duplication
    //TODO: Is this even necessary? Maybe bytebuffer + readInt is enough. Needs testing.
    //Geht nach dem Least Significant byte Reihenfolge vor
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
