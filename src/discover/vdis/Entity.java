/**
 * @author Tony Pinkston
 */
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
import discover.vdis.enums.VDIS;
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
    private static final float KNOTS_CONVERSION =  1.94384449f;

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

        this.transmitters = new TreeMap<Integer, Transmitter>();
        this.emitters = new TreeMap<Integer, EmitterSystemData>();
        this.associations = new ArrayList<AbstractVPRecord>();
        this.articulations = new ArrayList<AbstractVPRecord>();
        this.shotsFired = new ArrayList<PDU>();
        this.shotsReceived = new ArrayList<PDU>();
        this.lastUpdate = new Date();
        this.actionRequests = new int[] { 0, 0 };
        this.actionResponses = new int[] { 0, 0 };
    }

    public EntityState getState() {

        return (EntityState)this.state.getPDU();
    }

    public String getId() {

        if (this.state == null) {

            return UNKNOWN;
        }
        else {

            return this.getState().getEntityId().toString();
        }
    }

    public EntityId getEntityId() {

        if (this.state == null) {

            return null;
        }
        else {

            return this.getState().getEntityId();
        }
    }

    public String getType() {

        if (this.state == null) {

            return UNKNOWN;
        }
        else {

            return this.getState().getEntityType().toString();
        }
    }

    public EntityType getEntityType() {

        if (this.state == null) {

            return null;
        }
        else {

            return this.getState().getEntityType();
        }
    }

    public String getMarking() {

        if (this.state == null) {

            return UNKNOWN;
        }
        else {

            return this.state.getMarking();
        }
    }

    public String getForce() {

        if (this.state == null) {

            return UNKNOWN;
        }
        else {

            return VDIS.getDescription(
                VDIS.FORCE_ID,
                this.getState().getForceId());
        }
    }

    public String getSource() {

        if (this.state == null) {

            return UNKNOWN;
        }
        else {

            return this.state.getSource();
        }
    }

    public Integer getPort() {

        if (this.state == null) {

            return 0;
        }
        else {

            return this.state.getPort();
        }
    }

    public long getTime() {

        return this.lastUpdate.getTime();
    }

    /**
     * Updates the EntityState PDU reference.
     *
     * @param pdu - {@link PDU}
     *
     * @return True if any value presented in table cell is modified.
     */
    public boolean update(PDU pdu) {

        boolean updated = false;

        pdu.decode(false);

        if (pdu.getType() == VDIS.PDU_TYPE_ENTITY_STATE) {

            if (this.state == null) {

                updated = true;
            }
            else if (!this.state.getMarking().equals(pdu.getMarking())) {

                updated = true;
            }

            this.state = pdu;
            this.lastUpdate.setTime(pdu.getTime());
            this.getState().getAssociations(this.associations);
            this.getState().getArticulations(this.articulations);
        }
        else if (pdu.getType() == VDIS.PDU_TYPE_TRANSMITTER) {

            Transmitter transmitter = (Transmitter)pdu.getPDU();

            if (!this.transmitters.containsKey(transmitter.getRadioId())) {

                this.transmitters.put(transmitter.getRadioId(), transmitter);
                updated = true;
            }
            else {

                Transmitter radio = this.transmitters.get(
                    transmitter.getRadioId());

                if (radio.getTransmitState() !=
                    transmitter.getTransmitState()) {

                    this.transmitters.put(
                        transmitter.getRadioId(),
                        transmitter);

                    updated = true;
                }
            }
        }
        else if (pdu.getType() == VDIS.PDU_TYPE_ACTION_REQUEST) {

            if (this.state != null) {

                ActionRequest request = (ActionRequest)pdu.getPDU();

                if (this.getEntityId().equals(request.getOriginator())) {

                    this.actionRequests[SENT]++;
                    updated = true;
                }
                else if (this.getEntityId().equals(request.getRecipient())) {

                    this.actionRequests[RECEIVED]++;
                    updated = true;
                }
            }
        }
        else if (pdu.getType() == VDIS.PDU_TYPE_ACTION_RESPONSE) {

            if (this.state != null) {

                ActionResponse response = (ActionResponse)pdu.getPDU();

                if (this.getEntityId().equals(response.getOriginator())) {

                    this.actionResponses[SENT]++;
                }
                else if (this.getEntityId().equals(response.getRecipient())) {

                    this.actionResponses[RECEIVED]++;
                }
            }
        }
        else if (pdu.getType() == VDIS.PDU_TYPE_FIRE) {

            if (this.state != null) {

                Fire fire = (Fire)pdu.getPDU();

                if (this.getEntityId().equals(fire.getShooter())) {

                    this.shotsFired.add(pdu);
                }
                else if (this.getEntityId().equals(fire.getTarget())) {

                    this.shotsReceived.add(pdu);
                }
            }
        }
        else if (pdu.getType() == VDIS.PDU_TYPE_DETONATION) {

            if (this.state != null) {

                Detonation detonation = (Detonation)pdu.getPDU();
                List<PDU> list = null;

                if (this.getEntityId().equals(detonation.getShooter())) {

                    list = this.shotsFired;
                }
                else if (this.getEntityId().equals(detonation.getTarget())) {

                    list = this.shotsReceived;
                }

                if (list != null) {

                    for(PDU firePDU : list) {

                        Fire fire = (Fire)firePDU.getPDU();

                        if (fire.getEvent().equals(detonation.getEvent())) {

                            fire.setDetonation(detonation);
                        }
                    }
                }
            }
        }
        else if (pdu.getType() == VDIS.PDU_TYPE_EM_EMISSION) {

            ElectromagneticEmission emission =
                (ElectromagneticEmission)pdu.getPDU();

            for(EmitterSystemData system : emission.getSystems()) {

                system.setTime(pdu.getTime());
                this.emitters.put(system.getNumber(), system);
                updated = true;
            }
        }

        return updated;
    }

    public String getGeneralHTML() {

        HypertextBuffer basic = new HypertextBuffer();
        HypertextBuffer spatial = new HypertextBuffer();
        HypertextBuffer actions = new HypertextBuffer();
        String update = dateFormat.format(this.lastUpdate);
        String data[][] = new String[2][2];

        basic.addAttribute("Marking", this.getMarking());
        basic.addAttribute("Force Id", this.getForce());
        basic.addAttribute("Entity Id", this.getId());
        basic.addLabel("Entity Type");
        basic.addBreak();
        this.getEntityType().toBuffer(basic);

        spatial.addAttribute(
            "Location (GCC)",
            this.getState().getLocation().toStringGCC());
        spatial.addAttribute(
            "Location (GDC)",
            this.getState().getLocation().toStringGDC());
        spatial.addAttribute(
            "Speed",
            this.getSpeedString());
        spatial.addAttribute(
            "Velocity",
            this.getState().getVelocity().toString());
        spatial.addAttribute(
            "Orientation",
            this.getState().getOrientation().toString());

        actions.addLabel("Action Requests");
        actions.addBold(Integer.toString(this.actionRequests[SENT]));
        actions.addText(" sent, ");
        actions.addBold(Integer.toString(this.actionRequests[RECEIVED]));
        actions.addText(" received");
        actions.addBreak();
        actions.addLabel("Action Responses");
        actions.addBold(Integer.toString(this.actionResponses[SENT]));
        actions.addText(" sent, ");
        actions.addBold(Integer.toString(this.actionResponses[RECEIVED]));
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

        if (this.associations.isEmpty()) {

            buffer.addItalic(NONE);
        }
        else {

            final int count = this.associations.size();
            String data[][] = new String[count][1];

            for(int i = 0; i < count; ++i) {

                HypertextBuffer html = new HypertextBuffer();
                AbstractVPRecord record = this.associations.get(i);

                record.toBuffer(html);

                data[i][0] = html.toString();
            }

            buffer.addTable(1, null, count, 1, data);
        }

        return buffer.toString();
    }

    public String getArticulationsHTML() {

        HypertextBuffer buffer = new HypertextBuffer();

        if (this.articulations.isEmpty()) {

            buffer.addItalic(NONE);
        }
        else {

            final int count = this.articulations.size();
            String data[][] = new String[count][1];

            for(int i = 0; i < count; ++i) {

                HypertextBuffer html = new HypertextBuffer();
                AbstractVPRecord record = this.articulations.get(i);

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
        EntityState state = this.getState();

        AbstractVPRecord appearance = null;

        for(AbstractVPRecord record : this.getState().getRecords()) {

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

        if (this.transmitters.isEmpty()) {

            buffer.addItalic(NONE);
        }
        else {

            int rows = (this.transmitters.size() + 1);
            String data[][] = new String[rows][5];

            data[0][0] = "Id";
            data[0][1] = "Type";
            data[0][2] = "State";
            data[0][3] = "Source";
            data[0][4] = "Antenna Pattern";

            int i = 1;

            for(Transmitter transmitter : this.transmitters.values()) {

                data[i][0] = Integer.toString(transmitter.getRadioId());
                data[i][1] = transmitter.getRadioType().description;
                data[i][2] = VDIS.getDescription(
                    VDIS.TRANSMIT_STATE,
                    transmitter.getTransmitState());
                data[i][3] = VDIS.getDescription(
                    VDIS.INPUT_SOURCE,
                    transmitter.getInputSource());
                data[i][4] = VDIS.getDescription(
                    VDIS.ANTENNA_PATTERN_TYPE,
                    transmitter.getAntennaPattern());

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

        for(PDU pdu : this.shotsFired){

            Fire fire = (Fire)pdu.getPDU();
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

        for(PDU pdu : this.shotsReceived){

            Fire fire = (Fire)pdu.getPDU();
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

        int rows = this.shotsFired.size();

        if (rows < this.shotsReceived.size()) {

            rows = this.shotsReceived.size();
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

            for(int i = 0; i < rows; ++i) {

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

        if (this.emitters.isEmpty()) {

            buffer.addItalic(NONE);
        }
        else {

            int columns = this.emitters.size();
            String data[][] = new String[1][columns];

            int i = 0;

            for(EmitterSystemData system : this.emitters.values()) {

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
        int domain = this.state.getEntityDomain();
        float speed = this.getState().getVelocity().getLength();

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
