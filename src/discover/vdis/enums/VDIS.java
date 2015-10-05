package discover.vdis.enums;

import java.util.HashMap;

/**
 * Auto-generated class containing VDIS enumeration data.
 */
public class VDIS {

    public static class Handle {

        public final String name;
        public final int[] values;
        public final String[] names;
        public final String[] descriptions;

        public Handle(
            String name,
            int values[],
            String names[],
            String descriptions[]) {

            this.name = name;
            this.values = values;
            this.names = names;
            this.descriptions = descriptions;
        }

        public String getName(int value) {

            for(int i = 0, size = values.length; i < size; ++i) {

                if (value == values[i]) {

                    return names[i];
                }
            }

            return getUnknown(name, value);
        }

        public String getDescription(int value) {

            for(int i = 0, size = values.length; i < size; ++i) {

                if (value == values[i]) {

                    return descriptions[i];
                }
            }

            return getUnknown(name, value);
        }
    }

    private static final HashMap<Integer, Handle> handles;
    private static final HashMap<String, HashMap<Integer, String>> unknowns;

    static {

        handles = new HashMap<Integer, Handle>();
        unknowns = new HashMap<String, HashMap<Integer, String>>();
    }

    public static final int
        PDU_TYPE_OTHER = 0,
        PDU_TYPE_ENTITY_STATE = 1,
        PDU_TYPE_FIRE = 2,
        PDU_TYPE_DETONATION = 3,
        PDU_TYPE_COLLISION = 4,
        PDU_TYPE_SERVICE_REQ = 5,
        PDU_TYPE_RESUPPLY_OFFER = 6,
        PDU_TYPE_RESUPPLY_RECEIVED = 7,
        PDU_TYPE_RESUPPLY_CANCEL = 8,
        PDU_TYPE_REPAIR_COMPLETE = 9,
        PDU_TYPE_REPAIR_RESPONSE = 10,
        PDU_TYPE_CREATE_ENTITY = 11,
        PDU_TYPE_REMOVE_ENTITY = 12,
        PDU_TYPE_START_RESUME = 13,
        PDU_TYPE_STOP_FREEZE = 14,
        PDU_TYPE_ACKNOWLEDGE = 15,
        PDU_TYPE_ACTION_REQUEST = 16,
        PDU_TYPE_ACTION_RESPONSE = 17,
        PDU_TYPE_DATA_QUERY = 18,
        PDU_TYPE_SET_DATA = 19,
        PDU_TYPE_DATA = 20,
        PDU_TYPE_EVENT_REPORT = 21,
        PDU_TYPE_COMMENT = 22,
        PDU_TYPE_EM_EMISSION = 23,
        PDU_TYPE_DESIGNATOR = 24,
        PDU_TYPE_TRANSMITTER = 25,
        PDU_TYPE_SIGNAL = 26,
        PDU_TYPE_RECEIVER = 27,
        PDU_TYPE_IFF = 28,
        PDU_TYPE_UNDERWATER_ACOUSTIC = 29,
        PDU_TYPE_SEES = 30,
        PDU_TYPE_INTERCOM_SIGNAL = 31,
        PDU_TYPE_INTERCOM_CONTROL = 32,
        PDU_TYPE_AGGREGATE_STATE = 33,
        PDU_TYPE_ISGROUPOF = 34,
        PDU_TYPE_TRANSFER_OWNERSHIP = 35,
        PDU_TYPE_ISPARTOF = 36,
        PDU_TYPE_MINEFIELD_STATE = 37,
        PDU_TYPE_MINEFIELD_QUERY = 38,
        PDU_TYPE_MINEFIELD_DATA = 39,
        PDU_TYPE_MINEFIELD_RESPONSE_NAK = 40,
        PDU_TYPE_ENVIRONMENTAL_PROCESS = 41,
        PDU_TYPE_GRIDDED_DATA = 42,
        PDU_TYPE_POINT_OBJECT_STATE = 43,
        PDU_TYPE_LINEAR_OBJECT_STATE = 44,
        PDU_TYPE_AREAL_OBJECT_STATE = 45,
        PDU_TYPE_TSPI = 46,
        PDU_TYPE_APPEARANCE = 47,
        PDU_TYPE_ARTICULATED_PARTS = 48,
        PDU_TYPE_LE_FIRE = 49,
        PDU_TYPE_LE_DETONATION = 50,
        PDU_TYPE_CREATE_ENTITY_R = 51,
        PDU_TYPE_REMOVE_ENTITY_R = 52,
        PDU_TYPE_START_RESUME_R = 53,
        PDU_TYPE_STOP_FREEZE_R = 54,
        PDU_TYPE_ACKNOWLEDGE_R = 55,
        PDU_TYPE_ACTION_REQUEST_R = 56,
        PDU_TYPE_ACTION_RESPONSE_R = 57,
        PDU_TYPE_DATA_QUERY_R = 58,
        PDU_TYPE_SET_DATA_R = 59,
        PDU_TYPE_DATA_R = 60,
        PDU_TYPE_EVENT_REPORT_R = 61,
        PDU_TYPE_COMMENT_R = 62,
        PDU_TYPE_RECORD_R = 63,
        PDU_TYPE_SET_RECORD_R = 64,
        PDU_TYPE_RECORD_QUERY_R = 65,
        PDU_TYPE_COLLISION_ELASTIC = 66,
        PDU_TYPE_ENTITY_STATE_UPDATE = 67,
        PDU_TYPE_DE_FIRE = 68,
        PDU_TYPE_ENTITY_DAMAGE_STATUS = 69,
        PDU_TYPE_INFO_OPS_ACTION = 70,
        PDU_TYPE_INFO_OPS_REPORT = 71,
        PDU_TYPE_ATTRIBUTE = 72,
        PDU_TYPE_APPLICATION_CTRL = 200;

