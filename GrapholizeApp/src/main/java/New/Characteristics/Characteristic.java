package New.Characteristics;

public abstract class Characteristic<T,D> {
    private String name;

    public Characteristic(String name) {
        this.name = name;
    }

    public abstract T calculate(D data);

    public String getName(){
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Characteristic){
            if(name.equals(((Characteristic) obj).getName()))return true;
            else return false;
        }
        return false ;
    }
}
