package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum PDU_TYPE implements EnumInterface {

    PDU_TYPE_OTHER(0, "Other"),
    PDU_TYPE_ENTITY_STATE(1, "Entity State"),
    PDU_TYPE_FIRE(2, "Fire"),
    PDU_TYPE_DETONATION(3, "Detonation"),
    PDU_TYPE_COLLISION(4, "Collision"),
    PDU_TYPE_SERVICE_REQ(5, "Service Request"),
    PDU_TYPE_RESUPPLY_OFFER(6, "Resupply Offer"),
    PDU_TYPE_RESUPPLY_RECEIVED(7, "Resupply Received"),
    PDU_TYPE_RESUPPLY_CANCEL(8, "Resupply Cancel"),
    PDU_TYPE_REPAIR_COMPLETE(9, "Repair Complete"),
    PDU_TYPE_REPAIR_RESPONSE(10, "Repair Response"),
    PDU_TYPE_CREATE_ENTITY(11, "Create Entity"),
    PDU_TYPE_REMOVE_ENTITY(12, "Remove Entity"),
    PDU_TYPE_START_RESUME(13, "Start / Resume"),
    PDU_TYPE_STOP_FREEZE(14, "Stop / Freeze"),
    PDU_TYPE_ACKNOWLEDGE(15, "Acknowledge"),
    PDU_TYPE_ACTION_REQUEST(16, "Action Request"),
    PDU_TYPE_ACTION_RESPONSE(17, "Action Response"),
    PDU_TYPE_DATA_QUERY(18, "Data Query"),
    PDU_TYPE_SET_DATA(19, "Set Data"),
    PDU_TYPE_DATA(20, "Data"),
    PDU_TYPE_EVENT_REPORT(21, "Event Report"),
    PDU_TYPE_COMMENT(22, "Comment"),
    PDU_TYPE_EM_EMISSION(23, "Electromagnetic Emission"),
    PDU_TYPE_DESIGNATOR(24, "Designator"),
    PDU_TYPE_TRANSMITTER(25, "Transmitter"),
    PDU_TYPE_SIGNAL(26, "Signal"),
    PDU_TYPE_RECEIVER(27, "Receiver"),
    PDU_TYPE_IFF(28, "Identification Friend or Foe"),
    PDU_TYPE_UNDERWATER_ACOUSTIC(29, "Underwater Acoustic"),
    PDU_TYPE_SEES(30, "Supplemental Emission / Entity State"),
    PDU_TYPE_INTERCOM_SIGNAL(31, "Intercom Signal"),
    PDU_TYPE_INTERCOM_CONTROL(32, "Intercom Control"),
    PDU_TYPE_AGGREGATE_STATE(33, "Aggregate State"),
    PDU_TYPE_ISGROUPOF(34, "Is Group Of"),
    PDU_TYPE_TRANSFER_OWNERSHIP(35, "Transfer Ownership"),
    PDU_TYPE_ISPARTOF(36, "Is Part Of"),
    PDU_TYPE_MINEFIELD_STATE(37, "Minefield State"),
    PDU_TYPE_MINEFIELD_QUERY(38, "Minefield Query"),
    PDU_TYPE_MINEFIELD_DATA(39, "Minefield Data"),
    PDU_TYPE_MINEFIELD_RESPONSE_NAK(40, "Minefield Response NAK"),
    PDU_TYPE_ENVIRONMENTAL_PROCESS(41, "Environmental Process"),
    PDU_TYPE_GRIDDED_DATA(42, "Gridded Data"),
    PDU_TYPE_POINT_OBJECT_STATE(43, "Point Object State"),
    PDU_TYPE_LINEAR_OBJECT_STATE(44, "Linear Object State"),
    PDU_TYPE_AREAL_OBJECT_STATE(45, "Areal Object State"),
    PDU_TYPE_TSPI(46, "TSPI"),
    PDU_TYPE_APPEARANCE(47, "Appearance"),
    PDU_TYPE_ARTICULATED_PARTS(48, "Articulated Parts"),
    PDU_TYPE_LE_FIRE(49, "Live Entity Fire"),
    PDU_TYPE_LE_DETONATION(50, "Live Entity Detonation"),
    PDU_TYPE_CREATE_ENTITY_R(51, "Create Entity-R"),
    PDU_TYPE_REMOVE_ENTITY_R(52, "Remove Entity-R"),
    PDU_TYPE_START_RESUME_R(53, "Start / Resume-R"),
    PDU_TYPE_STOP_FREEZE_R(54, "Stop / Freeze-R"),
    PDU_TYPE_ACKNOWLEDGE_R(55, "Acknowledge-R"),
    PDU_TYPE_ACTION_REQUEST_R(56, "Action Request-R"),
    PDU_TYPE_ACTION_RESPONSE_R(57, "Action Response-R"),
    PDU_TYPE_DATA_QUERY_R(58, "Data Query-R"),
    PDU_TYPE_SET_DATA_R(59, "Set Data-R"),
    PDU_TYPE_DATA_R(60, "Data-R"),
    PDU_TYPE_EVENT_REPORT_R(61, "Event Report-R"),
    PDU_TYPE_COMMENT_R(62, "Comment-R"),
    PDU_TYPE_RECORD_R(63, "Record-R"),
    PDU_TYPE_SET_RECORD_R(64, "Set Record-R"),
    PDU_TYPE_RECORD_QUERY_R(65, "Record Query-R"),
    PDU_TYPE_COLLISION_ELASTIC(66, "Collision-Elastic"),
    PDU_TYPE_ENTITY_STATE_UPDATE(67, "Entity State Update"),
    PDU_TYPE_DE_FIRE(68, "Directed Energy Fire"),
    PDU_TYPE_ENTITY_DAMAGE_STATUS(69, "Entity Damage Status"),
    PDU_TYPE_INFO_OPS_ACTION(70, "Information Operations Action"),
    PDU_TYPE_INFO_OPS_REPORT(71, "Information Operations Report"),
    PDU_TYPE_ATTRIBUTE(72, "Attribute"),
    PDU_TYPE_APPLICATION_CTRL(200, "Application control");

    private final int value;
    private final String description;

    private PDU_TYPE(int value, String description) {

        this.value = value;
        this.description = description;
    }

    @Override
    public int getValue() {

        return value;
    }

    @Override
    public String getName() {

        return name();
    }

    @Override
    public String getDescription() {

        return description;
    }

    public static EnumInterface getValue(int value) {

        for(EnumInterface element : values()) {

            if (element.getValue() == value) {

                return element;
            }
        }

        return Enumerations.getUnknownValue(value, PDU_TYPE.class);
    }
}

