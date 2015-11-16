package discover.vdis;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import discover.common.buffer.HypertextBuffer;
import discover.vdis.common.BurstDescriptor;
import discover.vdis.common.EmitterSystemData;
import discover.vdis.common.EntityId;
import discover.vdis.enums.ANTENNA_PATTERN_TYPE;
import discover.vdis.enums.FORCE_ID;
import discover.vdis.enums.INPUT_SOURCE;
import discover.vdis.enums.PDU_TYPE;
import discover.vdis.enums.TRANSMIT_STATE;
import discover.vdis.pdu.ActionRequest;
import discover.vdis.pdu.ActionResponse;
import discover.vdis.pdu.Detonation;
import discover.vdis.pdu.ElectromagneticEmission;
import discover.vdis.pdu.EntityState;
import discover.vdis.pdu.Fire;
import discover.vdis.pdu.Transmitter;
import discover.vdis.types.EntityType;
import discover.vdis.vprecords.AbstractVPRecord;
import discover.vdis.vprecords.ExtendedAppearanceVPR;

/**
 * @author Tony Pinkston
 */
public class Entity {

    private static final String NOT_AVAILABLE = "(Not Available)";
    private static final String NONE = "None";
    private static final String UNKNOWN = "Unknown";
    private static final String PENDING = "Pending";
    private static final String SHOTS_FIRED = "Shots Fired";
    private static final String SHOTS_RECEIVED = "Shots Received";
    private static final String RESULT = "Result (Detonation)";

    /** Factor for converting m/s to km/h */
    private static final float KMH_CONVERSION = 3.6f;

    /** Factor for converting m/s to knots */
    private static final float KNOTS_CONVERSION = 1.94384449f;

    private static final int SENT = 0;
    private static final int RECEIVED = 0;

    private static final DateFormat dateFormat;
    private static final NumberFormat speedFormatter;
    private static final NumberFormat floatFormatter;

    static {

        dateFormat = DateFormat.getDateTimeInstance();

        speedFormatter = NumberFormat.getInstance();
        speedFormatter.setMinimumFractionDigits(1);
        speedFormatter.setMaximumFractionDigits(1);

        floatFormatter = NumberFormat.getInstance();
        floatFormatter.setMinimumFractionDigits(1);
        floatFormatter.setMaximumFractionDigits(5);
    }

    /** Last entity state PDU to be received: */
    private PDU state = null;

    /** Transmitters on this entity: */
    private final TreeMap<Integer, Transmitter> transmitters;

    /** Emitters on this entity: */
    private final TreeMap<Integer, EmitterSystemData> emitters;

    /** List of most recent entity associations. */
    private final ArrayList<AbstractVPRecord> associations;

    /** List of most recent entity articulations. */
    private final ArrayList<AbstractVPRecord> articulations;

    /** Weapons/munitions fired by this entity. */
    private final ArrayList<PDU> shotsFired;

    /** Weapons/munitions fired at this entity. */
    private final ArrayList<PDU> shotsReceived;

    /** Time of last entity state update (PDU time): */
    private final Date lastUpdate;

    /** Count of sent/received action requests: */
    private final int actionRequests[];

    /** Count of sent/received action responses: */
    private final int actionResponses[];

    public Entity() {

        transmitters = new TreeMap<Integer, Transmitter>();
        emitters = new TreeMap<Integer, EmitterSystemData>();
        associations = new ArrayList<AbstractVPRecord>();
        articulations = new ArrayList<AbstractVPRecord>();
        shotsFired = new ArrayList<PDU>();
        shotsReceived = new ArrayList<PDU>();
        lastUpdate = new Date();
        actionRequests = new int[] { 0, 0 };
        actionResponses = new int[] { 0, 0 };
    }

    public EntityState getState() {

        return (EntityState) state.getPDU();
    }

    public String getId() {

        if (state == null) {

            return UNKNOWN;
        }
        else {

            return getState().getEntityId().toString();
        }
    }

    public EntityId getEntityId() {

        if (state == null) {

            return null;
        }
        else {

            return getState().getEntityId();
        }
    }

    public String getType() {

        if (state == null) {

            return UNKNOWN;
        }
        else {

            return getState().getEntityType().toString();
        }
    }

    public EntityType getEntityType() {

        if (state == null) {

            return null;
        }
        else {

            return getState().getEntityType();
        }
    }

    public String getMarking() {

        if (state == null) {

            return UNKNOWN;
        }
        else {

            return state.getMarking();
        }
    }

    public String getForce() {

        if (getState() == null) {

            return UNKNOWN;
        }
        else {

            return FORCE_ID.getValue(getState().getForceId()).getDescription();
        }
    }

