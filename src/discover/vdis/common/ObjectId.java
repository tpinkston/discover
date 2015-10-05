/**
 * @author Tony Pinkston
 */
package discover.vdis.common;

import java.io.DataInputStream;
import java.io.IOException;

import discover.common.Readable;

public class ObjectId implements Comparable<ObjectId>, Readable {

    private static final int SITE = 0;
    private static final int APPLICATION = 1;
    private static final int OBJECT = 2;

    private final int values[] = new int[3];
    
    public int getSite() { return this.values[SITE]; }
    public int getApplication() { return this.values[APPLICATION]; }
    public int getObject() { return this.values[OBJECT]; }

    @Override
    public boolean equals(Object object) {
        
        if (object instanceof ObjectId) {
        
            ObjectId id = (ObjectId)object;
            
            for(int i = 0; i < 3; ++i) {
                
                if (this.values[i] != id.values[i]) {
                    
                    return false;
                }
            }

            return true;
        }
        
        return false;
    }
    
    public boolean matches(Integer site, Integer application, Integer entity) {
        
        if ((site != null) && 
            (site.intValue() != this.getSite())) {
            
            return false;
        }
        else if ((application != null) && 
                 (application.intValue() != this.getApplication())) {
            
            return false;
        }
        else if ((entity != null) && 
                 (entity.intValue() != this.getObject())) {
            
            return false;
        }
        else {
            
            return true;
        }
    }

    public void clear() {
        
        for(int i = 0; i < 3; ++i) {
            
            this.values[i] = 0;
        }
    }

    @Override
    public int compareTo(ObjectId id) {

        int comparison = 0;

        for(int i = 1; i < 8; ++i) {
            
            comparison = this.compare(this.values[i], id.values[i]);
            
            if (comparison != 0) {
                
                return comparison;
            }
        }

        return comparison;
    }

    @Override
    public String toString() {
        
        return ("(" + this.values[SITE] + 
                ", " + this.values[APPLICATION] + 
                ", " + this.values[OBJECT] + ")");
    }
    
    @Override
    public void read(DataInputStream stream) throws IOException {

        for(int i = 0; i < 3; ++i) {
            
            this.values[i] = stream.readUnsignedShort();
        }
    }
    
    private int compare(int first, int second) {

        if (first < second) {
            
            return -1;
        }
        else if (first > second) {
            
            return 1;
        }
        else {
            
            return 0;
        }
    }
}
