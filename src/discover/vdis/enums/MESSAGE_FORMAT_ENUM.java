package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum MESSAGE_FORMAT_ENUM implements EnumInterface {

    MESSAGE_FORMAT_47001C_LINK_16(0, "Link 16 (J-series) message"),
    MESSAGE_FORMAT_47001C_BINARY(1, "Binary File"),
    MESSAGE_FORMAT_47001C_VMF(2, "Variable Message Format (VMF) (K-series) message"),
    MESSAGE_FORMAT_47001C_NITFS(3, "National Imagery Transmission Format System (NITFS)"),
    MESSAGE_FORMAT_47001C_FWD_MSG(4, "Forwarded Message (FWD MSG)"),
    MESSAGE_FORMAT_47001C_USMTF(5, "United States Message Text Format (USMTF)"),
    MESSAGE_FORMAT_47001C_DOI_103(6, "DOI-103"),
    MESSAGE_FORMAT_47001C_XML_MTF(7, "eXtensible Markup Language (XML) - Message Text Format (MTF)"),
    MESSAGE_FORMAT_47001C_XML_VMF(8, "eXtensible Markup Language (XML) - Variable Message Format (VMF)"),
    MESSAGE_FORMAT_AFAPD(16, "AFAPD");

    private final int value;
    private final String description;

    private MESSAGE_FORMAT_ENUM(int value, String description) {

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

        return Enumerations.getUnknownValue(value, MESSAGE_FORMAT_ENUM.class);
    }
}

