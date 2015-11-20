package vdis;

import java.io.InputStream;
import java.util.ArrayList;
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
}
