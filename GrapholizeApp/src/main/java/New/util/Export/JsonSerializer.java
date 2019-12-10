package New.util.Export;

import Interfaces.Serializer;
import com.google.gson.Gson;

public class JsonSerializer implements Serializer {
    @Override
    public String serialize(Object o) {
        return new Gson().toJson(o);
    }
}
