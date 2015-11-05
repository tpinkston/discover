package vdis.handlers;

import org.apache.poi.xssf.usermodel.XSSFComment;

import vdis.EnumGenerator;
import vdis.EnumGenerator.Element;

public class CulturalFeatureCategoryHandler extends AbstractSheetHandler {

    private final EnumGenerator land = new EnumGenerator("CF_CAT_LAND");
    private final EnumGenerator surface = new EnumGenerator("CF_CAT_SURFACE");
    private final EnumGenerator subsurface = new EnumGenerator("CF_CAT_SUBSURFACE");

    private Element currentLand = null;
    private Element currentSurface = null;
    private Element currentSubsurface = null;

    @Override
    public void startRow(int row) {

        // The first row is the header row, no data.  Skip it.
        //
        if (row > 0) {

            super.startRow(row);

            currentLand = new Element();
            currentSurface = new Element();
            currentSubsurface = new Element();
        }
    }

    @Override
    public void endRow(int row) {

        super.endRow(row);

        currentLand = addElement(currentLand, land);
        currentSurface = addElement(currentSurface, surface);
        currentSubsurface = addElement(currentSubsurface, subsurface);
    }

    @Override
    public void cell(String reference, String value, XSSFComment comment) {

        if (getCurrentRow() != null) {

            switch(reference.charAt(0)) {

                // ------------------------------------------------------------
                // LAND
                case 'A':
                    currentLand.value = getInteger(value);
                    break;
                case 'B':
                    currentLand.name = value;
                    break;
                case 'C':
                    currentLand.description = value;
                    break;

                 // ------------------------------------------------------------
                // SURFACE
                case 'D':
                    currentSurface.value = getInteger(value);
                    break;
                case 'E':
                    currentSurface.name = value;
                    break;
                case 'F':
                    currentSurface.description = value;
                    break;

                // ------------------------------------------------------------
                // SUBSURFACE
                case 'G':
                    currentSubsurface.value = getInteger(value);
                    break;
                case 'H':
                    currentSubsurface.name = value;
                    break;
                case 'I':
                    currentSubsurface.description = value;
                    break;
            }
        }
    }

    @Override
    protected void parseCompleted() throws Exception {

        land.generate();
        surface.generate();
        subsurface.generate();
    }
}
