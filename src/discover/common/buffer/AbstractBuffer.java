/**
 * @author Tony Pinkston
 */
package discover.common.buffer;

import java.text.NumberFormat;

import discover.vdis.enums.VDIS;

public abstract class AbstractBuffer {

    private static final String THIN_TEXT_SEPARATOR =
        "----------------------------------------" +
        "----------------------------------------";
    private static final String THICK_TEXT_SEPARATOR =
        "========================================" +
        "========================================";
    
    private static final NumberFormat number;
    
    private static final String NULL = "(null)";
    
    static {
        
        number = NumberFormat.getInstance();
        number.setMaximumFractionDigits(4);
    }

    private final StringBuilder buffer = new StringBuilder();
    
    public abstract boolean isHTML();
    
    public final String toString() {
        
        return this.buffer.toString();
    }
    
    public void addBuffer(Bufferable bufferable) {
        
        bufferable.toBuffer(this);
    }

    public void listStart() {
        
        this.buffer.append(this.isHTML() ? "<ul>" : "\n");
    }
    
    public void listFinished() {
        
        this.buffer.append(this.isHTML() ? "</ul>" : "\n");
    }
    
    public void listItemStart() {
        
        this.buffer.append(this.isHTML() ? "<li>" : " - ");
    }
    
    public void listItemFinished() {
        
        this.buffer.append(this.isHTML() ? "</li>" : "\n");
    }
           
    public void addBreak() {
      
        this.buffer.append(this.isHTML() ? "<br/>" : "\n");
    }
    
    public void addText(String text) {
        
        if (text == null) {
            
            this.buffer.append(NULL);
        }
        else {
            
            this.buffer.append(text);
        }
    }
    
    public void addSeparator() {
      
        if (this.isHTML()) {
            
            buffer.append("<hr noshade/>");
        }
        else {
            
            this.addText(THIN_TEXT_SEPARATOR);
            this.addBreak();
        }
    }
    
    public void addThickSeparator(String title) {
      
        if (this.isHTML()) {
            
            buffer.append("<hr noshade/>");
        }
        else if (title != null) {
            
            String spaced = (" " + title + " ");

            int length = spaced.length();
            int half = ((80 - length) / 2);
            
            this.addText(THICK_TEXT_SEPARATOR.substring(0, half));
            this.addText(spaced);
            
            if ((length % 2) == 0) {
                
                this.addText(THICK_TEXT_SEPARATOR.substring(0, half));
            }
            else {
                
                this.addText(THICK_TEXT_SEPARATOR.substring(0, (half + 1)));
            }
        }
        else {
            
            this.addText(THICK_TEXT_SEPARATOR);
            this.addBreak();
        }
    }
    
    public void addTitle(String title) {
      
        this.addBold(title);
        this.addBreak();
    }
    
    public void addBold(String text) {
      
        this.addHTML("<b>");
        this.addText(text);
        this.addHTML("</b>");
    }
    
    public void addItalic(String text) {
      
        this.addHTML("<i>");
        this.addText(text);
        this.addHTML("</i>");
    }
    
    public void addFixedWidthItalic(String text) {
      
        this.addHTML("<i><tt>");
        this.addText(text);
        this.addHTML("</tt></i>");
    }
    
    public void addFixedWidthText(String text) {
        
        this.addHTML("<tt>");
        this.addText(text);
        this.addHTML("</tt>");
    }
    
    public void addLabel(String label) {
        
        this.addText(label);
        this.addText(": ");
    }
    
    public void addBoldLabel(String label) {
        
        this.addHTML("<b>");
        this.addLabel(label);
        this.addHTML("</b>");
    }
    
    public void addAttribute(String name, String value) {
      
        this.addLabel(name);
        this.addItalic(value);
        this.addBreak();
    }
    
    public void addBoldAttribute(String name, String value) {
      
        this.addBoldLabel(name);
        this.addItalic(value);
        this.addBreak();
    }
    
    public void addAttribute(String name, Integer value) {
      
        if (value == null) {
            
            this.addAttribute(name, NULL);
        }
        else {
            
            this.addAttribute(name, value.toString());
        }
    }
    
    public void addBoldAttribute(String name, Integer value) {
      
        if (value == null) {
            
            this.addBoldAttribute(name, NULL);
        }
        else {
            
            this.addBoldAttribute(name, value.toString());
        }
    }
    
    public void addAttribute(String name, Long value) {
      
        if (value == null) {
            
            this.addAttribute(name, NULL);
        }
        else {
            
            this.addAttribute(name, value.toString());
        }
    }
    
    public void addAttribute(String name, Float value) {
      
        if (value == null) {
            
            this.addAttribute(name, NULL);
        }
        else {
            
            this.addAttribute(name, number.format(value));
        }
    }
    
    public void addAttribute(String name, int value, int type) {
        
        String text = VDIS.getDescription(type, value);

        text.concat(" [0x");
        text.concat(Integer.toHexString(value).toUpperCase());
        text.concat("]");

        this.addAttribute(name, text);
    }
    
    public void addListAttribute(String name, String value) {

        this.listItemStart();
        this.addLabel(name);
        this.addItalic(value);
        this.listItemFinished();
    }
    
    public void addListAttribute(String name, int value, int type) {

        String text = VDIS.getDescription(type, value);

        text.concat(" [0x");
        text.concat(Integer.toHexString(value).toUpperCase());
        text.concat("]");

        this.addListAttribute(name, text);
    }
    
    private void addHTML(String string) {
        
        if (this.isHTML()) {
            
            this.buffer.append(string);
        }
    }
}
