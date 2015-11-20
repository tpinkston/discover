package vdis;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFReader.SheetIterator;
import org.apache.poi.xssf.model.StylesTable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import vdis.parsers.AbstractSpreadsheetParser;
import vdis.parsers.EmitterNamesParser;
import vdis.parsers.EntityTypesParser;
import vdis.parsers.ObjectTypesParser;
import vdis.parsers.OtherEnumsParser;

/**
 * @author Tony Pinkston
 */
public class VDIS {

    private static final List<String> interfaces = new ArrayList<>();

    public static void addEnumInterface(String name) {

        if (!interfaces.contains(name)) {

            interfaces.add(name);
        }
    }

    public static void main(String[] args) throws Exception {

        try {

            parse(new EmitterNamesParser());
            parse(new EntityTypesParser());
            parse(new ObjectTypesParser());
            parse(new OtherEnumsParser());

            parseOtherTypes("other_types.xml");
        }
        catch(Exception exception) {

            exception.printStackTrace();

            System.exit(1);
        }
    }

    private static void parse(AbstractSpreadsheetParser handler) throws Exception {

        String filename = handler.getFileName();

        System.out.println("------------------------------------------------------");
        System.out.println("Parsing spreadsheet: " + filename);
        System.out.println("------------------------------------------------------");

        OPCPackage opc = OPCPackage.open(filename, PackageAccess.READ);
        ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opc);
        XSSFReader reader = new XSSFReader(opc);
        StylesTable styles = reader.getStylesTable();
        SheetIterator iterator = (SheetIterator)reader.getSheetsData();

        while(iterator.hasNext()) {

            InputStream stream = iterator.next();
            String name = iterator.getSheetName();

            handler.parseSheet(name, styles, strings, stream);

            stream.close();
        }
    }

    private static void parseOtherTypes(String filename) {

        System.out.println("------------------------------------------------------");
        System.out.println("Parsing XML file: " + filename);
        System.out.println("------------------------------------------------------");

        InputStream resource = VDIS.class.getResourceAsStream(filename);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(resource));

            document.getDocumentElement().normalize();

            NodeList types = document.getElementsByTagName("type");

            for(int i = 0; i < types.getLength(); ++i) {

                Element type = (Element)types.item(i);
                NodeList elements = type.getElementsByTagName("element");
                String typeName = type.getAttribute("name");
                EnumGenerator generator = new EnumGenerator(typeName);

                System.out.println("-- " + typeName);

                for(int j = 0; j < elements.getLength(); ++j) {

                    Element element = (Element)elements.item(j);

                    String name = element.getAttribute("name");
                    String description = element.getAttribute("description");
                    String value = element.getAttribute("value");

                    generator.addElement(
                        name,
                        description,
                        Integer.parseInt(value));
                }

                generator.generate();
            }
        }
        catch(Exception exception) {

            exception.printStackTrace();

            System.exit(1);
        }
    }
}
