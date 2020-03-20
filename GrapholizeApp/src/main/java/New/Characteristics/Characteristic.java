package New.Characteristics;

import New.Model.Entities.Dot;
import New.Model.Entities.Segment;
import New.Model.Entities.Stroke;
import New.util.PageUtil;

import java.util.List;

/**
 * The Characteristics class is an abstract base class for any characteristics that need to be calculated
 * The Characteristic class is generic, so that it can return a variety of different numbered results.
 * @param <T>
 */
public abstract class Characteristic<T extends Number> {
    private String name;
    private String unitName;

    /**
     * Create a new Characteristic object.
     * @param name Name of the characteristic. This string is mostly used for display purposes.
     * @param unitName name of the unit of the calculated characteristic. (Example: mm/s etc)
     */
    public Characteristic(String name, String unitName) {
        this.name = name;
        this.unitName = unitName;
    }

    /**
     * Takes a segment and a list of strokes and calculates the characteristic value for
     * the given time frame of the segment.
     * @param segment segment which denotes the timeframe for which the characteristic needs to be calculated
     * @param strokes a list of strokes which is checked for dots that intersect with the given segment.
     * @return the calculated characteristic value for the given segment over the given list of strokes.
     */
    public T calculate(Segment segment, List<Stroke> strokes) {
        return calculateImplementation(PageUtil.getDotSectionsForAnnotation(segment,strokes));
    }

    /**
     * Calculates the characteristic using the given dot sections. This method needs to be overwritten
     * in each extending class.
     * @param data a list of dot sections which are used for the calculation of the characteristic
     * @return the calculated characteristic value.
     */
    protected abstract T calculateImplementation(List<List<Dot>> data);


    /**
     * Returns the name of this characteristic
     * @return name of this characteristic
     */
    public String getName(){
        return name;
    }

    /**
     * Returns the unit description of this characteristic
     * @return unit description of this characteristic
     */
    public String getUnitName() {
        return unitName;
    }

    /**
     * Returns the name of this characteristic
     * @return name of this characterictic
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Compares to a given object and returns true if both are characteristics and both have the same name.
     * @param obj object to compare
     * @return true if obj is a Characteristic and has the same name as this Characteristic, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Characteristic){
            if(name.equals(((Characteristic) obj).getName())) return true;
            else return false;
        }
        return false ;
    }
}
