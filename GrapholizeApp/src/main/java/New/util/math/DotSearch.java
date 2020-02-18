package New.util.math;

import New.Model.Entities.Dot;

import java.util.Collections;
import java.util.List;
@Deprecated
public class DotSearch {
    /**
     * Does a binary search over a sorted list of dots and returns the dots within the given range.
     * This method assumes that the passed list is sorted. If it is not a sorted list, the search might fail.
     * @param dots list of dots that needs to be searched.
     * @param timeStart start of time range
     * @param timeStop end of time range
     * @return a sorted list containing the dots from that time range or an empty list if no dots are found within the time frame
     */
    public static List<Dot> dotsInRange(List<Dot> dots, double timeStart, double timeStop){
        List<Dot> res;
        if(!dots.isEmpty()){
            boolean found = false;
            int i = dots.size()/2;
            while(!found){
                if(inRange(i, 0, dots.size()-1)){
                    if(dots.get(i).getTimeStamp() < timeStart){
                        i/=2;
                    }
                    else if(dots.get(i).getTimeStamp() > timeStop){
                        i *= 1.5;
                    }
                    else{
                        found = true;
                    }
                }
                else{
                    return List.of();
                }
            }
            //Found timeRange
            int iL = i;
            int iR = i;
            boolean hitLeftBounds = false;
            boolean hitRightBounds = false;
            while(!hitLeftBounds || !hitRightBounds){
                if(!hitLeftBounds && (!inRange(--iL, 0, dots.size()) || !inRange(dots.get(iL).getTimeStamp(),timeStart, timeStop))){
                    hitLeftBounds = true;
                }
                if(!hitRightBounds && (!inRange(++iR, 0, dots.size()) || !inRange(dots.get(iL).getTimeStamp(),timeStart, timeStop))){
                    hitRightBounds = true;
                }
            }
            //Add/Subtract 1 to restore last valid position.
            return dots.subList(iL+1, iR-1);
        }
        return List.of();
    }

    private static boolean inRange(double check, double start, double stop){
        return check >= start && check <= stop;
    }
}
