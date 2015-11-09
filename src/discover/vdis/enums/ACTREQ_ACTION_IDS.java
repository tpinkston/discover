package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum ACTREQ_ACTION_IDS implements EnumInterface {

    ACTREQ_ID_OTHER(0, "Other"),
    ACTREQ_ID_LOCAL_STORAGE_REQ_INFO(1, "Local Storage Requested Information"),
    ACTREQ_ID_INFORM_EVENT_RAN_OUT_OF_AMMO(2, "Inform Event Ran Out of Ammunition"),
    ACTREQ_ID_INFORM_EVENT_KILLED_IN_ACTION(3, "Inform Event Killed In Action"),
    ACTREQ_ID_INFORM_EVENT_DAMAGE(4, "Inform Event Damage"),
    ACTREQ_ID_INFORM_EVENT_MOBILITY_DISABLED(5, "Inform Event Mobility Disabled"),
    ACTREQ_ID_INFORM_EVENT_FIRE_DISABLED(6, "Inform Event Fire Disabled"),
    ACTREQ_ID_INFORM_EVENT_RAN_OUT_OF_FUEL(7, "Inform Event Ran Out of Fuel"),
    ACTREQ_ID_RECALL_CHECKPOINT_DATA(8, "Recall Checkpoint Data"),
    ACTREQ_ID_RECALL_INITIAL_PARMS(9, "Recall Initial Parameters"),
    ACTREQ_ID_INIT_TETHER_LEAD(10, "Initiate Tether-Lead"),
    ACTREQ_ID_INIT_TETHER_FOLLOW(11, "Initiate Tether-Follow"),
    ACTREQ_ID_UNTETHER(12, "Untether"),
    ACTREQ_ID_INIT_SERVICE_STATION_RESUPPLY(13, "Initiate Service Station Resupply"),
    ACTREQ_ID_INIT_TAILGATE_RESUPPLY(14, "Initiate Tailgate Resupply"),
    ACTREQ_ID_INIT_HITCH_LEAD(15, "Initiate Hitch Lead"),
    ACTREQ_ID_INIT_HITCH_FOLLOW(16, "Initiate Hitch Follow"),
    ACTREQ_ID_UNHITCH(17, "Unhitch"),
    ACTREQ_ID_MOUNT(18, "Mount"),
    ACTREQ_ID_DISMOUNT(19, "Dismount"),
    ACTREQ_ID_START_DRC(20, "Start Daily Readiness Check"),
    ACTREQ_ID_STOP_DRC(21, "Stop Daily Readiness Check"),
    ACTREQ_ID_DATA_QUERY(22, "Data Query"),
    ACTREQ_ID_STATUS_REQ(23, "Status Request"),
    ACTREQ_ID_SEND_OBJECT_STATE_DATA(24, "Send Object State Data"),
    ACTREQ_ID_RECONSTITUTE(25, "Reconstitute"),
    ACTREQ_ID_LOCK_SITE_CONFIG(26, "Lock Site Configuration"),
    ACTREQ_ID_UNLOCK_SITE_CONFIG(27, "Unlock Site Configuration"),
    ACTREQ_ID_UPDATE_SITE_CONFIG(28, "Update Site Configuration"),
    ACTREQ_ID_QUERY_SITE_CONFIG(29, "Query Site Configuration"),
    ACTREQ_ID_TETHERING_INFORMATION(30, "Tethering Information"),
    ACTREQ_ID_MOUNT_INTENT(31, "Mount Intent"),
    ACTREQ_ID_ACCEPT_SUBSCRIPTION(33, "Accept Subscription"),
    ACTREQ_ID_UNSUBSCRIBE(34, "Unsubscribe"),
    ACTREQ_ID_TELEPORT_ENTITY(35, "Teleport Entity"),
    ACTREQ_ID_CHANGE_AGGREGATE_STATE(36, "Change Aggregate State"),
    ACTREQ_ID_REQ_START_PDU(37, "Request Start PDU"),
    ACTREQ_ID_WAKEUP_GET_READY_FOR_INIT(38, "Wakeup Get Ready For Initialization"),
    ACTREQ_ID_INIT_INTERNAL_PARMS(39, "Initialize Internal Parameters"),
    ACTREQ_ID_SEND_PLAN_DATA(40, "Send Plan Data"),
    ACTREQ_ID_SYNC_INTERNAL_CLOCKS(41, "Synchronize Internal Clocks"),
    ACTREQ_ID_RUN(42, "Run"),
    ACTREQ_ID_SAVE_INTERNAL_PARMS(43, "Save Internal Parameters"),
    ACTREQ_ID_SIMULATE_MALFUNCTION(44, "Simulate Malfunction"),
    ACTREQ_ID_JOIN_EXERCISE(45, "Join Exercise"),
    ACTREQ_ID_RESIGN_EXERCISE(46, "Resign Exercise"),
    ACTREQ_ID_TIME_ADVANCE(47, "Time Advance"),
    ACTREQ_ID_COMMAND_FROM_SIMULATOR(48, "Command from Simulator"),
    ACTREQ_ID_TACCSF_LOS_REQ_TYPE_1(100, "TACCSF LOS Request-Type 1"),
    ACTREQ_ID_TACCSF_LOS_REQ_TYPE_2(101, "TACCSF LOS Request-Type 2"),
    ACTREQ_ID_SLING_CAPABILITY_REQ(4300, "Sling Load Capability Request"),
    ACTREQ_ID_SLING_ATTACH_REQ(4301, "Sling Load Attach Request"),
    ACTREQ_ID_SLING_RELEASE_REQ(4302, "Sling Load Release Request");

    private final int value;
    private final String description;

    private ACTREQ_ACTION_IDS(int value, String description) {

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

        return Enumerations.getUnknownValue(value, ACTREQ_ACTION_IDS.class);
    }
}

