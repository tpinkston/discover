package vdis.parsers;

import java.io.InputStream;

import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;

import vdis.handlers.AbstractSheetHandler;
import vdis.handlers.EntityTypesHandler;

public class EntityTypesParser extends AbstractSpreadsheetParser {

    public EntityTypesParser() {

    }

    @Override
    public String getFileName() {

        return getFilePath() + "/Entity_Types.xlsx";
    }

    @Override
    public void parseSheet(
            String name,
            StylesTable styles,
            ReadOnlySharedStringsTable strings,
            InputStream stream) throws Exception {

        AbstractSheetHandler handler = null;

        if ("Entity Types".equals(name)) {

            handler = new EntityTypesHandler();
        }

        if (handler != null) {

            handler.handle(styles, strings, stream);
        }
    }
}