    public String getSource() {

        return (state == null) ? UNKNOWN : state.getSource();
    }

    public Integer getPort() {

        return (state == null) ? 0 : state.getPort();
    }

    public long getTime() {

        return lastUpdate.getTime();
    }

    /**
     * Updates the EntityState PDU reference.
     *
     * @param pdu
     *            - {@link PDU}
     *
     * @return True if any value presented in table cell is modified.
     */
    public boolean update(PDU pdu) {

        boolean updated = false;

        pdu.decode(false);

        if (pdu.getTypeEnum() == PDU_TYPE.PDU_TYPE_ENTITY_STATE) {

            if (state == null) {

                updated = true;
            }
            else if (!state.getMarking().equals(pdu.getMarking())) {

                updated = true;
            }

            state = pdu;
            lastUpdate.setTime(pdu.getTime());

            getState().getAssociations(associations);
            getState().getArticulations(articulations);
        }
        else if (pdu.getTypeEnum() == PDU_TYPE.PDU_TYPE_TRANSMITTER) {

            Transmitter transmitter = (Transmitter)pdu.getPDU();

            if (!transmitters.containsKey(transmitter.getRadioId())) {

                transmitters.put(transmitter.getRadioId(), transmitter);
                updated = true;
            }
            else {

                Transmitter radio = transmitters.get(
                    transmitter.getRadioId());

                if (radio.getTransmitState() != transmitter.getTransmitState()) {

                    transmitters.put(
                        transmitter.getRadioId(),
                        transmitter);

                    updated = true;
                }
            }
        }
        else if (pdu.getTypeEnum() == PDU_TYPE.PDU_TYPE_ACTION_REQUEST) {

            if (state != null) {

                ActionRequest request = (ActionRequest)pdu.getPDU();

                if (getEntityId().equals(request.getOriginator())) {

                    actionRequests[SENT]++;
                    updated = true;
                }
                else if (getEntityId().equals(request.getRecipient())) {

                    actionRequests[RECEIVED]++;
                    updated = true;
                }
            }
        }
        else if (pdu.getTypeEnum() == PDU_TYPE.PDU_TYPE_ACTION_RESPONSE) {

            if (state != null) {

                ActionResponse response = (ActionResponse)pdu.getPDU();

                if (getEntityId().equals(response.getOriginator())) {

                    actionResponses[SENT]++;
                }
                else if (getEntityId().equals(response.getRecipient())) {

                    actionResponses[RECEIVED]++;
                }
            }
        }
        else if (pdu.getTypeEnum() == PDU_TYPE.PDU_TYPE_FIRE) {

            if (state != null) {

                Fire fire = (Fire)pdu.getPDU();

                if (getEntityId().equals(fire.getShooter())) {

                    shotsFired.add(pdu);
                }
                else if (getEntityId().equals(fire.getTarget())) {

                    shotsReceived.add(pdu);
                }
            }
        }
        else if (pdu.getTypeEnum() == PDU_TYPE.PDU_TYPE_DETONATION) {

            if (state != null) {

                Detonation detonation = (Detonation)pdu.getPDU();
                List<PDU> list = null;

                if (getEntityId().equals(detonation.getShooter())) {

                    list = shotsFired;
                }
                else if (getEntityId().equals(detonation.getTarget())) {

                    list = shotsReceived;
                }

                if (list != null) {

                    for (PDU firePDU : list) {

                        Fire fire = (Fire)firePDU.getPDU();

                        if (fire.getEvent().equals(detonation.getEvent())) {

                            fire.setDetonation(detonation);
                        }
                    }
                }
            }
        }
        else if (pdu.getTypeEnum() == PDU_TYPE.PDU_TYPE_EM_EMISSION) {

            ElectromagneticEmission emission = (ElectromagneticEmission)pdu.getPDU();

            for (EmitterSystemData system : emission.getSystems()) {

                system.setTime(pdu.getTime());
                emitters.put(system.getNumber(), system);
                updated = true;
            }
        }

        return updated;
    }

