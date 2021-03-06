package discover.vdis.enums;

import java.util.List;

/**
 * PDU_TYPE: This class is auto-generated by vdis.EnumGenerator
 */
public final class PDU_TYPE extends Value {

    public static final PDU_TYPE
       OTHER = new PDU_TYPE(0, "OTHER", "Other", true),
       ENTITY_STATE = new PDU_TYPE(1, "ENTITY_STATE", "Entity State", true),
       FIRE = new PDU_TYPE(2, "FIRE", "Fire", true),
       DETONATION = new PDU_TYPE(3, "DETONATION", "Detonation", true),
       COLLISION = new PDU_TYPE(4, "COLLISION", "Collision", true),
       SERVICE_REQ = new PDU_TYPE(5, "SERVICE_REQ", "Service Request", true),
       RESUPPLY_OFFER = new PDU_TYPE(6, "RESUPPLY_OFFER", "Resupply Offer", true),
       RESUPPLY_RECEIVED = new PDU_TYPE(7, "RESUPPLY_RECEIVED", "Resupply Received", true),
       RESUPPLY_CANCEL = new PDU_TYPE(8, "RESUPPLY_CANCEL", "Resupply Cancel", true),
       REPAIR_COMPLETE = new PDU_TYPE(9, "REPAIR_COMPLETE", "Repair Complete", true),
       REPAIR_RESPONSE = new PDU_TYPE(10, "REPAIR_RESPONSE", "Repair Response", true),
       CREATE_ENTITY = new PDU_TYPE(11, "CREATE_ENTITY", "Create Entity", true),
       REMOVE_ENTITY = new PDU_TYPE(12, "REMOVE_ENTITY", "Remove Entity", true),
       START_RESUME = new PDU_TYPE(13, "START_RESUME", "Start / Resume", true),
       STOP_FREEZE = new PDU_TYPE(14, "STOP_FREEZE", "Stop / Freeze", true),
       ACKNOWLEDGE = new PDU_TYPE(15, "ACKNOWLEDGE", "Acknowledge", true),
       ACTION_REQUEST = new PDU_TYPE(16, "ACTION_REQUEST", "Action Request", true),
       ACTION_RESPONSE = new PDU_TYPE(17, "ACTION_RESPONSE", "Action Response", true),
       DATA_QUERY = new PDU_TYPE(18, "DATA_QUERY", "Data Query", true),
       SET_DATA = new PDU_TYPE(19, "SET_DATA", "Set Data", true),
       DATA = new PDU_TYPE(20, "DATA", "Data", true),
       EVENT_REPORT = new PDU_TYPE(21, "EVENT_REPORT", "Event Report", true),
       COMMENT = new PDU_TYPE(22, "COMMENT", "Comment", true),
       EM_EMISSION = new PDU_TYPE(23, "EM_EMISSION", "Electromagnetic Emission", true),
       DESIGNATOR = new PDU_TYPE(24, "DESIGNATOR", "Designator", true),
       TRANSMITTER = new PDU_TYPE(25, "TRANSMITTER", "Transmitter", true),
       SIGNAL = new PDU_TYPE(26, "SIGNAL", "Signal", true),
       RECEIVER = new PDU_TYPE(27, "RECEIVER", "Receiver", true),
       IFF = new PDU_TYPE(28, "IFF", "Identification Friend or Foe", true),
       UNDERWATER_ACOUSTIC = new PDU_TYPE(29, "UNDERWATER_ACOUSTIC", "Underwater Acoustic", true),
       SEES = new PDU_TYPE(30, "SEES", "Supplemental Emission / Entity State", true),
       INTERCOM_SIGNAL = new PDU_TYPE(31, "INTERCOM_SIGNAL", "Intercom Signal", true),
       INTERCOM_CONTROL = new PDU_TYPE(32, "INTERCOM_CONTROL", "Intercom Control", true),
       AGGREGATE_STATE = new PDU_TYPE(33, "AGGREGATE_STATE", "Aggregate State", true),
       ISGROUPOF = new PDU_TYPE(34, "ISGROUPOF", "Is Group Of", true),
       TRANSFER_OWNERSHIP = new PDU_TYPE(35, "TRANSFER_OWNERSHIP", "Transfer Ownership", true),
       ISPARTOF = new PDU_TYPE(36, "ISPARTOF", "Is Part Of", true),
       MINEFIELD_STATE = new PDU_TYPE(37, "MINEFIELD_STATE", "Minefield State", true),
       MINEFIELD_QUERY = new PDU_TYPE(38, "MINEFIELD_QUERY", "Minefield Query", true),
       MINEFIELD_DATA = new PDU_TYPE(39, "MINEFIELD_DATA", "Minefield Data", true),
       MINEFIELD_RESPONSE_NAK = new PDU_TYPE(40, "MINEFIELD_RESPONSE_NAK", "Minefield Response NAK", true),
       ENVIRONMENTAL_PROCESS = new PDU_TYPE(41, "ENVIRONMENTAL_PROCESS", "Environmental Process", true),
       GRIDDED_DATA = new PDU_TYPE(42, "GRIDDED_DATA", "Gridded Data", true),
       POINT_OBJECT_STATE = new PDU_TYPE(43, "POINT_OBJECT_STATE", "Point Object State", true),
       LINEAR_OBJECT_STATE = new PDU_TYPE(44, "LINEAR_OBJECT_STATE", "Linear Object State", true),
       AREAL_OBJECT_STATE = new PDU_TYPE(45, "AREAL_OBJECT_STATE", "Areal Object State", true),
       TSPI = new PDU_TYPE(46, "TSPI", "TSPI", true),
       APPEARANCE = new PDU_TYPE(47, "APPEARANCE", "Appearance", true),
       ARTICULATED_PARTS = new PDU_TYPE(48, "ARTICULATED_PARTS", "Articulated Parts", true),
       LE_FIRE = new PDU_TYPE(49, "LE_FIRE", "Live Entity Fire", true),
       LE_DETONATION = new PDU_TYPE(50, "LE_DETONATION", "Live Entity Detonation", true),
       CREATE_ENTITY_R = new PDU_TYPE(51, "CREATE_ENTITY_R", "Create Entity-R", true),
       REMOVE_ENTITY_R = new PDU_TYPE(52, "REMOVE_ENTITY_R", "Remove Entity-R", true),
       START_RESUME_R = new PDU_TYPE(53, "START_RESUME_R", "Start / Resume-R", true),
       STOP_FREEZE_R = new PDU_TYPE(54, "STOP_FREEZE_R", "Stop / Freeze-R", true),
       ACKNOWLEDGE_R = new PDU_TYPE(55, "ACKNOWLEDGE_R", "Acknowledge-R", true),
       ACTION_REQUEST_R = new PDU_TYPE(56, "ACTION_REQUEST_R", "Action Request-R", true),
       ACTION_RESPONSE_R = new PDU_TYPE(57, "ACTION_RESPONSE_R", "Action Response-R", true),
       DATA_QUERY_R = new PDU_TYPE(58, "DATA_QUERY_R", "Data Query-R", true),
       SET_DATA_R = new PDU_TYPE(59, "SET_DATA_R", "Set Data-R", true),
       DATA_R = new PDU_TYPE(60, "DATA_R", "Data-R", true),
       EVENT_REPORT_R = new PDU_TYPE(61, "EVENT_REPORT_R", "Event Report-R", true),
       COMMENT_R = new PDU_TYPE(62, "COMMENT_R", "Comment-R", true),
       RECORD_R = new PDU_TYPE(63, "RECORD_R", "Record-R", true),
       SET_RECORD_R = new PDU_TYPE(64, "SET_RECORD_R", "Set Record-R", true),
       RECORD_QUERY_R = new PDU_TYPE(65, "RECORD_QUERY_R", "Record Query-R", true),
       COLLISION_ELASTIC = new PDU_TYPE(66, "COLLISION_ELASTIC", "Collision-Elastic", true),
       ENTITY_STATE_UPDATE = new PDU_TYPE(67, "ENTITY_STATE_UPDATE", "Entity State Update", true),
       DE_FIRE = new PDU_TYPE(68, "DE_FIRE", "Directed Energy Fire", true),
       ENTITY_DAMAGE_STATUS = new PDU_TYPE(69, "ENTITY_DAMAGE_STATUS", "Entity Damage Status", true),
       INFO_OPS_ACTION = new PDU_TYPE(70, "INFO_OPS_ACTION", "Information Operations Action", true),
       INFO_OPS_REPORT = new PDU_TYPE(71, "INFO_OPS_REPORT", "Information Operations Report", true),
       ATTRIBUTE = new PDU_TYPE(72, "ATTRIBUTE", "Attribute", true),
       APPLICATION_CTRL = new PDU_TYPE(200, "APPLICATION_CTRL", "Application control", true);

    private PDU_TYPE(int value, String name, String description, boolean known) {

        super(value, name, description, known);

        cache(this, PDU_TYPE.class);
    }

    /** @see Value#values(Class) */
    public static List<PDU_TYPE> values() {

        return values(PDU_TYPE.class);
    }

    /** @see Value#values(Class, boolean) */
    public static List<PDU_TYPE> values(boolean known) {

        return values(PDU_TYPE.class, known);
    }

    /** @see Value#get(int, Class) */
    public static PDU_TYPE get(int value) {

        return get(value, PDU_TYPE.class);
    }
}

