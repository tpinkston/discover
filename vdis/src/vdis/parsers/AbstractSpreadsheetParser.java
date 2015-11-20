package vdis.parsers;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;

import vdis.handlers.AbstractSheetHandler;

/**
 * @author Tony Pinkston
 *
 */
public abstract class AbstractSpreadsheetParser {

    protected final Map<String, AbstractSheetHandler> handlers = new HashMap<>();

    public abstract String getFileName();

    public void parseSheet(
            String name,
            StylesTable styles,
            ReadOnlySharedStringsTable strings,
            InputStream stream) throws Exception {

        AbstractSheetHandler handler = handlers.get(name);

        System.out.print("-- " + name.toUpperCase() + ": ");

        if (handler == null) {

            System.out.println("no handler...");
        }
        else {

            System.out.println(handler.getClass().getName());

            handler.handle(styles, strings, stream);
        }
    }

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
