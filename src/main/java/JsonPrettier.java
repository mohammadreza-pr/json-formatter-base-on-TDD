import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Iterator;


public class JsonPrettier {
    private String inLineJson;
    private StringBuilder prettifiedJson;

    private int currentDepth = 0;

    public JsonPrettier(String inLineJson) {
        this.inLineJson = inLineJson;
        this.prettifiedJson = new StringBuilder();
    }

    public String prettify() {
        if (isEmptyJson())
            return "";
        JsonNode jsonKeyValues = getKeyValuesFromJson();
        prettifyASimpleJson(jsonKeyValues);
        return this.prettifiedJson.toString();
    }

    private boolean isEmptyJson() {
        return inLineJson.isEmpty();
    }

    public JsonNode getKeyValuesFromJson() {
        JsonNode keyValues = null;
        try {
            keyValues = new ObjectMapper().readTree(this.inLineJson);
        } catch (IOException e) {
            throw new RuntimeException();
        }
        return keyValues;
    }

    private void prettifyASimpleJson(JsonNode jsonKeyValues) {
        getIntoNewBlock();
        addABreakForNotEmptyJson(jsonKeyValues);
        addKeyValuesOfASimpleJson(jsonKeyValues);
        removeLastExtraDashForNotEmptyJson(jsonKeyValues);
        getOutOfBlock();
    }

    private void getIntoNewBlock() {
        this.prettifiedJson.append("{");
        this.currentDepth += 1;
    }

    private void addABreakForNotEmptyJson(JsonNode jsonKeyValues) {
        if (isAnyKeyInJson(jsonKeyValues))
            this.prettifiedJson.append("\n");
    }

    private static boolean isAnyKeyInJson(JsonNode jsonKeyValues) {
        return jsonKeyValues.size() != 0;
    }

    private void addKeyValuesOfASimpleJson(JsonNode jsonKeyValues) {
        Iterator<String> keys = jsonKeyValues.fieldNames();
        while (keys.hasNext()) {
            Object key = keys.next();
            Object value = jsonKeyValues.get(String.valueOf(key));
            addASingleKeyValueRow(key, value);
        }
    }

    public void addASingleKeyValueRow(Object key, Object value) {
        addKeyToRow(key);
        addValueToRow(value);
        this.prettifiedJson.append(",\n");
    }

    private void addKeyToRow(Object key) {
        addEnoughTab();
        this.prettifiedJson.append("\"");
        this.prettifiedJson.append(key);
        this.prettifiedJson.append("\": ");
    }

    private void addValueToRow(Object value) {
        if (value.getClass().getSimpleName().equals("ObjectNode")) {
            this.prettifyASimpleJson((JsonNode) value);
        } else if (isValueString(value)) {
            addAStringValue(value);
        } else {
            this.prettifiedJson.append(value);
        }
    }

    private void addAStringValue(Object value) {
        this.prettifiedJson.append("\"");
        this.prettifiedJson.append(value);
        this.prettifiedJson.append("\"");
    }

    private void removeLastExtraDashForNotEmptyJson(JsonNode jsonKeyValues) {
        if (isAnyKeyInJson(jsonKeyValues))
            this.prettifiedJson.delete(prettifiedJson.length() - 2, prettifiedJson.length() - 1);
    }

    private void getOutOfBlock() {
        this.currentDepth -= 1;
        addEnoughTab();
        this.prettifiedJson.append("}");
    }


    private void addEnoughTab() {
        this.prettifiedJson.append("\t".repeat(Math.max(0, this.currentDepth)));
    }

    private static boolean isValueString(Object value) {
        return value.getClass().getSimpleName().equals("String");
    }
}
