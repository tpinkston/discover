package vdis.parsers;

import vdis.handlers.GenericEnumerationHandler;
import vdis.handlers.JammingTechniqueHandler;

/**
 * @author Tony Pinkston
 *
 */
public class OtherEnumsParser extends AbstractSpreadsheetParser {

    public OtherEnumsParser() {

        handlers.put("JAMMING_TECHNIQUE", new JammingTechniqueHandler());

        addGenericHandler("ACK_ACKNOWLEDGE_FLAG");
        addGenericHandler("ACK_RESPONSE_FLAG");
        addGenericHandler("ACTRES_REQ_STATUS");
        addGenericHandler("ACTREQ_ACTION_IDS");
        addGenericHandler("AGGREGATE_KIND");
        addGenericHandler("AGGREGATE_STATE");
        addGenericHandler("AMPLITUDE");
        addGenericHandler("AMPLITUDE_AND_ANGLE");
        addGenericHandler("ANGLE");
        addGenericHandler("ANTENNA_PATTERN_TYPE");
        addGenericHandler("AREAL_MINEFIELD_BREACH");
        addGenericHandler("ARTICULATED_PARTS");
        addGenericHandler("ATTACHED_PARTS");
        addGenericHandler("BEAM_FUNCTION");
        addGenericHandler("CLASSIFICATION_ENUM");
        addGenericHandler("CLEAR_CHANNEL");
        addGenericHandler("COLLISION");
        addGenericHandler("COMBINATION");
        addGenericHandler("COMMAND");
        addGenericHandler("COMMS_TYPE");
        addGenericHandler("CONST_GRID");
        addGenericHandler("CONTROL_TYPE");
        addGenericHandler("COORD_SYSTEM");
        addGenericHandler("CRYPTO_SYS");
        addGenericHandler("DATA_REP");
        addGenericHandler("DEAD_RECKONING");
        addGenericHandler("DESTINATION_LINE");
        addGenericHandler("DETONATION_RESULT");
        addGenericHandler("EMITTER_FUNCTION");
        addGenericHandler("ENCODING_CLASS");
        addGenericHandler("ENCODING_TYPE");
        addGenericHandler("EVENT_TYPE");
        addGenericHandler("FORCE_ID");
        addGenericHandler("FORMATION");
        addGenericHandler("FUSE_BURST_DESC");
        addGenericHandler("GROUPED_ENTITY");
        addGenericHandler("IFF_SYSTEM_NAME");
        addGenericHandler("IFF_SYSTEM_TYPE");
        addGenericHandler("IFF_SYSTEM_MODE");
        addGenericHandler("INPUT_SOURCE");
        addGenericHandler("NATURE");
        addGenericHandler("MAJOR_MODULATION");
        addGenericHandler("MESSAGE_FORMAT_ENUM");
        addGenericHandler("OBJECT_KIND");
        addGenericHandler("PDU_FAMILY");
        addGenericHandler("PDU_TYPE");
        addGenericHandler("POSITION");
        addGenericHandler("PRECEDENCE_ENUM");
        addGenericHandler("PROTOCOL_VERSION");
        addGenericHandler("PULSE");
        addGenericHandler("RADIO_SYSTEM");
        addGenericHandler("RECEIVER");
        addGenericHandler("RECORD_TYPE");
        addGenericHandler("REFERENCE_SYSTEM");
        addGenericHandler("REPAIR_RESPONSE");
        addGenericHandler("REPAIR_TYPE");
        addGenericHandler("REST_STATUS");
        addGenericHandler("SERVICE_TYPE");
        addGenericHandler("SF_REASON_CODES");
        addGenericHandler("START_OF_MESSAGE");
        addGenericHandler("STATION_NAME");
        addGenericHandler("SYNCHRONIZATION_STATE");
        addGenericHandler("TDL_TYPE");
        addGenericHandler("TERMINAL_PRIMARY");
        addGenericHandler("TERMINAL_SECONDARY");
        addGenericHandler("TRANSFER_TYPE");
        addGenericHandler("TRANSMIT_LINE");
        addGenericHandler("TRANSMIT_STATE");
        addGenericHandler("UA_FUNCTION");
        addGenericHandler("UA_PROP_PLANT");
        addGenericHandler("UA_SCAN");
        addGenericHandler("UA_SYS_NAME");
        addGenericHandler("UNMODULATED");
        addGenericHandler("USMTF_VERSION_ENUM");
        addGenericHandler("VMF_VERSION_ENUM");
        addGenericHandler("AFAPD_VERSION_ENUM");
        addGenericHandler("WARHEAD_BURST_DESC");
        addGenericHandler("CFS_TETHER");
        addGenericHandler("LASER_FUNCTION");
        addGenericHandler("RQR_EVENT_TYPE");
        addGenericHandler("EDS_COMP_ID");
        addGenericHandler("EDS_DMG_STATUS");
        addGenericHandler("EDS_SURF_DMG");
        addGenericHandler("DE_BEAM_SHAPE");
        addGenericHandler("WS_CEL_SEASON");
        addGenericHandler("WS_CEL_TOD_MODE");
        addGenericHandler("WS_CEL_TOD_SCENE");
        addGenericHandler("WS_CEL_NIGHT_ILL");
        addGenericHandler("WS_PREC_TYPE");
        addGenericHandler("WS_PREC_RAIN");
        addGenericHandler("WS_PREC_RATE");
        addGenericHandler("WS_CLOUDS_STATUS");
        addGenericHandler("WS_CLOUDS_TYPE");
        addGenericHandler("WS_CLOUDS_DENS");
        addGenericHandler("WS_CLOUDS_SCUD_FLAGS");
        addGenericHandler("WS_GR_FOG_STATUS");
        addGenericHandler("WS_HAZE_STATUS");
        addGenericHandler("WS_HAZE_TYPE");
        addGenericHandler("WS_LIGHT_STATUS");
        addGenericHandler("WS_THUN_STATUS");
        addGenericHandler("WS_LAYER_TYPE");
        addGenericHandler("RADIO_TYPE_CAT");
        addGenericHandler("PDU_STATUS_TEI");
        addGenericHandler("PDU_STATUS_LVCI");
        addGenericHandler("PDU_STATUS_FTI");
        addGenericHandler("PDU_STATUS_DTI");
        addGenericHandler("PDU_STATUS_CEI");
        addGenericHandler("PDU_STATUS_RAI");
        addGenericHandler("PDU_STATUS_DMI");
        addGenericHandler("COLLISION_ORIGIN");
        addGenericHandler("DESIG_SPOT_TYPE");
        addGenericHandler("DESIG_MOTION_PATTERN");
        addGenericHandler("DESIG_SYSTEM_NAME");
        addGenericHandler("DESIG_OBJ_STATUS");
        addGenericHandler("DESTINATION_TYPE");
        addGenericHandler("SPEED_BUMP_MTL");
        addGenericHandler("PL_PAINT_SCHEME");
        addGenericHandler("PL_DECAL_SCHEME");
        addGenericHandler("PL_COND_EXT_DMG");
        addGenericHandler("PL_COND_CLEAN");
        addGenericHandler("PL_COND_RUST");
        addGenericHandler("PL_COND_MTL");
        addGenericHandler("AIR_AC_LIGHTS");
        addGenericHandler("AIR_FORM_PAT");
        addGenericHandler("AIR_LIGHT_MODE");
        addGenericHandler("AIR_EQ_SLINGLOAD");
        addGenericHandler("LF_CLOTH_SCHEME");
        addGenericHandler("LF_DECAL_SCHEME");
        addGenericHandler("LF_PRIM_COND_HEAD");
        addGenericHandler("LF_SEC_COND_FACIAL");
        addGenericHandler("LF_COND_MTL");
        addGenericHandler("LF_COND_EXT_DMG");
        addGenericHandler("LF_COND_CLEAN");
        addGenericHandler("LF_EQ_CHUTE");
        addGenericHandler("PRESENT_DOMAIN");
        addGenericHandler("DISGUISE_STATUS");
        addGenericHandler("ENT_ASSOC_STATUS");
        addGenericHandler("PHYS_ASSOC_TYPE");
        addGenericHandler("PHYS_CONN_TYPE");
        addGenericHandler("DR_TYPE");
        addGenericHandler("GRP_MEM_TYPE");
        addGenericHandler("OFFSET_TYPE");
        addGenericHandler("HEADGAZWEAPAIM_TYPE");
        addGenericHandler("ACTION_SEQ_DIR");
        addGenericHandler("ACTION_SEQ_TYPE");
        addGenericHandler("VP_RECORD_TYPE");
        addGenericHandler("STEALTH_SPECTRUM_TYPE");
        addGenericHandler("STEALTH_ATTACH_COMMAND");
        addGenericHandler("STEALTH_ATTACH_MODE");
        addGenericHandler("STEALTHSTATE_SENSOR_MODE");
        addGenericHandler("MUNITION_STATUS");
        addGenericHandler("FUEL_MEAS_UNITS");
        addGenericHandler("HOOK_TYPE");
        addGenericHandler("DATUM_IDS");
        addGenericHandler("COLORS");
        addGenericHandler("ENTITY_MARKING");
        addGenericHandler("RADAR_TRACK");
        addGenericHandler("LEAF_COVERAGE");
    }

    @Override
    public String getFileName() {

        return getFilePath() + "/Other_Enums.xlsx";
    }

    private void addGenericHandler(String name) {

        handlers.put(name, new GenericEnumerationHandler(name));
    }
}