    public String getGeneralHTML() {

        HypertextBuffer basic = new HypertextBuffer();
        HypertextBuffer spatial = new HypertextBuffer();
        HypertextBuffer actions = new HypertextBuffer();
        String update = dateFormat.format(lastUpdate);
        String data[][] = new String[2][2];

        basic.addAttribute("Marking", getMarking());
        basic.addAttribute("Force Id", getForce());
        basic.addAttribute("Entity Id", getId());
        basic.addLabel("Entity Type");
        basic.addBreak();
        getEntityType().toBuffer(basic);

        spatial.addAttribute(
            "Location (GCC)",
            getState().getLocation().toStringGCC());
        spatial.addAttribute(
            "Location (GDC)",
            getState().getLocation().toStringGDC());
        spatial.addAttribute(
            "Speed",
            getSpeedString());
        spatial.addAttribute(
            "Velocity",
            getState().getVelocity().toString());
        spatial.addAttribute(
            "Orientation",
            getState().getOrientation().toString());

        actions.addLabel("Action Requests");
        actions.addBold(Integer.toString(actionRequests[SENT]));
        actions.addText(" sent, ");
        actions.addBold(Integer.toString(actionRequests[RECEIVED]));
        actions.addText(" received");
        actions.addBreak();
        actions.addLabel("Action Responses");
        actions.addBold(Integer.toString(actionResponses[SENT]));
        actions.addText(" sent, ");
        actions.addBold(Integer.toString(actionResponses[RECEIVED]));
        actions.addText(" received");

        data[0][0] = basic.toString();
        data[1][0] = spatial.toString();
        data[0][1] = actions.toString();
        data[1][1] = "";

        HypertextBuffer buffer = new HypertextBuffer();

        buffer.addTable(1, null, 2, 2, data);
        buffer.addBreak();
        buffer.addAttribute("Last Updated: ", update);

        return buffer.toString();
    }

    public String getAssociationsHTML() {

        HypertextBuffer buffer = new HypertextBuffer();

        if (associations.isEmpty()) {

            buffer.addItalic(NONE);
        }
        else {

            final int count = associations.size();
            String data[][] = new String[count][1];

            for (int i = 0; i < count; ++i) {

                HypertextBuffer html = new HypertextBuffer();
                AbstractVPRecord record = associations.get(i);

                record.toBuffer(html);

                data[i][0] = html.toString();
            }

            buffer.addTable(1, null, count, 1, data);
        }

        return buffer.toString();
    }

    public String getArticulationsHTML() {

        HypertextBuffer buffer = new HypertextBuffer();

        if (articulations.isEmpty()) {

            buffer.addItalic(NONE);
        }
        else {

            final int count = articulations.size();
            String data[][] = new String[count][1];

            for (int i = 0; i < count; ++i) {

                HypertextBuffer html = new HypertextBuffer();
                AbstractVPRecord record = articulations.get(i);

                record.toBuffer(html);

                data[i][0] = html.toString();
            }

            buffer.addTable(1, null, count, 1, data);
        }

        return buffer.toString();
    }

    public String getAppearanceHTML() {

        HypertextBuffer buffer = new HypertextBuffer();
        HypertextBuffer basic = new HypertextBuffer();
        HypertextBuffer extended = new HypertextBuffer();
        String data[][] = new String[1][2];
        EntityState state = getState();

        AbstractVPRecord appearance = null;

        for (AbstractVPRecord record : getState().getRecords()) {

            if (record instanceof ExtendedAppearanceVPR) {

                appearance = record;
            }
        }

        if (state.getAppearance() == null) {

            basic.addTitle("BASIC APPEARANCE");
            basic.addItalic(NOT_AVAILABLE);
        }
        else {

            basic.addTitle(state.getAppearance().getName().toUpperCase());
            basic.addBuffer(state.getAppearance());
        }

        if (appearance == null) {

            extended.addTitle("EXTENDED APPEARANCE");
            extended.addItalic(NOT_AVAILABLE);
        }
        else {

            extended.addBuffer(appearance);
        }

        data[0][0] = basic.toString();
        data[0][1] = extended.toString();

        buffer.addTable(1, null, 1, 2, data);

        return buffer.toString();
    }

    public String getTransmittersHTML() {

        HypertextBuffer buffer = new HypertextBuffer();

        if (transmitters.isEmpty()) {

            buffer.addItalic(NONE);
        }
        else {

            int rows = (transmitters.size() + 1);
            String data[][] = new String[rows][5];

            data[0][0] = "Id";
            data[0][1] = "Type";
            data[0][2] = "State";
            data[0][3] = "Source";
            data[0][4] = "Antenna Pattern";

            int i = 1;

            for (Transmitter transmitter : transmitters.values()) {

                data[i][0] = Integer.toString(transmitter.getRadioId());
                data[i][1] = transmitter.getRadioType().description;
                data[i][2] = TRANSMIT_STATE.getValue(transmitter.getTransmitState()).getDescription();
                data[i][3] = INPUT_SOURCE.getValue(transmitter.getInputSource()).getDescription();
                data[i][4] = ANTENNA_PATTERN_TYPE.getValue(transmitter.getAntennaPattern()).getDescription();

                ++i;
            }

            buffer.addTable(1, null, rows, 5, data);
        }

        return buffer.toString();
    }

