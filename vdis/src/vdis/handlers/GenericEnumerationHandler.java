package vdis.handlers;

import org.apache.poi.xssf.usermodel.XSSFComment;

import vdis.EnumGenerator;
import vdis.EnumGenerator.Element;

public class GenericEnumerationHandler extends AbstractSheetHandler {

    private final EnumGenerator generator;

    private Element current = null;

    public GenericEnumerationHandler(String name) {

        generator = new EnumGenerator(name);
    }

    @Override
    public void startRow(int row) {

        super.startRow(row);

        // The first row is the header row, no data.  Skip it.
        //
        if (row > 1) {

            current = new Element();
        }
    }

    @Override
    public void endRow(int row) {

        super.endRow(row);

        if (current != null) {

            if (current.isValid()) {

                generator.addElement(current);
            }

            current = null;
        }
    }

    @Override
    public void cell(String reference, String value, XSSFComment comment) {

        if (current != null) {

            switch(reference.charAt(0)) {

                case 'A':
                    current.value = Integer.parseInt(value);
                    break;
                case 'B':
                    current.name = value;
                    break;
                case 'C':
                    current.description = value;
                    break;
            }
        }
    }

    @Override
    protected void parseCompleted() throws Exception {

        generator.generate();
    }
}
