package New.Interfaces;

public interface Serializer<T> {
    public String serialize(T t);

    public Object deserialize(String s, T t);
}
