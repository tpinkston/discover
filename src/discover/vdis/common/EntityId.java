package discover.vdis.common;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

import discover.common.Readable;
import discover.common.Writable;

/**
 * @author Tony Pinkston
 */
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

        clear();
    }

    public EntityId(int site, int application, int entity) {

        set(site, application, entity);
    }

    public int getSite() { return values[SITE]; }
    public int getApplication() { return values[APPLICATION]; }
    public int getEntity() { return values[ENTITY]; }

    public void setSite(int value) { values[SITE] = value; }
    public void setApplication(int value) { values[APPLICATION] = value; }
    public void setEntity(int value) { values[ENTITY] = value; }

    public int get(int index) {

        return values[index];
    }

    public void set(int index, int value) {

        values[index] = value;
    }

    public void set(int site, int application, int entity) {

        setSite(site);
        setApplication(application);
        setEntity(entity);
    }

    public void set(EntityId id) {

        setSite(id.values[SITE]);
        setApplication(id.values[APPLICATION]);
        setEntity(id.values[ENTITY]);
    }

    @Override
    public EntityId clone() {

        return new EntityId(
            values[SITE],
            values[APPLICATION],
            values[ENTITY]);
    }

    @Override
    public boolean equals(Object object) {

        if (object instanceof EntityId) {

            EntityId id = (EntityId)object;

            for(int i = 0; i < 3; ++i) {

                if (values[i] != id.values[i]) {

                    return false;
                }
            }

            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {

        return Arrays.hashCode(values);
    }

    public boolean matches(Integer site, Integer application, Integer entity) {

        if ((site != null) &&
            (site.intValue() != getSite())) {

            return false;
        }
        else if ((application != null) &&
                 (application.intValue() != getApplication())) {

            return false;
        }
        else if ((entity != null) &&
                 (entity.intValue() != getEntity())) {

            return false;
        }
        else {

            return true;
        }
    }

    public void clear() {

        for(int i = 0; i < 3; ++i) {

            values[i] = 0;
        }
    }

    @Override
    public int compareTo(EntityId id) {

        int comparison = 0;

        for(int i = 1; i < 3; ++i) {

            comparison = compare(values[i], id.values[i]);

            if (comparison != 0) {

                return comparison;
            }
        }

        return comparison;
    }

    @Override
    public String toString() {

        return ("(" + values[SITE] +
                ", " + values[APPLICATION] +
                ", " + values[ENTITY] + ")");
    }

    public String toHeadlessString() {

        return (values[SITE] +
                "." + values[APPLICATION] +
                "." + values[ENTITY]);
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        for(int i = 0; i < 3; ++i) {

            values[i] = stream.readUnsignedShort();
        }
    }

    @Override
    public void write(DataOutputStream stream) throws IOException {

        for(int i = 0; i < 3; ++i) {

            stream.writeShort(values[i]);
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

            values[i] = stream.readUnsignedShort();
        }

        values[2] = 0x00;
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
