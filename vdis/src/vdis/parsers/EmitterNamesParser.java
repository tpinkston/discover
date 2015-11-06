package vdis.parsers;

import vdis.handlers.EmitterNamesHandler;

/**
 * @author Tony Pinkston
 *
 */
public class EmitterNamesParser extends AbstractSpreadsheetParser {

    public EmitterNamesParser() {

        handlers.put("EMITTER_NAME", new EmitterNamesHandler());
    }

    @Override
    public String getFileName() {

        return getFilePath() + "/Emitter_Name.xlsx";
    }
}
