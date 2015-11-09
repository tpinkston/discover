package vdis.parsers;

import vdis.handlers.CulturalFeatureCategoryHandler;
import vdis.handlers.EntityTypesHandler;
import vdis.handlers.EnvironmentCategoryHandler;
import vdis.handlers.GenericEnumerationHandler;
import vdis.handlers.LifeformCategoryHandler;
import vdis.handlers.PlatformCategoryHandler;

/**
 * @author Tony Pinkston
 *
 */
public class EntityTypesParser extends AbstractSpreadsheetParser {

    public EntityTypesParser() {

        handlers.put("Entity Types", new EntityTypesHandler());
        handlers.put("Country LUT", new GenericEnumerationHandler("COUNTRY"));
        handlers.put("Platform Cat LUT", new PlatformCategoryHandler());
        handlers.put("Lifeform Cat LUT", new LifeformCategoryHandler());
        handlers.put("MUN Cat LUT", new GenericEnumerationHandler("MUN_CAT"));
        handlers.put("ENV Cat LUT", new EnvironmentCategoryHandler());
        handlers.put("CF Cat LUT", new CulturalFeatureCategoryHandler());
        handlers.put("RADIO Cat LUT", new GenericEnumerationHandler("RAD_CAT"));
        handlers.put("EXP Cat LUT", new GenericEnumerationHandler("EXP_CAT"));
        handlers.put("SE Cat LUT", new GenericEnumerationHandler("SE_CAT"));
    }

    @Override
    public String getFileName() {

        return getFilePath() + "/Entity_Types.xlsx";
    }
}
