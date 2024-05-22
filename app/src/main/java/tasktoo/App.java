package tasktoo;

import org.json.JSONArray;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class App {

    public static Boolean isValid(Set<String> s) {
        List<String> possibleFields = List.of("name", "postalZip", "region", "country", "address", "list");
        for (int i = 0; i < s.size(); i++) {
            if (!possibleFields.contains(s.toArray()[i])) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            // Use ClassLoader to load the XML file as a resource
            ClassLoader classLoader = App.class.getClassLoader();
            InputStream xmlFile = classLoader.getResourceAsStream("data.xml");

            if (xmlFile == null) {
                throw new IllegalArgumentException("file not found! " + "data.xml");
            }
            Set<String> selectedFields = new HashSet<>();
            // Prompt user for fields to print
            while (true) {
                System.out.println(
                        "Enter the fields to display (comma separated), e.g., name,postalZip,region,country,address,list:");
                String input = scanner.nextLine();

                // Split the input into selected fields and store them in a set
                for (String field : input.split(",")) {
                    selectedFields.add(field.trim());
                }

                if (isValid(selectedFields)) {
                    break;
                }
                System.out.println("Something's wrong with the input. Try again.");
                selectedFields = new HashSet<>();
            }

            // Create a SAX parser factory
            SAXParserFactory factory = SAXParserFactory.newInstance();

            // Create a SAX parser
            SAXParser saxParser = factory.newSAXParser();

            // Create a SAX handler instance
            SAXHandler handler = new SAXHandler(selectedFields);

            // Parse the XML file using the handler
            saxParser.parse(xmlFile, handler);

            // Get the JSON array of records
            JSONArray records = handler.getRecords();

            // Print the JSON array
            System.out.println(records.toString(4));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}
