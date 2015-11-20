package vdis;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class EnumGenerator {

    public static final String ENUMS_PACKAGE = "discover.vdis.enums";
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

            element.name += (" " + element.value.toString());
        }

        if (findOrdinal(element.value)) {

            System.out.println(
                "WARNING: Duplicate ordinal " + element.value +
                " for '" + element.name + "'...");
        }

        elements.add(element);
    }

    public void generate() throws Exception {

        String filename = (ENUMS_PATH + "/" + name + ".java");
        FileWriter file = new FileWriter(new File(filename));
        PrintWriter writer = new PrintWriter(file);

        writer.println("package " + ENUMS_PACKAGE + ";");
        writer.println();
        writer.println("import java.util.List;");
        writer.println();
        writer.println("/**");
        writer.println(" * " + name + ": This class is auto-generated by " + getClass().getName());
        writer.println(" */");
        writer.println("public final class " + name + " extends Value {");
        writer.println();
        writer.println("    public static final " + name);

        for(int i = 0; i < elements.size(); ++i) {

            Element element = elements.get(i);

            element.name = element.name.toUpperCase();
            element.name = element.name.replace(' ', '_');
            element.name = element.name.replace('-', '_');
            element.name = element.name.replace('/', '_');
            element.name = element.name.replaceAll("#", "");
            element.name = element.name.replaceAll(",", "");
            element.name = element.name.replaceAll("\\(", "");
            element.name = element.name.replaceAll("\\)", "");

            if (element.name.startsWith(name + "_")) {

                element.name = element.name.substring(name.length() + 1);
            }

            writer.print("       " + element.name + " = new " + name + "(");
            writer.print(element.value);
            writer.print(", \"" + element.name + "\"");
            writer.print(", \"" + element.description + "\"");
            writer.print(", true)");
            writer.println((i == (elements.size() - 1)) ? ";" : ",");
        }

        writer.println();
        writer.println("    private " + name + "(int value, String name, String description, boolean known) {");
        writer.println();
        writer.println("        super(value, name, description, known);");
        writer.println();
        writer.println("        cache(this, " + name + ".class);");
        writer.println("    }");
        writer.println();
        writer.println("    /** @see Value#values(Class) */");
        writer.println("    public static List<" + name + "> values() {");
        writer.println();
        writer.println("        return values(" + name + ".class);");
        writer.println("    }");
        writer.println();
        writer.println("    /** @see Value#values(Class, boolean) */");
        writer.println("    public static List<" + name + "> values(boolean known) {");
        writer.println();
        writer.println("        return values(" + name + ".class, known);");
        writer.println("    }");
        writer.println();
        writer.println("    /** @see Value#get(int, Class) */");
        writer.println("    public static " + name + " get(int value) {");
        writer.println();
        writer.println("        return get(value, " + name + ".class);");
        writer.println("    }");
        writer.println("}");
        writer.println();
        writer.close();

        System.out.println("---- File written: " + filename + ", enums: " + elements.size());

        VDIS.addEnumInterface(ENUMS_PACKAGE + "." + name);
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

        @Override
        public String toString() {

            StringBuilder builder = new StringBuilder();

            builder.append("[value = ").append(value).append("]");
            builder.append("[name = ").append(name).append("]");
            builder.append("[description = ").append(description).append("]");

            return builder.toString();
        }
    }
}
