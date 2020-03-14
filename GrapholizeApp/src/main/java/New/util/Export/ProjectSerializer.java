package New.util.Export;

import New.Interfaces.Serializer;
import New.Model.Entities.Project;
import com.google.gson.GsonBuilder;
import java.lang.reflect.Modifier;

public class ProjectSerializer implements Serializer<Project> {
    /*
    * Returns a Json String of the given class
    * */
    @Override
    public String serialize(Project project) {
        String s =  new GsonBuilder()
                .excludeFieldsWithModifiers(Modifier.TRANSIENT)
                .setPrettyPrinting()
                .create()
                .toJson(project);
        System.out.println(s);
        return s;
    }

    @Override
    public Object deserialize(String s, Project p) {
        return new GsonBuilder()
                .excludeFieldsWithModifiers(Modifier.TRANSIENT)
                .create()
                .fromJson(s, p.getClass());
    }
}
