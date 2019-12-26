package New.Interfaces;

public interface Serializer<T> {
    public String serialize(T t);

    public T deserialize(String s, T t);
}
