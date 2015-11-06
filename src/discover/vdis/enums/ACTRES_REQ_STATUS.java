package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum ACTRES_REQ_STATUS implements EnumInterface {

    ACTRES_REQ_OTHER(0, "Other"),
    ACTRES_REQ_PENDING(1, "Pending"),
    ACTRES_REQ_EXECUTING(2, "Executing"),
    ACTRES_REQ_PARTIALLY_COMPLETE(3, "Partially Complete"),
    ACTRES_REQ_COMPLETE(4, "Complete"),
    ACTRES_REQ_REQUEST_REJECTED(5, "Request Rejected"),
    ACTRES_REQ_RETRANSMIT_REQUEST_NOW(6, "Retransmit Request Now"),
    ACTRES_REQ_RETRANSMIT_REQUEST_LATER(7, "Retransmit Request Later"),
    ACTRES_REQ_INVALREQ_TIME_PARAMETERS(8, "Invalid Time Parameters"),
    ACTRES_REQ_SIM_TIME_EXCEEDED(9, "Simulation Time Exceeded"),
    ACTRES_REQ_REQUEST_DONE(10, "Request Done"),
    ACTRES_REQ_TACCSF_LOS_REPLY_TYPE_1(100, "TACCSF LOS Reply-Type 1"),
    ACTRES_REQ_TACCSF_LOS_REPLY_TYPE_2(101, "TACCSF LOS Reply-Type 2"),
    ACTRES_REQ_JOIN_EXERCISE_REQ_REJECTED(201, "Join Exercise Request Rejected");

    private final int value;
    private final String description;

    private ACTRES_REQ_STATUS(int value, String description) {

        this.value = value;
        this.description = description;
    }

    @Override
    public int getValue() {

        return value;
    }

    @Override
    public String getDescription() {

        return description;
    }
}

