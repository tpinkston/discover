package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum UA_SYS_NAME implements EnumInterface {

    UA_SYS_NAME_OTHER(0, "Other"),
    UA_SYS_NAME_AN_BQQ_5(1, "AN/BQQ-5"),
    UA_SYS_NAME_AN_SSQ_62(2, "AN/SSQ-62"),
    UA_SYS_NAME_AN_SQS_23(3, "AN/SQS-23"),
    UA_SYS_NAME_AN_SQS_26(4, "AN/SQS-26"),
    UA_SYS_NAME_AN_SQS_53(5, "AN/SQS-53"),
    UA_SYS_NAME_ALFS(6, "ALFS"),
    UA_SYS_NAME_LFA(7, "LFA"),
    UA_SYS_NAME_AN_AQS_901(8, "AN/AQS-901"),
    UA_SYS_NAME_AN_AQS_902(9, "AN/AQS-902");

    private final int value;
    private final String description;

    private UA_SYS_NAME(int value, String description) {

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