    public static final int
        ACK_ACKNOWLEDGE_FLAG = 0,
        ACK_RESPONSE_FLAG = 1,
        ACTREQ_ACTION_IDS = 2,
        ACTRES_REQ_STATUS = 3,
        AIR_EQ_SLINGLOAD = 4,
        AIR_SMOKE = 5,
        AMPLITUDE = 6,
        AMPLITUDE_AND_ANGLE = 7,
        ANGLE = 8,
        ANTENNA_PATTERN_TYPE = 9,
        APP_CTRL_APPLICATION_TYPE = 10,
        APP_CTRL_CONTROL_TYPE = 11,
        ARTICULATED_PARTS = 12,
        ARTICULATED_PARTS_METRIC = 13,
        ATTACHED_PARTS = 14,
        BEAM_FUNCTION = 15,
        CDT_ONESAF_STATUS = 16,
        CDT_WAYPOINT_TRIGGER_TYPE = 17,
        CDT_WEATHER_TYPE = 18,
        COLORS = 19,
        COMBINATION = 20,
        CRYPTO_SYS = 21,
        CUSTOM_AIR_SMOKE = 22,
        CUSTOM_ARTICULATED_PARTS_METRIC = 23,
        CUSTOM_ENT_CNTRY = 24,
        CUSTOM_ENT_DOMAIN = 25,
        CUSTOM_ENT_KIND = 26,
        CUSTOM_FROZEN_BEHAVIOR = 27,
        CUSTOM_IED_PRESENCE = 28,
        CUSTOM_LAND_SMOKE = 29,
        CUSTOM_LF_EQ_LASER = 30,
        CUSTOM_OBJ_DAMAGE = 31,
        CUSTOM_ONOFF = 32,
        CUSTOM_SEVERITY = 33,
        CUSTOM_SLING_DAMAGE = 34,
        CUSTOM_YESNO = 35,
        DATUM_IDS = 36,
        DEAD_RECKONING = 37,
        DESIG_MOTION_PATTERN = 38,
        DESIG_OBJ_STATUS = 39,
        DESIG_SPOT_TYPE = 40,
        DESIG_SYSTEM_NAME = 41,
        DETONATION_RESULT = 42,
        DISGUISE_STATUS = 43,
        DOMAIN = 44,
        DR_TYPE = 45,
        EMITTER_FUNCTION = 46,
        EMITTER_NAME = 47,
        ENCODING_CLASS = 48,
        ENCODING_TYPE = 49,
        ENTITY_MARKING = 50,
        ENT_ASSOC_STATUS = 51,
        ENT_CNTRY = 52,
        ENT_KIND = 53,
        ETHNICITY = 54,
        FORCE_ID = 55,
        FROZEN_BEHAVIOR = 56,
        FUSE_BURST_DESC = 57,
        GENERIC_PRESENCE = 58,
        GRP_MEM_TYPE = 59,
        HOOK_TYPE = 60,
        HUMAN_AGE_GROUP = 61,
        IED_PRESENCE = 62,
        IFF_SYSTEM_MODE = 63,
        IFF_SYSTEM_NAME = 64,
        IFF_SYSTEM_TYPE = 65,
        INPUT_SOURCE = 66,
        JAMMING_TECHNIQUE = 67,
        LAND_SMOKE = 68,
        LASER_FUNCTION = 69,
        LF_CAMOUFLAGE = 70,
        LF_CLOTH_SCHEME = 71,
        LF_CLOTH_TYPE = 72,
        LF_COMPLIANCE = 73,
        LF_COND_EXT_DMG = 74,
        LF_EQ_CHUTE = 75,
        LF_EQ_LASER = 76,
        LF_HEALTH = 77,
        LF_POSTURE = 78,
        LF_WEAPON_STATE = 79,
        MAJOR_MODULATION = 80,
        OBJECT_DAMAGE = 81,
        OBJECT_GEOMETRY = 82,
        OBJECT_KIND = 83,
        OFFSET_TYPE = 84,
        ONOFF = 85,
        PDU_FAMILY = 86,
        PDU_STATUS_CEI = 87,
        PDU_STATUS_DMI = 88,
        PDU_STATUS_DTI = 89,
        PDU_STATUS_FTI = 90,
        PDU_STATUS_LVCI = 91,
        PDU_STATUS_RAI = 92,
        PDU_STATUS_TEI = 93,
        PDU_TYPE = 94,
        PHYS_ASSOC_TYPE = 95,
        PHYS_CONN_TYPE = 96,
        PL_COND_EXT_DMG = 97,
        PL_COND_MTL = 98,
        PL_DECAL_SCHEME = 99,
        PL_PAINT_SCHEME = 100,
        PRESENT_DOMAIN = 101,
        PROTOCOL_VERSION = 102,
        PULSE = 103,
        RADIO_SYSTEM = 104,
        RECEIVER = 105,
        SEVERITY = 106,
        SEX = 107,
        SF_REASON_CODES = 108,
        SLING_DAMAGE = 109,
        STATION_NAME = 110,
        TDL_TYPE = 111,
        TRANSMIT_STATE = 112,
        UNMODULATED = 113,
        VP_RECORD_TYPE = 114,
        WARHEAD_BURST_DESC = 115,
        YESNO = 116;

