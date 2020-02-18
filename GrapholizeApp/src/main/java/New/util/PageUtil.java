package New.util;

import New.Model.Entities.Segment;
import New.Model.Entities.Dot;
import New.Model.Entities.Stroke;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The PageUtil class provides static methods for searching dot sections (list of dot lists (strokes) that lie within
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

    public static List<List<Dot>> getDotSectionsForAnnotation(Segment a, List<Stroke> strokes){

        return overlappingStrokesForAnnotationStream(a, strokes)
                .map(s -> s.getDotsWithinTimeRange(a.getTimeStart(), a.getTimeStop()))
                .filter(dots -> dots.size() > 0)
                .collect(Collectors.toList());
    }

    public static List<Stroke> overlappingStrokesForAnnotation(Segment a, List<Stroke> strokes){
        return overlappingStrokesForAnnotationStream(a, strokes)
                .collect(Collectors.toList());
    }

    public static Stream<Stroke> overlappingStrokesForAnnotationStream(Segment a, List<Stroke> strokes){
        return strokes.stream()
                .filter(stroke -> stroke.getTimeEnd() >= a.getTimeStart() && stroke.getTimeStart() <= a.getTimeStop());
    }


    public static List<Stroke> overlappingStrokesForAnnotations(List<Segment> segments, List<Stroke> strokes){
        return segments.stream()
                .flatMap(annotation -> overlappingStrokesForAnnotation(annotation, strokes).stream())
                .collect(Collectors.toList());
    }

}
