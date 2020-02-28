package New.util;

import New.Model.Entities.Segment;
import New.Model.Entities.Dot;
import New.Model.Entities.Stroke;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The PageUtil class provides static methods for searching dot sections (list of consecutive dots within a stroke that overlap with
 * a segment's start and end time)
 * This functionality is mainly used for the Detail timelines and Characteristic calculation.
 * Definition: Dot Section: Overlap between strokes and segments
 */
public class PageUtil {

    /**
     * Retrieves all dot sections that are contained in between the given segments
     * @param segments List of segments for which the dotsections should be retrieved
     * @param strokes All strokes that need to be examined (generally all strokes on a page)
     * @return list of dot sections
     */
    public static List<List<Dot>> getDotSectionsForAnnotations(List<Segment> segments, List<Stroke> strokes){

        List<List<Dot>> res = new LinkedList<>();
        for(Segment a : segments) {
            for (List<Dot> dotSection : getDotSectionsForAnnotation(a, strokes)) {
                res.add(dotSection);
            }
        }
        return res;
    }

    /**
     * Retrieves all dot sections that overlap with the given segment.
     * @param segment The segment that determines the timeframe of interest
     * @param strokes List of strokes that need to be examined for overlapping dots.
     * @return A list of dot sections
     */
    public static List<List<Dot>> getDotSectionsForAnnotation(Segment segment, List<Stroke> strokes){

        return overlappingStrokesForAnnotationStream(segment, strokes)
                .map(s -> s.getDotsWithinTimeRange(segment.getTimeStart(), segment.getTimeStop()))
                .filter(dots -> dots.size() > 0)
                .collect(Collectors.toList());
    }

    /**
     * Returns a list of strokes whose start and/or end time overlap with the given segment's time frame.
     * @param segment The given segment that determines the time frame of interest.
     * @param strokes The list of strokes that needs to be filtered.
     * @return a list of strokes which overlap with the given segment.
     */
    public static List<Stroke> overlappingStrokesForAnnotation(Segment segment, List<Stroke> strokes){
        return overlappingStrokesForAnnotationStream(segment, strokes)
                .collect(Collectors.toList());
    }

    /**
     * Takes a segment and a list of strokes and returns a stream that has filtered the stroke list so that it
     * only contains overlapping segments
     * @param a the given segment that determines the time frame of interest
     * @param strokes The list of strokes that needs to be filtered.
     * @return a stream that has filtered the stroke list for overlapping strokes.
     */
    public static Stream<Stroke> overlappingStrokesForAnnotationStream(Segment a, List<Stroke> strokes){
        return strokes.stream()
                .filter(stroke -> stroke.getTimeEnd() >= a.getTimeStart() && stroke.getTimeStart() <= a.getTimeStop());
    }


}
