package New.util;

import New.CustomControls.Annotation.AnnotationRectangle;
import New.Model.Entities.Annotation;
import New.Model.Entities.Dot;
import New.Model.Entities.Stroke;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class PageUtil {

    public static List<List<Dot>> getDotSectionsForAnnotations(List<Annotation> annotations, List<Stroke> strokes){
        List<List<Dot>> res = new LinkedList<>();

        for(Annotation a : annotations) {
            for (List<Dot> dotSection : getDotSectionsForAnnotation(a, strokes)) {
                res.add(dotSection);
            }
        }
        return res;
    }

    public static List<List<Dot>> getDotSectionsForAnnotation(Annotation a, List<Stroke> strokes){
        List<List<Dot>> res = new LinkedList<>();

        //reqStrokes contains all strokes that overlap with the bounds of this annotation.
        List<Stroke> reqStrokes = strokes.stream()
                .filter(stroke -> stroke.getTimeEnd() >= a.getTimeStart() && stroke.getTimeStart() <= a.getTimeStop())
                .collect(Collectors.toList());
        for(Stroke s : reqStrokes){
            res.add(s.getDotsWithinTimeRange(a.getTimeStart(), a.getTimeStop()));
        }
        return res;
    }
}
