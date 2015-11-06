package vdis.handlers;

import java.io.InputStream;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.util.SAXHelper;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import vdis.EnumGenerator;
import vdis.EnumGenerator.Element;

public abstract class AbstractSheetHandler implements SheetContentsHandler {

    private Integer row = null;

    public AbstractSheetHandler() {

    }

    @Override
    public abstract void cell(String reference, String value, XSSFComment comment);

    @Override
    public void startRow(int row) {

        if (this.row != null) {

            throw new RuntimeException("Row not ended!");
        }

        // Even though sheets start at row 1, POI starts them at 0...
        //
        this.row = Integer.valueOf(row + 1);
    }

    @Override
    public void endRow(int row) {

        if ((this.row != null) && (this.row.intValue() != (row + 1))) {

            throw new RuntimeException("Row mismatch: " + row);
        }

        this.row = null;
    }

    @Override
    public void headerFooter(String text, boolean header, String tag) {

    }

    public Integer getCurrentRow() {

        return row;
    }

    public final void handle(
            StylesTable styles,
            ReadOnlySharedStringsTable strings,
            InputStream stream) throws Exception {

        InputSource source = new InputSource(stream);
        XMLReader parser = SAXHelper.newXMLReader();

        ContentHandler handler = new XSSFSheetXMLHandler(
              styles,
              null,
              strings,
              this,
              new DataFormatter(),
              false);

        parser.setContentHandler(handler);
        parser.parse(source);

        parseCompleted();
    }

    protected void parseCompleted() throws Exception {

    }

    protected Element addElement(Element element, EnumGenerator generator) {

        if (element != null) {

            if (element.isValid()) {

                if (element.name.equalsIgnoreCase("reserved")) {

                    element.name += (" " + element.value.toString());
                }

                generator.addElement(element);
            }

            element = null;
        }

        return element;
    }

    protected Integer getInteger(String string) {

        try {

            return Integer.parseInt(string);
        }
        catch(NumberFormatException exception) {

            return null;
        }
    }

    protected boolean isInteger(String string) {

        if ((string != null) && !string.isEmpty()) {

            for(int i = 0; i < string.length(); ++i) {

                if (!Character.isDigit(string.charAt(i))) {

                    return false;
                }
            }

            return true;
        }

        return false;
    }
}
