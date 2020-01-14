package New.util.Export;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CSVBuilder{
    private final String elementSeparator = ";"; //TODO maybe make this choosable by user
                                                 //TODO Windows Excel uses the semicolon instead of comma (csv (comma seperated values^^ it's right there in the name))
    private final String rowSeperator = "\n";
    private final String firstColumnTitel = "Participants";
    private List<String> columnHeaders;
    private Map<String, List<String>> rows;

    public CSVBuilder() {
        addColumnHeader(firstColumnTitel);
        rows = new HashMap<>();
    }
    public boolean addColumnHeader(String s){
        return columnHeaders.add(s);
    }

    public void addRow(String rowName) {
        rows.put(rowName, new LinkedList<>());
    }

    public void addDataToRow(String rowName, String data) {
        rows.get(rowName).add(data);
    }

    public String build() {
        StringBuilder sBuilder = new StringBuilder();
        //DO headers
        StringBuilder sBuilderBody = buildRow(sBuilder, columnHeaders);
        //Do row per row
        rows.forEach((k, list) -> {
            sBuilderBody.append(rowSeperator);
            sBuilderBody.append(k);
            sBuilderBody.append(elementSeparator);
            buildRow(sBuilderBody, list);
        });
        return sBuilderBody.toString();
    }

    private StringBuilder buildRow(StringBuilder sBuilder, List<String> elements) {
        for (int i = 0; i < columnHeaders.size(); i++) {
            if(i == columnHeaders.size() - 1){
                sBuilder.append(columnHeaders.get(i));
            }else{
                sBuilder.append(columnHeaders.get(i));
                sBuilder.append(elementSeparator);
            }
        }
        return sBuilder;
    }
}
