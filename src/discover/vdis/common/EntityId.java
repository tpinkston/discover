/**
 * @author Tony Pinkston
 */
package discover.vdis.common;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import discover.common.Readable;
import discover.common.Writable;

public class EntityId implements Comparable<EntityId>, Readable, Writable {

    public static final int LENGTH = 6;

    public static final int SITE = 0;
    public static final int APPLICATION = 1;
    public static final int ENTITY = 2;

    public static final String NAMES[] = new String[] {

        "Site",
        "Application",
        "Entity"
    };

    private final int values[] = new int[3];

    public EntityId() {

        this.clear();
    }

    public EntityId(int site, int application, int entity) {

        this.set(site, application, entity);
    }

    public int getSite() { return this.values[SITE]; }
    public int getApplication() { return this.values[APPLICATION]; }
    public int getEntity() { return this.values[ENTITY]; }

    public void setSite(int value) { this.values[SITE] = value; }
    public void setApplication(int value) { this.values[APPLICATION] = value; }
    public void setEntity(int value) { this.values[ENTITY] = value; }

    public int get(int index) {

        return this.values[index];
    }

    public void set(int index, int value) {

        this.values[index] = value;
    }

    public void set(int site, int application, int entity) {

        this.setSite(site);
        this.setApplication(application);
        this.setEntity(entity);
    }

    public void set(EntityId id) {

        this.setSite(id.values[SITE]);
        this.setApplication(id.values[APPLICATION]);
        this.setEntity(id.values[ENTITY]);
    }

    @Override
    public EntityId clone() {

        return new EntityId(
            this.values[SITE],
            this.values[APPLICATION],
            this.values[ENTITY]);
    }

    @Override
    public boolean equals(Object object) {

        if (object instanceof EntityId) {

            EntityId id = (EntityId)object;

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
                 (entity.intValue() != this.getEntity())) {

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
    public int compareTo(EntityId id) {

        int comparison = 0;

        for(int i = 1; i < 3; ++i) {

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
                ", " + this.values[ENTITY] + ")");
    }

    public String toHeadlessString() {

        return (this.values[SITE] +
                "." + this.values[APPLICATION] +
                "." + this.values[ENTITY]);
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        for(int i = 0; i < 3; ++i) {

            this.values[i] = stream.readUnsignedShort();
        }
    }

    @Override
    public void write(DataOutputStream stream) throws IOException {

        for(int i = 0; i < 3; ++i) {

            stream.writeShort(this.values[i]);
        }
    }

    /**
     * Reads just 32 bits (site and application) instead of the full 48 bits
     * (site, application and entity).
     *
     * @param stream - {@link DataInputStream}
     *
     * @throws IOException
     */
    public void readPartial(DataInputStream stream) throws IOException {

        for(int i = 0; i < 2; ++i) {

            this.values[i] = stream.readUnsignedShort();
        }

        this.values[2] = 0x00;
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
