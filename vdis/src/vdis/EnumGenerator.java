package vdis;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class EnumGenerator {

    public static final String ENUMS_PATH;

    static {

        File enums = new File("src/discover/vdis/enums");

        // Working directory may be './discover' or './discover/vdis'
        //
        if (enums.exists() && enums.isDirectory()) {

            ENUMS_PATH = enums.getPath();

        }
        else {

            ENUMS_PATH = ("../" + enums.getPath());
        }
    }

    private final List<Element> elements = new ArrayList<>();

    private final String name;

    /**
     * @param name - Enumeration name
     */
    public EnumGenerator(String name) {

        this.name = name;
    }

    public void addElement(Element element) {

        if (findName(element.name)) {

            throw new IllegalArgumentException("Duplicate name: " + element.name);
        }

        if (findOrdinal(element.value)) {

            System.err.println(
                "WARNING: Duplicate ordinal " + element.value +
                " for '" + element.name + "'...");
        }

        elements.add(element);
    }

    public void generate() throws Exception {

        String filename = (ENUMS_PATH + "/" + name + ".java");
        FileWriter file = new FileWriter(new File(filename));
        PrintWriter writer = new PrintWriter(file);

        writer.println("package discover.vdis.enums;");
        writer.println();
        writer.println("/**");
        writer.println(" * This file is auto-generated (see " + getClass().getName() + ")");
        writer.println(" */");
        writer.println("public enum " + name + " {");
        writer.println();

        for(int i = 0; i < elements.size(); ++i) {

            Element element = elements.get(i);
            String name = element.name;

            name = name.toUpperCase();
            name = name.replace(' ', '_');

            writer.print("    " + name + "(" + element.value);
            writer.print(", \"" + element.description + "\")");
            writer.println((i == (elements.size() - 1)) ? ";" : ",");
        }

        writer.println();
        writer.println("    private final int value;");
        writer.println("    private final String description;");
        writer.println();
        writer.println("    private " + name + "(int value, String description) {");
        writer.println();
        writer.println("        this.value = value;");
        writer.println("        this.description = description;");
        writer.println("    }");
        writer.println();
        writer.println("    public int getValue() {");
        writer.println();
        writer.println("        return value;");
        writer.println("    }");
        writer.println();
        writer.println("    public String getDescription() {");
        writer.println("");
        writer.println("        return description;");
        writer.println("    }");
        writer.println("}");
        writer.println();
        writer.close();

        System.out.println("---- File written: " + filename + ", enums: " + elements.size());
    }

    private boolean findName(String name) {

        for(Element element : elements) {

            if (element.name.equalsIgnoreCase(name)) {

                return true;
            }
        }

        return false;
    }

    private boolean findOrdinal(int ordinal) {

        for(Element element : elements) {

            if (element.value == ordinal) {

                return true;
            }
        }

        return false;
    }

    public static class Element {

        public String name;
        public String description;
        public Integer value;

        public boolean isValid() {

            return ((name != null) && !name.isEmpty() && (value != null) && (value >= 0));
        }
    }
}
