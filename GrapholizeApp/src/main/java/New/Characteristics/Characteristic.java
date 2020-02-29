package New.Characteristics;

import New.Model.Entities.Dot;
import New.Model.Entities.Segment;
import New.Model.Entities.Stroke;
import New.util.PageUtil;

import java.util.List;

public abstract class Characteristic<T extends Number> {
    private String name;
    private String unitName;

    public Characteristic(String name, String unitName) {
        this.name = name;
        this.unitName = unitName;
    }

    public T calculate(Segment seg, List<Stroke> strokes) {
        return calculateImplementation(PageUtil.getDotSectionsForAnnotation(seg,strokes));
    }

    protected abstract T calculateImplementation(List<List<Dot>> data);


    public String getName(){
        return name;
    }

    public String getUnitName() {
        return unitName;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Characteristic){
            if(name.equals(((Characteristic) obj).getName())) return true;
            else return false;
        }
        return false ;
    }
}
