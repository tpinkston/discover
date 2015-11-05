package vdis.handlers;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFComment;

/**
 * Handler for sheet "Entity Types" in Entity_Types.xlsx
 *
 * @author Tony Pinkston
 *
 */
public class EntityTypesHandler extends AbstractSheetHandler {

    private static final Map<Integer, String> files = new HashMap<>();

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

        files.put(1, path + "/platforms.xml");
        files.put(2, path + "/munitions.xml");
        files.put(3, path + "/lifeforms.xml");
        files.put(4, path + "/environmentals.xml");
        files.put(5, path + "/cultural_features.xml");
        files.put(6, path + "/supplies.xml");
        files.put(7, path + "/radios.xml");
        files.put(8, path + "/expendables.xml");
        files.put(9, path + "/sensor_emitters.xml");
    }

    private final List<EntityType> types = new ArrayList<>();

    private EntityType current = null;

    @Override
    public void startRow(int row) {

        // The first row is the header row, no data.  Skip it.
        //
        if (row > 0) {

            super.startRow(row);

            current = new EntityType();
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
                    current.name = value;
                    break;
                case 'B':
                    current.kind = Integer.parseInt(value);
                    break;
                case 'C':
                    current.domain = Integer.parseInt(value);
                    break;
                case 'D':
                    current.country = Integer.parseInt(value);
                    break;
                case 'E':
                    current.category = Integer.parseInt(value);
                    break;
                case 'F':
                    current.subcategory = Integer.parseInt(value);
                    break;
                case 'G':
                    current.specific = Integer.parseInt(value);
                    break;
                case 'H':
                    current.extension = Integer.parseInt(value);
                    break;
                case 'I':
                    current.description = value;
                    break;
            }
        }
    }

    private static class EntityType {

        String name;
        String description;
        Integer kind;
        Integer domain;
        Integer country;
        Integer category;
        Integer subcategory;
        Integer specific;
        Integer extension;
    }

    @Override
    protected void parseCompleted() throws Exception {

        Map<Integer, PrintWriter> writers = new HashMap<>();

        for(Integer kind : files.keySet()) {

            FileWriter file = new FileWriter(new File(files.get(kind)));
            PrintWriter writer = new PrintWriter(file);

            writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            writer.println("<types>");

            writers.put(kind, writer);
        }

        for(EntityType type : types) {

            PrintWriter writer = writers.get(type.kind);

            writer.print("  <type name=\"" + type.name + "\" value=\"");
            writer.print(type.kind + ".");
            writer.print(type.domain + ".");
            writer.print(type.country + ".");
            writer.print(type.category + ".");
            writer.print(type.subcategory + ".");
            writer.print(type.specific + ".");
            writer.print(type.extension);
            writer.print("\" description=\"");
            writer.println(type.description + "\"/>");
        }

        for(Integer kind : writers.keySet()) {

            PrintWriter writer = writers.get(kind);

            writer.println("</types>");
            writer.close();

            System.out.println("---- File written: " + files.get(kind));
        }
    }
}
