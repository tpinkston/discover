package vdis.parsers;

import java.io.InputStream;

import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;

import vdis.handlers.AbstractSheetHandler;
import vdis.handlers.EntityTypesHandler;
import vdis.handlers.GenericEnumerationHandler;
import vdis.handlers.PlatformCategoryHandler;

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

        if ("Entity Types".equalsIgnoreCase(name)) {

            handler = new EntityTypesHandler();
        }
        else if ("Country LUT".equalsIgnoreCase(name)) {

            handler = new GenericEnumerationHandler("COUNTRY");
        }
        else if ("Platform Cat LUT".equalsIgnoreCase(name)) {

            handler = new PlatformCategoryHandler();
        }
        else if ("Mun Cat LUT".equalsIgnoreCase(name)) {

            handler = new GenericEnumerationHandler("MUNITION_CAT");
        }
        else if ("Radio Cat LUT".equalsIgnoreCase(name)) {

            handler = new GenericEnumerationHandler("RADIO_CAT");
        }
        else if ("EXP Cat LUT".equalsIgnoreCase(name)) {

            handler = new GenericEnumerationHandler("EXPENDABLE_CAT");
        }
        else if ("SE Cat LUT".equalsIgnoreCase(name)) {

            handler = new GenericEnumerationHandler("SENSOR_EMITTER_CAT");
        }

        if (handler != null) {

            handler.handle(styles, strings, stream);
        }
    }
}
