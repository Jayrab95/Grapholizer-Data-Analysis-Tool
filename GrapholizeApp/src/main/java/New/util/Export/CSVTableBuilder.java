package New.util.Export;

import java.util.LinkedList;
import java.util.List;

public class CSVTableBuilder {
    private final String ELEMENT_SEPERATOR = ";"; //TODO maybe make this choosable by user
    private final String ROW_SEPERATOR = "\n"; //TODO Windows Excel uses the semicolon instead of comma (csv (comma seperated values^^ it's right there in the name))
    private List<String> columnHeaders;
    private List<List<String>> rows;

    public CSVTableBuilder() {
        rows = new LinkedList<>();
        columnHeaders = new LinkedList<>();
    }

    public boolean addColumnHeader(String s){ return columnHeaders.add(s); }

    public int addRow(String rowName) {
        List<String> list = new LinkedList<>();
        list.add(rowName);
        rows.add(list);
        return list.indexOf(rowName);
    }

    public CSVTableBuilder addDataToRow(int rowIndex, String data) {
        rows.get(rowIndex).add(data);
        return this;
    }

    public void addEmptyRow(int numberOfEntries) {
        List<String> emptyRow = new LinkedList<>();
        for (int i = 0; i < numberOfEntries; i++) {
            emptyRow.add("");
        }
        rows.add(emptyRow);
    }

    public String build() {
        StringBuilder sBuilder = new StringBuilder();
        StringBuilder sBuilderBody = buildRow(sBuilder, columnHeaders);
        rows.forEach( row -> {
            sBuilderBody.append(ROW_SEPERATOR);
            buildRow(sBuilderBody, row);
        });
        return sBuilderBody.toString();
    }

    private StringBuilder buildRow(StringBuilder sBuilder, List<String> elements) {
        for (int i = 0; i < elements.size(); i++) {
            if(i == elements.size() - 1){
                sBuilder.append(elements.get(i));
            }else{
                sBuilder.append(elements.get(i));
                sBuilder.append(ELEMENT_SEPERATOR);
            }
        }
        return sBuilder;
    }

    public boolean hasInitializedHeaders(){
        return columnHeaders.size() > 1;
    }
}
