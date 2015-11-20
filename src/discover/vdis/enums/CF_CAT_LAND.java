package discover.vdis.enums;

import java.util.List;

/**
 * CF_CAT_LAND: This class is auto-generated by vdis.EnumGenerator
 */
public final class CF_CAT_LAND extends Value {

    public static final CF_CAT_LAND
       OTH = new CF_CAT_LAND(0, "OTH", "Other", true),
       BRD = new CF_CAT_LAND(1, "BRD", "Bridge", true),
       BLDG = new CF_CAT_LAND(2, "BLDG", "Building", true),
       TRAK = new CF_CAT_LAND(3, "TRAK", "Tracks", true),
       CRAT = new CF_CAT_LAND(4, "CRAT", "Crater", true),
       OBS = new CF_CAT_LAND(5, "OBS", "Obstacle", true),
       FLAG = new CF_CAT_LAND(6, "FLAG", "Flags/Markers", true),
       PREP = new CF_CAT_LAND(7, "PREP", "Prepared Positions", true),
       UGRD = new CF_CAT_LAND(8, "UGRD", "Underground Facility", true),
       SSM = new CF_CAT_LAND(9, "SSM", "SSM Site", true),
       RNWY = new CF_CAT_LAND(10, "RNWY", "Runway", true),
       SAM = new CF_CAT_LAND(11, "SAM", "SAM Site", true),
       NUKE = new CF_CAT_LAND(12, "NUKE", "Nuclear Power Plant", true),
       RAIL = new CF_CAT_LAND(13, "RAIL", "Railyard", true),
       CTF = new CF_CAT_LAND(14, "CTF", "Camp Lejeune MOUT Collective Training Facility (CTF)", true),
       PST = new CF_CAT_LAND(15, "PST", "POL Storage Tank", true),
       CARGO = new CF_CAT_LAND(16, "CARGO", "Cargo Container", true),
       TOWER = new CF_CAT_LAND(17, "TOWER", "Tower", true),
       AAA = new CF_CAT_LAND(18, "AAA", "AAA Site", true),
       BRDSP = new CF_CAT_LAND(19, "BRDSP", "Bridge Span", true),
       ROAD = new CF_CAT_LAND(20, "ROAD", "Road Element", true);

    private CF_CAT_LAND(int value, String name, String description, boolean known) {

        super(value, name, description, known);

        cache(this, CF_CAT_LAND.class);
    }

    /** @see Value#values(Class) */
    public static List<CF_CAT_LAND> values() {

        return values(CF_CAT_LAND.class);
    }

    /** @see Value#values(Class, boolean) */
    public static List<CF_CAT_LAND> values(boolean known) {

        return values(CF_CAT_LAND.class, known);
    }

    /** @see Value#get(int, Class) */
    public static CF_CAT_LAND get(int value) {

        return get(value, CF_CAT_LAND.class);
    }
}

