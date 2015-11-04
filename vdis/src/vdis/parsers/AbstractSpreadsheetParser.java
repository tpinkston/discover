package vdis.parsers;

import java.io.InputStream;

import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;

public abstract class AbstractSpreadsheetParser {

    public abstract String getFileName();

    public abstract void parseSheet(
            String name,
            StylesTable styles,
            ReadOnlySharedStringsTable strings,
            InputStream stream) throws Exception;


    public String getFilePath() {

        return "vdis/docs";
    }
}
