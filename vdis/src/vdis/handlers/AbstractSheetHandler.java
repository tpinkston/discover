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

        this.row = Integer.valueOf(row);
    }

    @Override
    public void endRow(int row) {

        if (this.row == null) {

            throw new RuntimeException("Row not started!");
        }

        if (this.row.intValue() != row) {

            throw new RuntimeException("Row mismatch!");
        }

        this.row = null;
    }

    @Override
    public void headerFooter(String text, boolean header, String tag) {

    }

    public Integer getCurrentRow() {

        return row;
    }

    public void handle(
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
}
