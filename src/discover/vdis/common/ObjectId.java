package discover.vdis.common;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;

import discover.common.Readable;

/**
 * @author Tony Pinkston
 */
public class ObjectId implements Comparable<ObjectId>, Readable {

    private static final int SITE = 0;
    private static final int APPLICATION = 1;
    private static final int OBJECT = 2;

    private final int values[] = new int[3];

    public int getSite() { return values[SITE]; }
    public int getApplication() { return values[APPLICATION]; }
    public int getObject() { return values[OBJECT]; }

    @Override
    public boolean equals(Object object) {

        if (object instanceof ObjectId) {

            ObjectId id = (ObjectId)object;

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
                 (entity.intValue() != getObject())) {

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
    public int compareTo(ObjectId id) {

        int comparison = 0;

        for(int i = 1; i < 8; ++i) {

            comparison = Integer.compare(values[i], id.values[i]);

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
                ", " + values[OBJECT] + ")");
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        for(int i = 0; i < 3; ++i) {

            values[i] = stream.readUnsignedShort();
        }
    }
}
