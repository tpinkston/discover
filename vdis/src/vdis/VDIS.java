package vdis;

import java.io.InputStream;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFReader.SheetIterator;
import org.apache.poi.xssf.model.StylesTable;

import vdis.parsers.AbstractSpreadsheetParser;
import vdis.parsers.EntityTypesParser;

/**
 * @author Tony Pinkston
 */
public class VDIS {

    public static void main(String[] args) throws Exception {

        try {

            parse(new EntityTypesParser());
        }
        catch(Exception exception) {

            exception.printStackTrace();

            System.exit(1);
        }
    }

    private static void parse(AbstractSpreadsheetParser handler) throws Exception {

        String filename = handler.getFileName();

        System.out.println("Parsing spreadsheet: " + filename);

        OPCPackage opc = OPCPackage.open(filename, PackageAccess.READ);
        ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opc);
        XSSFReader reader = new XSSFReader(opc);
        StylesTable styles = reader.getStylesTable();
        SheetIterator iterator = (SheetIterator)reader.getSheetsData();

        while(iterator.hasNext()) {

            InputStream stream = iterator.next();
            String name = iterator.getSheetName();

            System.out.println("-- " + name);

            handler.parseSheet(name, styles, strings, stream);

            stream.close();
        }
    }
}
