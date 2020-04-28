package New.util.Import.model;

import New.Model.Entities.Dot;
import New.Model.Entities.Stroke;
import New.util.Import.model.CompressedDot;

import java.util.LinkedList;
import java.util.List;

public class CompressedStroke {
    public long TimeStart;
    public long TimeEnd;
    public List<CompressedDot> CompressedDots;

    public CompressedStroke(Stroke stroke) {
        TimeStart = stroke.getTimeStart();
        TimeEnd = stroke.getTimeEnd();
        CompressedDots = new LinkedList<>();
        long lastTimeStamp = stroke.getTimeStart();
        for(Dot temp : stroke.getDots()) {
            byte timeDiff = (byte) (temp.getTimeStamp() - lastTimeStamp);
            lastTimeStamp = temp.getTimeStamp();
            CompressedDots.add(new CompressedDot(temp, timeDiff));
        }
    }
}
