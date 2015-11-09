package vdis;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFReader.SheetIterator;
import org.apache.poi.xssf.model.StylesTable;

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

            writeIndex();
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

    private static void writeIndex() throws Exception {

        String filename = (EnumGenerator.ENUMS_PATH + "/index.txt");
        FileWriter file = new FileWriter(new File(filename));
        PrintWriter writer = new PrintWriter(file);

        Collections.sort(interfaces);

        for(int i = 0; i < interfaces.size(); ++i) {

            writer.println(interfaces.get(i));
        }

        writer.close();

        System.out.println("---- File written: " + filename + ", interfaces: " + interfaces.size());
    }
}
