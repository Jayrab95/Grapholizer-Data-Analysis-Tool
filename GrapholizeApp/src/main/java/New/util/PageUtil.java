package New.util;

import New.Model.Entities.Segment;
import New.Model.Entities.Dot;
import New.Model.Entities.Stroke;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PageUtil {

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
        List<List<Dot>> res = new LinkedList<>();

        /*
        //overlappingStrokes contains all strokes that overlap with the bounds of this annotation.
        List<Stroke> overlappingStrokes = strokes.stream()
                .filter(stroke -> stroke.getTimeEnd() >= a.getTimeStart() && stroke.getTimeStart() <= a.getTimeStop())
                .collect(Collectors.toList());
        for(Stroke s : overlappingStrokes){
            res.add(s.getDotsWithinTimeRange(a.getTimeStart(), a.getTimeStop()));
        }
        return res;

         */

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
