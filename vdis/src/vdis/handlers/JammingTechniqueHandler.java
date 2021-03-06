package vdis.handlers;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFComment;

/**
 * @author Tony Pinkston
 *
 */
public class JammingTechniqueHandler extends AbstractSheetHandler {

    private static final String XML_FILE;

    static {

        File data = new File("src/discover/vdis/types/data");
        String path = null;

        // Working directory may be './discover' or './discover/vdis'
        //
        if (data.exists() && data.isDirectory()) {

            path = data.getPath();
        }
        else {

            path = ("../" + data.getPath());
        }

        XML_FILE = (path + "/jamming_technique.xml");
    }

    private final List<ObjectType> types = new ArrayList<>();

    private ObjectType current = null;

    @Override
    public void startRow(int row) {

        // The first row is the header row, no data.  Skip it.
        //
        if (row > 0) {

            super.startRow(row);

            current = new ObjectType();
        }
    }

    @Override
    public void endRow(int row) {

        super.endRow(row);

        if (current != null) {

            types.add(current);
            current = null;
        }
    }

    @Override
    public void cell(String reference, String value, XSSFComment comment) {

        if (current != null) {

            switch(reference.charAt(0)) {

                case 'A':
                    current.kind = Integer.parseInt(value);
                    break;
                case 'B':
                    current.category = Integer.parseInt(value);
                    break;
                case 'C':
                    current.subcategory = Integer.parseInt(value);
                    break;
                case 'D':
                    current.specific = Integer.parseInt(value);
                    break;
                case 'E':
                    current.type = value;
                    break;
                case 'F':
                    current.description = value;
            }
        }
    }

    private static class ObjectType {

        String type;
        String description;
        Integer kind;
        Integer category;
        Integer subcategory;
        Integer specific;
    }

    @Override
    protected void parseCompleted() throws Exception {

        FileWriter file = new FileWriter(new File(XML_FILE));
        PrintWriter writer = new PrintWriter(file);

        writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        writer.println("<types>");

        for(ObjectType type : types) {

            writer.print("  <type type=\"" + type.type + "\" value=\"");
            writer.print(type.kind + ".");
            writer.print(type.category + ".");
            writer.print(type.subcategory + ".");
            writer.print(type.specific);
            writer.print("\" description=\"");
            writer.println(type.description + "\"/>");
        }

        writer.println("</types>");
        writer.close();

        System.out.println(
            "---- File written: " + XML_FILE +
            ", types: " + types.size());
    }
}
