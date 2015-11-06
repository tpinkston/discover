package vdis.parsers;

import vdis.handlers.ObjectTypesHandler;

/**
 * @author Tony Pinkston
 *
 */
public class ObjectTypesParser extends AbstractSpreadsheetParser {

    public ObjectTypesParser() {

        handlers.put("OBJECT_STATE", new ObjectTypesHandler());
    }

    @Override
    public String getFileName() {

        return getFilePath() + "/Object_Types.xlsx";
    }
}
