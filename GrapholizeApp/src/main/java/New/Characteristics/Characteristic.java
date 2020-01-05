package New.Characteristics;

public abstract class Characteristic<T,D> {
    private String name;

    public Characteristic(String name) {
        name = this.name;
    }

    @Override
    public String toString() {
        return "Characteristic: " + name;
    }

    public abstract T calculate(D data);
}