    public static final String ENUM_TYPE_NAMES[] = {
        "ACK_ACKNOWLEDGE_FLAG",
        "ACK_RESPONSE_FLAG",
        "ACTREQ_ACTION_IDS",
        "ACTRES_REQ_STATUS",
        "AIR_EQ_SLINGLOAD",
        "AIR_SMOKE",
        "AMPLITUDE",
        "AMPLITUDE_AND_ANGLE",
        "ANGLE",
        "ANTENNA_PATTERN_TYPE",
        "APP_CTRL_APPLICATION_TYPE",
        "APP_CTRL_CONTROL_TYPE",
        "ARTICULATED_PARTS",
        "ARTICULATED_PARTS_METRIC",
        "ATTACHED_PARTS",
        "BEAM_FUNCTION",
        "CDT_ONESAF_STATUS",
        "CDT_WAYPOINT_TRIGGER_TYPE",
        "CDT_WEATHER_TYPE",
        "COLORS",
        "COMBINATION",
        "CRYPTO_SYS",
        "CUSTOM_AIR_SMOKE",
        "CUSTOM_ARTICULATED_PARTS_METRIC",
        "CUSTOM_ENT_CNTRY",
        "CUSTOM_ENT_DOMAIN",
        "CUSTOM_ENT_KIND",
        "CUSTOM_FROZEN_BEHAVIOR",
        "CUSTOM_IED_PRESENCE",
        "CUSTOM_LAND_SMOKE",
        "CUSTOM_LF_EQ_LASER",
        "CUSTOM_OBJ_DAMAGE",
        "CUSTOM_ONOFF",
        "CUSTOM_SEVERITY",
        "CUSTOM_SLING_DAMAGE",
        "CUSTOM_YESNO",
        "DATUM_IDS",
        "DEAD_RECKONING",
        "DESIG_MOTION_PATTERN",
        "DESIG_OBJ_STATUS",
        "DESIG_SPOT_TYPE",
        "DESIG_SYSTEM_NAME",
        "DETONATION_RESULT",
        "DISGUISE_STATUS",
        "DOMAIN",
        "DR_TYPE",
        "EMITTER_FUNCTION",
        "EMITTER_NAME",
        "ENCODING_CLASS",
        "ENCODING_TYPE",
        "ENTITY_MARKING",
        "ENT_ASSOC_STATUS",
        "ENT_CNTRY",
        "ENT_KIND",
        "ETHNICITY",
        "FORCE_ID",
        "FROZEN_BEHAVIOR",
        "FUSE_BURST_DESC",
        "GENERIC_PRESENCE",
        "GRP_MEM_TYPE",
        "HOOK_TYPE",
        "HUMAN_AGE_GROUP",
        "IED_PRESENCE",
        "IFF_SYSTEM_MODE",
        "IFF_SYSTEM_NAME",
        "IFF_SYSTEM_TYPE",
        "INPUT_SOURCE",
        "JAMMING_TECHNIQUE",
        "LAND_SMOKE",
        "LASER_FUNCTION",
        "LF_CAMOUFLAGE",
        "LF_CLOTH_SCHEME",
        "LF_CLOTH_TYPE",
        "LF_COMPLIANCE",
        "LF_COND_EXT_DMG",
        "LF_EQ_CHUTE",
        "LF_EQ_LASER",
        "LF_HEALTH",
        "LF_POSTURE",
        "LF_WEAPON_STATE",
        "MAJOR_MODULATION",
        "OBJECT_DAMAGE",
        "OBJECT_GEOMETRY",
        "OBJECT_KIND",
        "OFFSET_TYPE",
        "ONOFF",
        "PDU_FAMILY",
        "PDU_STATUS_CEI",
        "PDU_STATUS_DMI",
        "PDU_STATUS_DTI",
        "PDU_STATUS_FTI",
        "PDU_STATUS_LVCI",
        "PDU_STATUS_RAI",
        "PDU_STATUS_TEI",
        "PDU_TYPE",
        "PHYS_ASSOC_TYPE",
        "PHYS_CONN_TYPE",
        "PL_COND_EXT_DMG",
        "PL_COND_MTL",
        "PL_DECAL_SCHEME",
        "PL_PAINT_SCHEME",
        "PRESENT_DOMAIN",
        "PROTOCOL_VERSION",
        "PULSE",
        "RADIO_SYSTEM",
        "RECEIVER",
        "SEVERITY",
        "SEX",
        "SF_REASON_CODES",
        "SLING_DAMAGE",
        "STATION_NAME",
        "TDL_TYPE",
        "TRANSMIT_STATE",
        "UNMODULATED",
        "VP_RECORD_TYPE",
        "WARHEAD_BURST_DESC",
        "YESNO" 
    };

