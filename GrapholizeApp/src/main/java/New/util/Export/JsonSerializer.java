package New.util.Export;

import New.Interfaces.Serializer;
import com.google.gson.Gson;

public class JsonSerializer implements Serializer {
    @Override
    public String serialize(Object o) { return new Gson().toJson(o); }

    @Override
    public Object deserialize(String s, Object ob) { return new Gson().fromJson(s, ob.getClass()); }

}
