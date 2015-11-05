package vdis.parsers;

import java.io.InputStream;

import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;

import vdis.handlers.AbstractSheetHandler;
import vdis.handlers.CulturalFeatureCategoryHandler;
import vdis.handlers.EntityTypesHandler;
import vdis.handlers.EnvironmentCategoryHandler;
import vdis.handlers.GenericEnumerationHandler;
import vdis.handlers.LifeformCategoryHandler;
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
        else if ("Lifeform Cat LUT".equalsIgnoreCase(name)) {

            handler = new LifeformCategoryHandler();
        }
        else if ("Mun Cat LUT".equalsIgnoreCase(name)) {

            handler = new GenericEnumerationHandler("MUN_CAT");
        }
        else if ("ENV Cat LUT".equalsIgnoreCase(name)) {

            handler = new EnvironmentCategoryHandler();
        }
        else if ("CF Cat LUT".equalsIgnoreCase(name)) {

            handler = new CulturalFeatureCategoryHandler();
        }
        else if ("Radio Cat LUT".equalsIgnoreCase(name)) {

            handler = new GenericEnumerationHandler("RAD_CAT");
        }
        else if ("EXP Cat LUT".equalsIgnoreCase(name)) {

            handler = new GenericEnumerationHandler("EXP_CAT");
        }
        else if ("SE Cat LUT".equalsIgnoreCase(name)) {

            handler = new GenericEnumerationHandler("SE_CAT");
        }

        if (handler != null) {

            handler.handle(styles, strings, stream);
        }
    }
}