    public static int[] getEnumValues(int type) {

        switch(type) {

            case ACK_ACKNOWLEDGE_FLAG: return VDISValues.ACK_ACKNOWLEDGE_FLAG;
            case ACK_RESPONSE_FLAG: return VDISValues.ACK_RESPONSE_FLAG;
            case ACTREQ_ACTION_IDS: return VDISValues.ACTREQ_ACTION_IDS;
            case ACTRES_REQ_STATUS: return VDISValues.ACTRES_REQ_STATUS;
            case AIR_EQ_SLINGLOAD: return VDISValues.AIR_EQ_SLINGLOAD;
            case AIR_SMOKE: return VDISValues.AIR_SMOKE;
            case AMPLITUDE: return VDISValues.AMPLITUDE;
            case AMPLITUDE_AND_ANGLE: return VDISValues.AMPLITUDE_AND_ANGLE;
            case ANGLE: return VDISValues.ANGLE;
            case ANTENNA_PATTERN_TYPE: return VDISValues.ANTENNA_PATTERN_TYPE;
            case APP_CTRL_APPLICATION_TYPE: return VDISValues.APP_CTRL_APPLICATION_TYPE;
            case APP_CTRL_CONTROL_TYPE: return VDISValues.APP_CTRL_CONTROL_TYPE;
            case ARTICULATED_PARTS: return VDISValues.ARTICULATED_PARTS;
            case ARTICULATED_PARTS_METRIC: return VDISValues.ARTICULATED_PARTS_METRIC;
            case ATTACHED_PARTS: return VDISValues.ATTACHED_PARTS;
            case BEAM_FUNCTION: return VDISValues.BEAM_FUNCTION;
            case CDT_ONESAF_STATUS: return VDISValues.CDT_ONESAF_STATUS;
            case CDT_WAYPOINT_TRIGGER_TYPE: return VDISValues.CDT_WAYPOINT_TRIGGER_TYPE;
            case CDT_WEATHER_TYPE: return VDISValues.CDT_WEATHER_TYPE;
            case COLORS: return VDISValues.COLORS;
            case COMBINATION: return VDISValues.COMBINATION;
            case CRYPTO_SYS: return VDISValues.CRYPTO_SYS;
            case CUSTOM_AIR_SMOKE: return VDISValues.CUSTOM_AIR_SMOKE;
            case CUSTOM_ARTICULATED_PARTS_METRIC: return VDISValues.CUSTOM_ARTICULATED_PARTS_METRIC;
            case CUSTOM_ENT_CNTRY: return VDISValues.CUSTOM_ENT_CNTRY;
            case CUSTOM_ENT_DOMAIN: return VDISValues.CUSTOM_ENT_DOMAIN;
            case CUSTOM_ENT_KIND: return VDISValues.CUSTOM_ENT_KIND;
            case CUSTOM_FROZEN_BEHAVIOR: return VDISValues.CUSTOM_FROZEN_BEHAVIOR;
            case CUSTOM_IED_PRESENCE: return VDISValues.CUSTOM_IED_PRESENCE;
            case CUSTOM_LAND_SMOKE: return VDISValues.CUSTOM_LAND_SMOKE;
            case CUSTOM_LF_EQ_LASER: return VDISValues.CUSTOM_LF_EQ_LASER;
            case CUSTOM_OBJ_DAMAGE: return VDISValues.CUSTOM_OBJ_DAMAGE;
            case CUSTOM_ONOFF: return VDISValues.CUSTOM_ONOFF;
            case CUSTOM_SEVERITY: return VDISValues.CUSTOM_SEVERITY;
            case CUSTOM_SLING_DAMAGE: return VDISValues.CUSTOM_SLING_DAMAGE;
            case CUSTOM_YESNO: return VDISValues.CUSTOM_YESNO;
            case DATUM_IDS: return VDISValues.DATUM_IDS;
            case DEAD_RECKONING: return VDISValues.DEAD_RECKONING;
            case DESIG_MOTION_PATTERN: return VDISValues.DESIG_MOTION_PATTERN;
            case DESIG_OBJ_STATUS: return VDISValues.DESIG_OBJ_STATUS;
            case DESIG_SPOT_TYPE: return VDISValues.DESIG_SPOT_TYPE;
            case DESIG_SYSTEM_NAME: return VDISValues.DESIG_SYSTEM_NAME;
            case DETONATION_RESULT: return VDISValues.DETONATION_RESULT;
            case DISGUISE_STATUS: return VDISValues.DISGUISE_STATUS;
            case DOMAIN: return VDISValues.DOMAIN;
            case DR_TYPE: return VDISValues.DR_TYPE;
            case EMITTER_FUNCTION: return VDISValues.EMITTER_FUNCTION;
            case EMITTER_NAME: return VDISValues.EMITTER_NAME;
            case ENCODING_CLASS: return VDISValues.ENCODING_CLASS;
            case ENCODING_TYPE: return VDISValues.ENCODING_TYPE;
            case ENTITY_MARKING: return VDISValues.ENTITY_MARKING;
            case ENT_ASSOC_STATUS: return VDISValues.ENT_ASSOC_STATUS;
            case ENT_CNTRY: return VDISValues.ENT_CNTRY;
            case ENT_KIND: return VDISValues.ENT_KIND;
            case ETHNICITY: return VDISValues.ETHNICITY;
            case FORCE_ID: return VDISValues.FORCE_ID;
            case FROZEN_BEHAVIOR: return VDISValues.FROZEN_BEHAVIOR;
            case FUSE_BURST_DESC: return VDISValues.FUSE_BURST_DESC;
            case GENERIC_PRESENCE: return VDISValues.GENERIC_PRESENCE;
            case GRP_MEM_TYPE: return VDISValues.GRP_MEM_TYPE;
            case HOOK_TYPE: return VDISValues.HOOK_TYPE;
            case HUMAN_AGE_GROUP: return VDISValues.HUMAN_AGE_GROUP;
            case IED_PRESENCE: return VDISValues.IED_PRESENCE;
            case IFF_SYSTEM_MODE: return VDISValues.IFF_SYSTEM_MODE;
            case IFF_SYSTEM_NAME: return VDISValues.IFF_SYSTEM_NAME;
            case IFF_SYSTEM_TYPE: return VDISValues.IFF_SYSTEM_TYPE;
            case INPUT_SOURCE: return VDISValues.INPUT_SOURCE;
            case JAMMING_TECHNIQUE: return VDISValues.JAMMING_TECHNIQUE;
            case LAND_SMOKE: return VDISValues.LAND_SMOKE;
            case LASER_FUNCTION: return VDISValues.LASER_FUNCTION;
            case LF_CAMOUFLAGE: return VDISValues.LF_CAMOUFLAGE;
            case LF_CLOTH_SCHEME: return VDISValues.LF_CLOTH_SCHEME;
            case LF_CLOTH_TYPE: return VDISValues.LF_CLOTH_TYPE;
            case LF_COMPLIANCE: return VDISValues.LF_COMPLIANCE;
            case LF_COND_EXT_DMG: return VDISValues.LF_COND_EXT_DMG;
            case LF_EQ_CHUTE: return VDISValues.LF_EQ_CHUTE;
            case LF_EQ_LASER: return VDISValues.LF_EQ_LASER;
            case LF_HEALTH: return VDISValues.LF_HEALTH;
            case LF_POSTURE: return VDISValues.LF_POSTURE;
            case LF_WEAPON_STATE: return VDISValues.LF_WEAPON_STATE;
            case MAJOR_MODULATION: return VDISValues.MAJOR_MODULATION;
            case OBJECT_DAMAGE: return VDISValues.OBJECT_DAMAGE;
            case OBJECT_GEOMETRY: return VDISValues.OBJECT_GEOMETRY;
            case OBJECT_KIND: return VDISValues.OBJECT_KIND;
            case OFFSET_TYPE: return VDISValues.OFFSET_TYPE;
            case ONOFF: return VDISValues.ONOFF;
            case PDU_FAMILY: return VDISValues.PDU_FAMILY;
            case PDU_STATUS_CEI: return VDISValues.PDU_STATUS_CEI;
            case PDU_STATUS_DMI: return VDISValues.PDU_STATUS_DMI;
            case PDU_STATUS_DTI: return VDISValues.PDU_STATUS_DTI;
            case PDU_STATUS_FTI: return VDISValues.PDU_STATUS_FTI;
            case PDU_STATUS_LVCI: return VDISValues.PDU_STATUS_LVCI;
            case PDU_STATUS_RAI: return VDISValues.PDU_STATUS_RAI;
            case PDU_STATUS_TEI: return VDISValues.PDU_STATUS_TEI;
            case PDU_TYPE: return VDISValues.PDU_TYPE;
            case PHYS_ASSOC_TYPE: return VDISValues.PHYS_ASSOC_TYPE;
            case PHYS_CONN_TYPE: return VDISValues.PHYS_CONN_TYPE;
            case PL_COND_EXT_DMG: return VDISValues.PL_COND_EXT_DMG;
            case PL_COND_MTL: return VDISValues.PL_COND_MTL;
            case PL_DECAL_SCHEME: return VDISValues.PL_DECAL_SCHEME;
            case PL_PAINT_SCHEME: return VDISValues.PL_PAINT_SCHEME;
            case PRESENT_DOMAIN: return VDISValues.PRESENT_DOMAIN;
            case PROTOCOL_VERSION: return VDISValues.PROTOCOL_VERSION;
            case PULSE: return VDISValues.PULSE;
            case RADIO_SYSTEM: return VDISValues.RADIO_SYSTEM;
            case RECEIVER: return VDISValues.RECEIVER;
            case SEVERITY: return VDISValues.SEVERITY;
            case SEX: return VDISValues.SEX;
            case SF_REASON_CODES: return VDISValues.SF_REASON_CODES;
            case SLING_DAMAGE: return VDISValues.SLING_DAMAGE;
            case STATION_NAME: return VDISValues.STATION_NAME;
            case TDL_TYPE: return VDISValues.TDL_TYPE;
            case TRANSMIT_STATE: return VDISValues.TRANSMIT_STATE;
            case UNMODULATED: return VDISValues.UNMODULATED;
            case VP_RECORD_TYPE: return VDISValues.VP_RECORD_TYPE;
            case WARHEAD_BURST_DESC: return VDISValues.WARHEAD_BURST_DESC;
            case YESNO: return VDISValues.YESNO;
            default: System.err.println("No values for type " + type);
        }

        return null;
    }

