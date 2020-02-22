package New.Characteristics;

import New.Model.Entities.Dot;

import java.util.List;

public abstract class Characteristic<T extends Number> {
    private String name;
    private String unitName;

    public Characteristic(String name, String unitName) {
        this.name = name;
        this.unitName = unitName;
    }

    public abstract T calculate(List<List<Dot>> data);

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
