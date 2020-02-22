package New.Dialogues.DialogResults;

import java.util.HashMap;
import java.util.Map;

public class FilterDialogResult {
    /**
     * The key is the topic id, the value is the filter text
     */
    private final Map<String, String> topicFilterMap;

    public FilterDialogResult(){
        this.topicFilterMap = new HashMap<>();
    }

    public void putTopicFilter(String topicID, String filterText){
        topicFilterMap.put(topicID, filterText);
    }

    public String getTopicFilter(String topicID){
        return topicFilterMap.get(topicID);
    }

    public void removeTopicFilter(String topicID){
        topicFilterMap.remove(topicID);
    }
}
