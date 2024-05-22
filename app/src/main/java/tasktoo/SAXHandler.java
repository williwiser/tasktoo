package tasktoo;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.List;
import java.util.Set;

public class SAXHandler extends DefaultHandler {
    private Set<String> selectedFields;
    private StringBuilder elementValue = new StringBuilder();
    private JSONObject currentRecord;
    private JSONArray records;

    public SAXHandler(Set<String> selectedFields) {
        this.selectedFields = selectedFields;
        this.records = new JSONArray();
    }

    public JSONArray getRecords() {
        return records;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        elementValue.setLength(0); // Clear the element value buffer
        if ("record".equals(qName)) {
            currentRecord = new JSONObject();
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        elementValue.append(ch, start, length); // Collect the characters
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (currentRecord != null && selectedFields.contains(qName)) {
            currentRecord.put(qName, elementValue.toString().trim());
        }
        if ("record".equals(qName)) {
            records.put(currentRecord);
        }
    }
}
