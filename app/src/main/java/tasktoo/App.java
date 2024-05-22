/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package tasktoo;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.swing.plaf.synth.Region;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

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
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            // Use ClassLoader to load the XML file as a resource
            ClassLoader classLoader = App.class.getClassLoader();
            InputStream xmlFile = classLoader.getResourceAsStream("data.xml");

            if (xmlFile == null) {
                throw new IllegalArgumentException("file not found! " + "data.xml");
            }

            // Create a DocumentBuilderFactory
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

            // Create a DocumentBuilder
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            // Parse the XML file and build the Document
            Document doc = dBuilder.parse(xmlFile);

            // Normalize the XML structure
            doc.getDocumentElement().normalize();
            JSONArray jsonArray = new JSONArray();

            System.out.println("Root element: " + doc.getDocumentElement().getNodeName());
            Set<String> selectedFields = new HashSet<>();
            // Get all record nodes
            NodeList nodeList = doc.getElementsByTagName("record");
            while (true) {
                // Prompt user for fields to print
                System.out.println("Enter the fields to display (comma separated), e.g., name,region,country:");
                String input = reader.readLine();
                System.out.print("");

                // Split the input into selected fields and store them in a set
                for (String field : input.split(",")) {
                    selectedFields.add(field.trim());
                }

                if (isValid(selectedFields)) {
                    break;
                }

                // Loops if invalid input
                System.out.println("Something's wrong with your input. Try again.");
                System.out.println("");
                selectedFields = new HashSet<>();
            }

            // Loop through all record nodes
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                JSONObject jsonObject = new JSONObject();
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    // Check and print selected fields
                    if (selectedFields.contains("name")) {
                        String name = element.getElementsByTagName("name").item(0).getTextContent();
                        jsonObject.put("name", name);
                    }
                    if (selectedFields.contains("postalZip")) {
                        String postalZip = element.getElementsByTagName("postalZip").item(0).getTextContent();
                        jsonObject.put("postalZip", postalZip);
                    }
                    if (selectedFields.contains("region")) {
                        String region = element.getElementsByTagName("region").item(0).getTextContent();
                        jsonObject.put("region", region);
                    }
                    if (selectedFields.contains("country")) {
                        String country = element.getElementsByTagName("country").item(0).getTextContent();
                        jsonObject.put("country", country);
                    }
                    if (selectedFields.contains("address")) {
                        String address = element.getElementsByTagName("address").item(0).getTextContent();
                        jsonObject.put("address", address);
                    }
                    if (selectedFields.contains("list")) {
                        String list = element.getElementsByTagName("list").item(0).getTextContent();
                        jsonObject.put("list", list);
                    }
                }
                jsonArray.put(jsonObject);
            }

            System.out.println(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
