/**
 * @author Tony Pinkston
 */
package discover.vdis.types;

import java.util.StringTokenizer;

public class Septuple {
    
    public final String string; // (e.g. "1.1.225.1.1.0.0")
    public final int kind;
    public final int domain;
    public final int country;
    public final int category;
    public final int subcategory;
    public final int specific;
    public final int extension;

    Septuple(
        String string,
        int kind,
        int domain,
        int country,
        int category,
        int subcategory,
        int specific,
        int extension) {
        
        this.string = string;
        this.kind = kind;
        this.domain = domain;
        this.country = country;
        this.category = category;
        this.subcategory = subcategory;
        this.specific = specific;
        this.extension = extension;
    }
    
    public long toLong() {
        
        return toLong(
            this.kind,
            this.domain,
            this.country,
            this.category,
            this.subcategory,
            this.specific,
            this.extension);
    }

    public static long toLong(
        int kind,
        int domain,
        int country,
        int category,
        int subcategory,
        int specific,
        int extension) {
        
        long value = 0;
        
        value = (kind & 0xFF);
        value <<= 8;
        value |= (domain & 0xFF);
        value <<= 16;
        value |= (country & 0xFFFF);
        value <<= 8;
        value |= (category & 0xFF);
        value <<= 8;
        value |= (subcategory & 0xFF);
        value <<= 8;
        value |= (specific & 0xFF);
        value <<= 8;
        value |= (extension & 0xFF);

        return value;
    }

    public static String toString(
        int kind,
        int domain,
        int country,
        int category,
        int subcategory,
        int specific,
        int extension) {
        
        StringBuilder builder = new StringBuilder();
        
        builder.append(kind);
        builder.append(".");
        builder.append(domain);
        builder.append(".");
        builder.append(country);
        builder.append(".");
        builder.append(category);
        builder.append(".");
        builder.append(subcategory);
        builder.append(".");
        builder.append(specific);
        builder.append(".");
        builder.append(extension);

        return builder.toString();
    }
    
    public static Septuple parse(String string) {
        
        StringTokenizer tokenizer = new StringTokenizer(string, ".");
        int kind = 0;
        int domain = 0;
        int country = 0;
        int category = 0;
        int subcategory = 0;
        int specific = 0;
        int extension = 0;

        if (tokenizer.countTokens() == 7) {
            
            try {
                
                kind = Integer.parseInt(tokenizer.nextToken());
                domain = Integer.parseInt(tokenizer.nextToken());
                country = Integer.parseInt(tokenizer.nextToken());
                category = Integer.parseInt(tokenizer.nextToken());
                subcategory = Integer.parseInt(tokenizer.nextToken());
                specific = Integer.parseInt(tokenizer.nextToken());
                extension = Integer.parseInt(tokenizer.nextToken());
            }
            catch(NumberFormatException exception) {
                
                exception.printStackTrace();
            }
        }
        
        return new Septuple(
            string, 
            kind, 
            domain, 
            country, 
            category, 
            subcategory, 
            specific, 
            extension);
    }
}
