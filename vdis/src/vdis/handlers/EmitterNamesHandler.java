package vdis.handlers;

import org.apache.poi.xssf.usermodel.XSSFComment;

import vdis.EnumGenerator;
import vdis.EnumGenerator.Element;

/**
 * @author Tony Pinkston
 *
 */
public class EmitterNamesHandler extends AbstractSheetHandler {

    private final EnumGenerator names = new EnumGenerator("EMITTER_NAME");

    private Element current = null;

    @Override
    public void startRow(int row) {

        // The first row is the header row, no data.  Skip it.
        //
        if (row > 0) {

            super.startRow(row);

            current = new Element();
        }
    }

    @Override
    public void endRow(int row) {

        super.endRow(row);

        current = addElement(current, names);
    }

    @Override
    public void cell(String reference, String value, XSSFComment comment) {

        if (getCurrentRow() != null) {

            switch(reference.charAt(0)) {

                case 'A':
                    current.value = getInteger(value);
                    break;
                case 'B':
                    current.name = value;
                    break;
                case 'C':
                    addDescription(current, value);
                    break;
                case 'D':
                    addDescription(current, value);
                    break;
                case 'E':
                    addDescription(current, value);
                    break;
            }
        }
    }

    @Override
    protected void parseCompleted() throws Exception {

        names.generate();
    }

    private void addDescription(Element element, String description) {

        if (element.description == null) {

            element.description = description;
        }
        else {

            element.description += (" " + description);
        }
    }
}