    public static String[] getEnumNames(int type) {

        switch(type) {

            case ACK_ACKNOWLEDGE_FLAG: return VDISNames.ACK_ACKNOWLEDGE_FLAG;
            case ACK_RESPONSE_FLAG: return VDISNames.ACK_RESPONSE_FLAG;
            case ACTREQ_ACTION_IDS: return VDISNames.ACTREQ_ACTION_IDS;
            case ACTRES_REQ_STATUS: return VDISNames.ACTRES_REQ_STATUS;
            case AIR_EQ_SLINGLOAD: return VDISNames.AIR_EQ_SLINGLOAD;
            case AIR_SMOKE: return VDISNames.AIR_SMOKE;
            case AMPLITUDE: return VDISNames.AMPLITUDE;
            case AMPLITUDE_AND_ANGLE: return VDISNames.AMPLITUDE_AND_ANGLE;
            case ANGLE: return VDISNames.ANGLE;
            case ANTENNA_PATTERN_TYPE: return VDISNames.ANTENNA_PATTERN_TYPE;
            case APP_CTRL_APPLICATION_TYPE: return VDISNames.APP_CTRL_APPLICATION_TYPE;
            case APP_CTRL_CONTROL_TYPE: return VDISNames.APP_CTRL_CONTROL_TYPE;
            case ARTICULATED_PARTS: return VDISNames.ARTICULATED_PARTS;
            case ARTICULATED_PARTS_METRIC: return VDISNames.ARTICULATED_PARTS_METRIC;
            case ATTACHED_PARTS: return VDISNames.ATTACHED_PARTS;
            case BEAM_FUNCTION: return VDISNames.BEAM_FUNCTION;
            case CDT_ONESAF_STATUS: return VDISNames.CDT_ONESAF_STATUS;
            case CDT_WAYPOINT_TRIGGER_TYPE: return VDISNames.CDT_WAYPOINT_TRIGGER_TYPE;
            case CDT_WEATHER_TYPE: return VDISNames.CDT_WEATHER_TYPE;
            case COLORS: return VDISNames.COLORS;
            case COMBINATION: return VDISNames.COMBINATION;
            case CRYPTO_SYS: return VDISNames.CRYPTO_SYS;
            case CUSTOM_AIR_SMOKE: return VDISNames.CUSTOM_AIR_SMOKE;
            case CUSTOM_ARTICULATED_PARTS_METRIC: return VDISNames.CUSTOM_ARTICULATED_PARTS_METRIC;
            case CUSTOM_ENT_CNTRY: return VDISNames.CUSTOM_ENT_CNTRY;
            case CUSTOM_ENT_DOMAIN: return VDISNames.CUSTOM_ENT_DOMAIN;
            case CUSTOM_ENT_KIND: return VDISNames.CUSTOM_ENT_KIND;
            case CUSTOM_FROZEN_BEHAVIOR: return VDISNames.CUSTOM_FROZEN_BEHAVIOR;
            case CUSTOM_IED_PRESENCE: return VDISNames.CUSTOM_IED_PRESENCE;
            case CUSTOM_LAND_SMOKE: return VDISNames.CUSTOM_LAND_SMOKE;
            case CUSTOM_LF_EQ_LASER: return VDISNames.CUSTOM_LF_EQ_LASER;
            case CUSTOM_OBJ_DAMAGE: return VDISNames.CUSTOM_OBJ_DAMAGE;
            case CUSTOM_ONOFF: return VDISNames.CUSTOM_ONOFF;
            case CUSTOM_SEVERITY: return VDISNames.CUSTOM_SEVERITY;
            case CUSTOM_SLING_DAMAGE: return VDISNames.CUSTOM_SLING_DAMAGE;
            case CUSTOM_YESNO: return VDISNames.CUSTOM_YESNO;
            case DATUM_IDS: return VDISNames.DATUM_IDS;
            case DEAD_RECKONING: return VDISNames.DEAD_RECKONING;
            case DESIG_MOTION_PATTERN: return VDISNames.DESIG_MOTION_PATTERN;
            case DESIG_OBJ_STATUS: return VDISNames.DESIG_OBJ_STATUS;
            case DESIG_SPOT_TYPE: return VDISNames.DESIG_SPOT_TYPE;
            case DESIG_SYSTEM_NAME: return VDISNames.DESIG_SYSTEM_NAME;
            case DETONATION_RESULT: return VDISNames.DETONATION_RESULT;
            case DISGUISE_STATUS: return VDISNames.DISGUISE_STATUS;
            case DOMAIN: return VDISNames.DOMAIN;
            case DR_TYPE: return VDISNames.DR_TYPE;
            case EMITTER_FUNCTION: return VDISNames.EMITTER_FUNCTION;
            case EMITTER_NAME: return VDISNames.EMITTER_NAME;
            case ENCODING_CLASS: return VDISNames.ENCODING_CLASS;
            case ENCODING_TYPE: return VDISNames.ENCODING_TYPE;
            case ENTITY_MARKING: return VDISNames.ENTITY_MARKING;
            case ENT_ASSOC_STATUS: return VDISNames.ENT_ASSOC_STATUS;
            case ENT_CNTRY: return VDISNames.ENT_CNTRY;
            case ENT_KIND: return VDISNames.ENT_KIND;
            case ETHNICITY: return VDISNames.ETHNICITY;
            case FORCE_ID: return VDISNames.FORCE_ID;
            case FROZEN_BEHAVIOR: return VDISNames.FROZEN_BEHAVIOR;
            case FUSE_BURST_DESC: return VDISNames.FUSE_BURST_DESC;
            case GENERIC_PRESENCE: return VDISNames.GENERIC_PRESENCE;
            case GRP_MEM_TYPE: return VDISNames.GRP_MEM_TYPE;
            case HOOK_TYPE: return VDISNames.HOOK_TYPE;
            case HUMAN_AGE_GROUP: return VDISNames.HUMAN_AGE_GROUP;
            case IED_PRESENCE: return VDISNames.IED_PRESENCE;
            case IFF_SYSTEM_MODE: return VDISNames.IFF_SYSTEM_MODE;
            case IFF_SYSTEM_NAME: return VDISNames.IFF_SYSTEM_NAME;
            case IFF_SYSTEM_TYPE: return VDISNames.IFF_SYSTEM_TYPE;
            case INPUT_SOURCE: return VDISNames.INPUT_SOURCE;
            case JAMMING_TECHNIQUE: return VDISNames.JAMMING_TECHNIQUE;
            case LAND_SMOKE: return VDISNames.LAND_SMOKE;
            case LASER_FUNCTION: return VDISNames.LASER_FUNCTION;
            case LF_CAMOUFLAGE: return VDISNames.LF_CAMOUFLAGE;
            case LF_CLOTH_SCHEME: return VDISNames.LF_CLOTH_SCHEME;
            case LF_CLOTH_TYPE: return VDISNames.LF_CLOTH_TYPE;
            case LF_COMPLIANCE: return VDISNames.LF_COMPLIANCE;
            case LF_COND_EXT_DMG: return VDISNames.LF_COND_EXT_DMG;
            case LF_EQ_CHUTE: return VDISNames.LF_EQ_CHUTE;
            case LF_EQ_LASER: return VDISNames.LF_EQ_LASER;
            case LF_HEALTH: return VDISNames.LF_HEALTH;
            case LF_POSTURE: return VDISNames.LF_POSTURE;
            case LF_WEAPON_STATE: return VDISNames.LF_WEAPON_STATE;
            case MAJOR_MODULATION: return VDISNames.MAJOR_MODULATION;
            case OBJECT_DAMAGE: return VDISNames.OBJECT_DAMAGE;
            case OBJECT_GEOMETRY: return VDISNames.OBJECT_GEOMETRY;
            case OBJECT_KIND: return VDISNames.OBJECT_KIND;
            case OFFSET_TYPE: return VDISNames.OFFSET_TYPE;
            case ONOFF: return VDISNames.ONOFF;
            case PDU_FAMILY: return VDISNames.PDU_FAMILY;
            case PDU_STATUS_CEI: return VDISNames.PDU_STATUS_CEI;
            case PDU_STATUS_DMI: return VDISNames.PDU_STATUS_DMI;
            case PDU_STATUS_DTI: return VDISNames.PDU_STATUS_DTI;
            case PDU_STATUS_FTI: return VDISNames.PDU_STATUS_FTI;
            case PDU_STATUS_LVCI: return VDISNames.PDU_STATUS_LVCI;
            case PDU_STATUS_RAI: return VDISNames.PDU_STATUS_RAI;
            case PDU_STATUS_TEI: return VDISNames.PDU_STATUS_TEI;
            case PDU_TYPE: return VDISNames.PDU_TYPE;
            case PHYS_ASSOC_TYPE: return VDISNames.PHYS_ASSOC_TYPE;
            case PHYS_CONN_TYPE: return VDISNames.PHYS_CONN_TYPE;
            case PL_COND_EXT_DMG: return VDISNames.PL_COND_EXT_DMG;
            case PL_COND_MTL: return VDISNames.PL_COND_MTL;
            case PL_DECAL_SCHEME: return VDISNames.PL_DECAL_SCHEME;
            case PL_PAINT_SCHEME: return VDISNames.PL_PAINT_SCHEME;
            case PRESENT_DOMAIN: return VDISNames.PRESENT_DOMAIN;
            case PROTOCOL_VERSION: return VDISNames.PROTOCOL_VERSION;
            case PULSE: return VDISNames.PULSE;
            case RADIO_SYSTEM: return VDISNames.RADIO_SYSTEM;
            case RECEIVER: return VDISNames.RECEIVER;
            case SEVERITY: return VDISNames.SEVERITY;
            case SEX: return VDISNames.SEX;
            case SF_REASON_CODES: return VDISNames.SF_REASON_CODES;
            case SLING_DAMAGE: return VDISNames.SLING_DAMAGE;
            case STATION_NAME: return VDISNames.STATION_NAME;
            case TDL_TYPE: return VDISNames.TDL_TYPE;
            case TRANSMIT_STATE: return VDISNames.TRANSMIT_STATE;
            case UNMODULATED: return VDISNames.UNMODULATED;
            case VP_RECORD_TYPE: return VDISNames.VP_RECORD_TYPE;
            case WARHEAD_BURST_DESC: return VDISNames.WARHEAD_BURST_DESC;
            case YESNO: return VDISNames.YESNO;
            default: System.err.println("No names for type " + type);
        }

        return null;
    }

