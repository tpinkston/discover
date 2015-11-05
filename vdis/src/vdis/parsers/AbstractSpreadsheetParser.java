package vdis.parsers;

import java.io.File;
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

        // Working directory may be './discover' or './discover/vdis'
        //
        File docs = new File("vdis/docs");

        if (docs.exists() && docs.isDirectory()) {

            return docs.getPath();
        }

        return ("../" + docs.getPath());
    }
}
