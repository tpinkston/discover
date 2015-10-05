/**
 * @author Tony Pinkston
 */
package discover.vdis.enums;

import java.io.PrintStream;

import discover.common.Hexadecimal;

public class EnumPrinter {
    
    public static void printUsage() {
    
        PrintStream out = System.out;
        
        out.println();
        out.println("For the enumerations possible parameters are as follows:");
        out.println();
        out.println("  --enum=?      Lists all enumerations and their numbers");
        out.println("  --enum=all    Same as '--enum=?'");
        out.println("  --enum=list   Same as '--enum=?'");
        out.println("  --enum=X      Print values for enumeration number X");
        out.println("  --enum=NAME   Print values for enumeration named NAME");
    }

    public static void print(String parameter) {

        int length = VDIS.ENUM_TYPE_NAMES.length;
        boolean found = false;
        
        if (parameter.equalsIgnoreCase("?") || 
            parameter.equalsIgnoreCase("all") || 
            parameter.equalsIgnoreCase("list")) {
            
            listEnums();
            found = true;
        }
        
        try {
            
            int type = Integer.parseInt(parameter);
            
            printEnum(type);
            found = true;
        }
        catch(NumberFormatException exception) {
            
            // Do nothing...
        }
        
        for(int i = 0; (i < length) && !found; ++i) {
            
            if (parameter.equalsIgnoreCase(VDIS.ENUM_TYPE_NAMES[i])) {
                
                printEnum(i);
                found = true;
            }
        }
    }
    
    private static void listEnums() {
        
        PrintStream out = System.out;
        int length = VDIS.ENUM_TYPE_NAMES.length;

        for(int i = 0; (i < length); ++i) {
            
            out.println(i + ": " + VDIS.ENUM_TYPE_NAMES[i]);
        }
    }
    
    private static void printEnum(int type) {
                
        PrintStream out = System.out;
        String names[] = VDIS.getEnumNames(type);
        String descriptions[] = VDIS.getEnumDescriptions(type);
        int values[] = VDIS.getEnumValues(type);

        if ((values != null) && (names != null) && (descriptions != null)) {
            
            out.println("Printing enumeration: " + VDIS.ENUM_TYPE_NAMES[type]);
            
            for(int i = 0; i < values.length; ++i) {
                
                out.print(values[i]);
                out.print(" (0x");
                out.print(Hexadecimal.toString32(values[i]));
                out.print("): ");
                out.print(names[i]);
                out.print(" ('" + descriptions[i] + "')\n");
            }
        }
    }
}