    public static String[] getEnumDescriptions(int type) {

        switch(type) {

            case ACK_ACKNOWLEDGE_FLAG: return VDISDescriptions.ACK_ACKNOWLEDGE_FLAG;
            case ACK_RESPONSE_FLAG: return VDISDescriptions.ACK_RESPONSE_FLAG;
            case ACTREQ_ACTION_IDS: return VDISDescriptions.ACTREQ_ACTION_IDS;
            case ACTRES_REQ_STATUS: return VDISDescriptions.ACTRES_REQ_STATUS;
            case AIR_EQ_SLINGLOAD: return VDISDescriptions.AIR_EQ_SLINGLOAD;
            case AIR_SMOKE: return VDISDescriptions.AIR_SMOKE;
            case AMPLITUDE: return VDISDescriptions.AMPLITUDE;
            case AMPLITUDE_AND_ANGLE: return VDISDescriptions.AMPLITUDE_AND_ANGLE;
            case ANGLE: return VDISDescriptions.ANGLE;
            case ANTENNA_PATTERN_TYPE: return VDISDescriptions.ANTENNA_PATTERN_TYPE;
            case APP_CTRL_APPLICATION_TYPE: return VDISDescriptions.APP_CTRL_APPLICATION_TYPE;
            case APP_CTRL_CONTROL_TYPE: return VDISDescriptions.APP_CTRL_CONTROL_TYPE;
            case ARTICULATED_PARTS: return VDISDescriptions.ARTICULATED_PARTS;
            case ARTICULATED_PARTS_METRIC: return VDISDescriptions.ARTICULATED_PARTS_METRIC;
            case ATTACHED_PARTS: return VDISDescriptions.ATTACHED_PARTS;
            case BEAM_FUNCTION: return VDISDescriptions.BEAM_FUNCTION;
            case CDT_ONESAF_STATUS: return VDISDescriptions.CDT_ONESAF_STATUS;
            case CDT_WAYPOINT_TRIGGER_TYPE: return VDISDescriptions.CDT_WAYPOINT_TRIGGER_TYPE;
            case CDT_WEATHER_TYPE: return VDISDescriptions.CDT_WEATHER_TYPE;
            case COLORS: return VDISDescriptions.COLORS;
            case COMBINATION: return VDISDescriptions.COMBINATION;
            case CRYPTO_SYS: return VDISDescriptions.CRYPTO_SYS;
            case CUSTOM_AIR_SMOKE: return VDISDescriptions.CUSTOM_AIR_SMOKE;
            case CUSTOM_ARTICULATED_PARTS_METRIC: return VDISDescriptions.CUSTOM_ARTICULATED_PARTS_METRIC;
            case CUSTOM_ENT_CNTRY: return VDISDescriptions.CUSTOM_ENT_CNTRY;
            case CUSTOM_ENT_DOMAIN: return VDISDescriptions.CUSTOM_ENT_DOMAIN;
            case CUSTOM_ENT_KIND: return VDISDescriptions.CUSTOM_ENT_KIND;
            case CUSTOM_FROZEN_BEHAVIOR: return VDISDescriptions.CUSTOM_FROZEN_BEHAVIOR;
            case CUSTOM_IED_PRESENCE: return VDISDescriptions.CUSTOM_IED_PRESENCE;
            case CUSTOM_LAND_SMOKE: return VDISDescriptions.CUSTOM_LAND_SMOKE;
            case CUSTOM_LF_EQ_LASER: return VDISDescriptions.CUSTOM_LF_EQ_LASER;
            case CUSTOM_OBJ_DAMAGE: return VDISDescriptions.CUSTOM_OBJ_DAMAGE;
            case CUSTOM_ONOFF: return VDISDescriptions.CUSTOM_ONOFF;
            case CUSTOM_SEVERITY: return VDISDescriptions.CUSTOM_SEVERITY;
            case CUSTOM_SLING_DAMAGE: return VDISDescriptions.CUSTOM_SLING_DAMAGE;
            case CUSTOM_YESNO: return VDISDescriptions.CUSTOM_YESNO;
            case DATUM_IDS: return VDISDescriptions.DATUM_IDS;
            case DEAD_RECKONING: return VDISDescriptions.DEAD_RECKONING;
            case DESIG_MOTION_PATTERN: return VDISDescriptions.DESIG_MOTION_PATTERN;
            case DESIG_OBJ_STATUS: return VDISDescriptions.DESIG_OBJ_STATUS;
            case DESIG_SPOT_TYPE: return VDISDescriptions.DESIG_SPOT_TYPE;
            case DESIG_SYSTEM_NAME: return VDISDescriptions.DESIG_SYSTEM_NAME;
            case DETONATION_RESULT: return VDISDescriptions.DETONATION_RESULT;
            case DISGUISE_STATUS: return VDISDescriptions.DISGUISE_STATUS;
            case DOMAIN: return VDISDescriptions.DOMAIN;
            case DR_TYPE: return VDISDescriptions.DR_TYPE;
            case EMITTER_FUNCTION: return VDISDescriptions.EMITTER_FUNCTION;
            case EMITTER_NAME: return VDISDescriptions.EMITTER_NAME;
            case ENCODING_CLASS: return VDISDescriptions.ENCODING_CLASS;
            case ENCODING_TYPE: return VDISDescriptions.ENCODING_TYPE;
            case ENTITY_MARKING: return VDISDescriptions.ENTITY_MARKING;
            case ENT_ASSOC_STATUS: return VDISDescriptions.ENT_ASSOC_STATUS;
            case ENT_CNTRY: return VDISDescriptions.ENT_CNTRY;
            case ENT_KIND: return VDISDescriptions.ENT_KIND;
            case ETHNICITY: return VDISDescriptions.ETHNICITY;
            case FORCE_ID: return VDISDescriptions.FORCE_ID;
            case FROZEN_BEHAVIOR: return VDISDescriptions.FROZEN_BEHAVIOR;
            case FUSE_BURST_DESC: return VDISDescriptions.FUSE_BURST_DESC;
            case GENERIC_PRESENCE: return VDISDescriptions.GENERIC_PRESENCE;
            case GRP_MEM_TYPE: return VDISDescriptions.GRP_MEM_TYPE;
            case HOOK_TYPE: return VDISDescriptions.HOOK_TYPE;
            case HUMAN_AGE_GROUP: return VDISDescriptions.HUMAN_AGE_GROUP;
            case IED_PRESENCE: return VDISDescriptions.IED_PRESENCE;
            case IFF_SYSTEM_MODE: return VDISDescriptions.IFF_SYSTEM_MODE;
            case IFF_SYSTEM_NAME: return VDISDescriptions.IFF_SYSTEM_NAME;
            case IFF_SYSTEM_TYPE: return VDISDescriptions.IFF_SYSTEM_TYPE;
            case INPUT_SOURCE: return VDISDescriptions.INPUT_SOURCE;
            case JAMMING_TECHNIQUE: return VDISDescriptions.JAMMING_TECHNIQUE;
            case LAND_SMOKE: return VDISDescriptions.LAND_SMOKE;
            case LASER_FUNCTION: return VDISDescriptions.LASER_FUNCTION;
            case LF_CAMOUFLAGE: return VDISDescriptions.LF_CAMOUFLAGE;
            case LF_CLOTH_SCHEME: return VDISDescriptions.LF_CLOTH_SCHEME;
            case LF_CLOTH_TYPE: return VDISDescriptions.LF_CLOTH_TYPE;
            case LF_COMPLIANCE: return VDISDescriptions.LF_COMPLIANCE;
            case LF_COND_EXT_DMG: return VDISDescriptions.LF_COND_EXT_DMG;
            case LF_EQ_CHUTE: return VDISDescriptions.LF_EQ_CHUTE;
            case LF_EQ_LASER: return VDISDescriptions.LF_EQ_LASER;
            case LF_HEALTH: return VDISDescriptions.LF_HEALTH;
            case LF_POSTURE: return VDISDescriptions.LF_POSTURE;
            case LF_WEAPON_STATE: return VDISDescriptions.LF_WEAPON_STATE;
            case MAJOR_MODULATION: return VDISDescriptions.MAJOR_MODULATION;
            case OBJECT_DAMAGE: return VDISDescriptions.OBJECT_DAMAGE;
            case OBJECT_GEOMETRY: return VDISDescriptions.OBJECT_GEOMETRY;
            case OBJECT_KIND: return VDISDescriptions.OBJECT_KIND;
            case OFFSET_TYPE: return VDISDescriptions.OFFSET_TYPE;
            case ONOFF: return VDISDescriptions.ONOFF;
            case PDU_FAMILY: return VDISDescriptions.PDU_FAMILY;
            case PDU_STATUS_CEI: return VDISDescriptions.PDU_STATUS_CEI;
            case PDU_STATUS_DMI: return VDISDescriptions.PDU_STATUS_DMI;
            case PDU_STATUS_DTI: return VDISDescriptions.PDU_STATUS_DTI;
            case PDU_STATUS_FTI: return VDISDescriptions.PDU_STATUS_FTI;
            case PDU_STATUS_LVCI: return VDISDescriptions.PDU_STATUS_LVCI;
            case PDU_STATUS_RAI: return VDISDescriptions.PDU_STATUS_RAI;
            case PDU_STATUS_TEI: return VDISDescriptions.PDU_STATUS_TEI;
            case PDU_TYPE: return VDISDescriptions.PDU_TYPE;
            case PHYS_ASSOC_TYPE: return VDISDescriptions.PHYS_ASSOC_TYPE;
            case PHYS_CONN_TYPE: return VDISDescriptions.PHYS_CONN_TYPE;
            case PL_COND_EXT_DMG: return VDISDescriptions.PL_COND_EXT_DMG;
            case PL_COND_MTL: return VDISDescriptions.PL_COND_MTL;
            case PL_DECAL_SCHEME: return VDISDescriptions.PL_DECAL_SCHEME;
            case PL_PAINT_SCHEME: return VDISDescriptions.PL_PAINT_SCHEME;
            case PRESENT_DOMAIN: return VDISDescriptions.PRESENT_DOMAIN;
            case PROTOCOL_VERSION: return VDISDescriptions.PROTOCOL_VERSION;
            case PULSE: return VDISDescriptions.PULSE;
            case RADIO_SYSTEM: return VDISDescriptions.RADIO_SYSTEM;
            case RECEIVER: return VDISDescriptions.RECEIVER;
            case SEVERITY: return VDISDescriptions.SEVERITY;
            case SEX: return VDISDescriptions.SEX;
            case SF_REASON_CODES: return VDISDescriptions.SF_REASON_CODES;
            case SLING_DAMAGE: return VDISDescriptions.SLING_DAMAGE;
            case STATION_NAME: return VDISDescriptions.STATION_NAME;
            case TDL_TYPE: return VDISDescriptions.TDL_TYPE;
            case TRANSMIT_STATE: return VDISDescriptions.TRANSMIT_STATE;
            case UNMODULATED: return VDISDescriptions.UNMODULATED;
            case VP_RECORD_TYPE: return VDISDescriptions.VP_RECORD_TYPE;
            case WARHEAD_BURST_DESC: return VDISDescriptions.WARHEAD_BURST_DESC;
            case YESNO: return VDISDescriptions.YESNO;
            default: System.err.println("No descriptions for type " + type);
        }

        return null;
    }

    public static String getTypeName(int type) {

        if ((type >= 0) && (type < 117)) {

            return ENUM_TYPE_NAMES[type];
        }

        System.err.println("Invalid type: " + type);

        return null;
    }

    public static String getName(int type, int value) {

       return getHandle(type).getName(value);
    }

    public static String getDescription(int type, int value) {

        return getHandle(type).getDescription(value);
    }

    public static String getUnknown(String name, int value) {

        HashMap<Integer, String> typeUnknowns = unknowns.get(name);

        if (typeUnknowns == null) {

            typeUnknowns = new HashMap<Integer, String>();

            unknowns.put(name, typeUnknowns);
        }

       String unknown = typeUnknowns.get(value);

        if (unknown == null) {

            unknown = (name + "_" + value);

            typeUnknowns.put(value, unknown);
        }

        return unknown;
}

    public static Handle getHandle(int type) {

         Handle handle = handles.get(type);

         if (handle == null) {

             handle = new Handle(
                  getTypeName(type),
                  getEnumValues(type),
                  getEnumNames(type),
                  getEnumDescriptions(type));

             handles.put(type, handle);
         }

         return handle;
     }

}
