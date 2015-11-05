package vdis.handlers;

import org.apache.poi.xssf.usermodel.XSSFComment;

import vdis.EnumGenerator;
import vdis.EnumGenerator.Element;

public class PlatformCategoryHandler extends AbstractSheetHandler {

    private final EnumGenerator land = new EnumGenerator("PLAT_CAT_LAND");
    private final EnumGenerator air = new EnumGenerator("PLAT_CAT_AIR");
    private final EnumGenerator surface = new EnumGenerator("PLAT_CAT_SURFACE");
    private final EnumGenerator subsurface = new EnumGenerator("PLAT_CAT_SUBSURFACE");
    private final EnumGenerator space = new EnumGenerator("PLAT_CAT_SPACE");

    private Element currentLand = null;
    private Element currentAir = null;
    private Element currentSurface = null;
    private Element currentSubsurface = null;
    private Element currentSpace = null;

    @Override
    public void startRow(int row) {

        super.startRow(row);

        // The first row is the header row, no data.  Skip it.
        //
        if (row > 1) {

            currentLand = new Element();
            currentAir = new Element();
            currentSurface = new Element();
            currentSubsurface = new Element();
            currentSpace = new Element();
        }
    }

    @Override
    public void endRow(int row) {

        super.endRow(row);

        currentLand = addElement(currentLand, land);
        currentAir = addElement(currentAir, air);
        currentSurface = addElement(currentSurface, surface);
        currentSubsurface = addElement(currentSubsurface, subsurface);
        currentSpace = addElement(currentSpace, space);
    }

    @Override
    public void cell(String reference, String value, XSSFComment comment) {

        if (currentLand != null) {

            switch(reference.charAt(0)) {

                case 'A':
                    if (isInteger(value)) {
                        currentLand.value = Integer.parseInt(value);
                    }
                    break;
                case 'B':
                    currentLand.name = value;
                    break;
                case 'C':
                    currentLand.description = value;
                    break;
                case 'D':
                    // TODO: Land sub-category (see spreadsheet)...
                    break;
            }
        }

        if (currentAir != null) {

            switch(reference.charAt(0)) {

                case 'E':
                    currentAir.value = Integer.parseInt(value);
                    break;
                case 'F':
                    currentAir.name = value;
                    break;
                case 'G':
                    currentAir.description = value;
                    break;
            }
        }

        if (currentSurface != null) {

            switch(reference.charAt(0)) {

                case 'H':
                    currentSurface.value = Integer.parseInt(value);
                    break;
                case 'I':
                    currentSurface.name = value;
                    break;
                case 'J':
                    currentSurface.description = value;
                    break;
            }
        }

        if (currentSubsurface != null) {

            switch(reference.charAt(0)) {

                case 'K':
                    currentSubsurface.value = Integer.parseInt(value);
                    break;
                case 'L':
                    currentSubsurface.name = value;
                    break;
                case 'M':
                    currentSubsurface.description = value;
                    break;
            }
        }

        if (currentSpace != null) {

            switch(reference.charAt(0)) {

                case 'N':
                    currentSpace.value = Integer.parseInt(value);
                    break;
                case 'O':
                    currentSpace.name = value;
                    break;
                case 'P':
                    currentSpace.description = value;
                    break;
            }
        }
    }

    @Override
    protected void parseCompleted() throws Exception {

        land.generate();
        air.generate();
        surface.generate();
        subsurface.generate();
        space.generate();
    }

    private Element addElement(Element element, EnumGenerator generator) {

        if (element != null) {

            if (element.isValid()) {

                generator.addElement(element);
            }
        }

        return null;
    }
}