    public String getWarfareHTML() {

        List<String> fired = new ArrayList<String>();
        List<String> received = new ArrayList<String>();
        Date date = new Date();

        for (PDU pdu : shotsFired) {

            Fire fire = (Fire) pdu.getPDU();
            HypertextBuffer buffer = new HypertextBuffer();
            BurstDescriptor burst = fire.getBurst();

            date.setTime(pdu.getTime());

            buffer.addAttribute("Event", fire.getEvent().toString());
            buffer.addAttribute("Target", fire.getTarget().toString());
            buffer.addAttribute("Location", fire.getLocation().toString());
            buffer.addAttribute("Munition", fire.getMunition().toString());
            buffer.addItalic(burst.getMunition().toString());
            buffer.addBreak();
            buffer.addItalic(burst.getMunition().description);
            buffer.addBreak();
            buffer.addAttribute("Warhead", burst.getWarhead());
            buffer.addAttribute("Quantity", burst.getQuantity());
            buffer.addAttribute("Time", dateFormat.format(date));

            if (fire.getDetonation() == null) {

                buffer.addAttribute(RESULT, PENDING);
            }
            else {

                buffer.addAttribute(
                    RESULT,
                    fire.getDetonation().getDetonationResult());
            }

            fired.add(buffer.toString());
        }

        for (PDU pdu : shotsReceived) {

            Fire fire = (Fire) pdu.getPDU();
            HypertextBuffer buffer = new HypertextBuffer();
            BurstDescriptor burst = fire.getBurst();

            date.setTime(pdu.getTime());

            buffer.addAttribute("Event", fire.getEvent().toString());
            buffer.addAttribute("Shooter", fire.getShooter().toString());
            buffer.addAttribute("Location", fire.getLocation().toString());
            buffer.addAttribute("Munition", fire.getMunition().toString());
            buffer.addItalic(burst.getMunition().toString());
            buffer.addBreak();
            buffer.addItalic(burst.getMunition().description);
            buffer.addBreak();
            buffer.addAttribute("Warhead", burst.getWarhead());
            buffer.addAttribute("Quantity", burst.getQuantity());
            buffer.addAttribute("Time", dateFormat.format(date));

            if (fire.getDetonation() == null) {

                buffer.addAttribute(RESULT, PENDING);
            }
            else {

                buffer.addAttribute(
                    RESULT,
                    fire.getDetonation().getDetonationResult());
            }

            received.add(buffer.toString());
        }

        int rows = shotsFired.size();

        if (rows < shotsReceived.size()) {

            rows = shotsReceived.size();
        }

        HypertextBuffer buffer = new HypertextBuffer();

        if (rows == 0) {

            String data[][] = new String[2][2];
            data[0][0] = SHOTS_FIRED;
            data[0][1] = SHOTS_RECEIVED;
            data[1][0] = NONE;
            data[1][1] = NONE;

            buffer.addTable(1, null, 2, 2, data);
        }
        else {

            String data[][] = new String[rows + 1][2];
            data[0][0] = SHOTS_FIRED;
            data[0][1] = SHOTS_RECEIVED;

            for (int i = 0; i < rows; ++i) {

                if (i < fired.size()) {

                    data[i + 1][0] = fired.get(i);
                }
                else {

                    data[i + 1][0] = "";
                }

                if (i < received.size()) {

                    data[i + 1][1] = received.get(i);
                }
                else {

                    data[i + 1][1] = "";
                }
            }

            buffer.addTable(1, null, (rows + 1), 2, data);
        }

        return buffer.toString();
    }

    public String getEmissionsHTML() {

        HypertextBuffer buffer = new HypertextBuffer();

        if (emitters.isEmpty()) {

            buffer.addItalic(NONE);
        }
        else {

            int columns = emitters.size();
            String data[][] = new String[1][columns];

            int i = 0;

            for (EmitterSystemData system : emitters.values()) {

                HypertextBuffer hypertext = new HypertextBuffer();

                hypertext.addBuffer(system);
                hypertext.addBreak();

                hypertext.addAttribute(
                    "Last Update",
                    dateFormat.format(system.getTime()));

                data[0][i] = hypertext.toString();

                ++i;
            }

            buffer.addTable(1, null, 1, columns, data);
        }

        return buffer.toString();
    }

    private String getSpeedString() {

        String label;
        int domain = state.getEntityDomain();
        float speed = getState().getVelocity().getLength();

        // Is is 'AIR' domain?
        if (domain == 2) {

            speed = (speed * KNOTS_CONVERSION);
            label = " knots";
        }
        else {

            // Assume it's land.
            speed = (speed * KMH_CONVERSION);
            label = " km/h";
        }

        return (speedFormatter.format(speed) + label);
    }
}
