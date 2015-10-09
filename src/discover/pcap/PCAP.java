package discover.pcap;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import discover.vdis.PDU;

/**
 * @author Steve Schricker, Tony Pinkston
 */
public class PCAP {

    public static int LINK_ETHERNET = 1;
    public static int ETHERTYPE_IPv4 = 0x0800;
    public static int ETHERTYPE_IPv6 = 0x86dd;
    public static int IP_UDP = 0x11;

    private static final Logger logger = LoggerFactory.getLogger(PCAP.class);

    public static List<PDU> getPDUs(DataInputStream stream, boolean swap)
            throws IOException {

        List<PDU> pdus = new ArrayList<>();

        Header pcapHeader = new Header(stream, swap);

        if (pcapHeader.linkLayerHeaderType == LINK_ETHERNET) {

            logger.info(
                "PCAP file contains non-ethernet capture (link layer type = " +
                pcapHeader.linkLayerHeaderType + ")");
        }
        else try {

            while(true) {

                Record record = new Record(stream, swap);
                EthernetPacket packet = new EthernetPacket(record.payload);

                if (packet.etherType == ETHERTYPE_IPv4) {

                    PDU pdu = getIPv4PDU(
                        packet.userData,
                        record.timestampSeconds,
                        record.timestampMicroseconds);

                    if (pdu != null) {

                        pdus.add(pdu);
                    }
                }
                else if (packet.etherType == ETHERTYPE_IPv6) {

                    PDU pdu = getIPv6PDU(
                        packet.userData,
                        record.timestampSeconds,
                        record.timestampMicroseconds);

                    if (pdu != null) {

                        pdus.add(pdu);
                    }
                }
            }
        }
        catch(EOFException exception) {

            // End of file--nothing more to do.
        }

        return pdus;
    }

    private static PDU getIPv4PDU(
            byte[] bytes,
            int timestampSeconds,
            int timestampMicroseconds) throws IOException {

        IPv4Packet ipv4Packet = new IPv4Packet(bytes);
        PDU pdu = null;

        if (ipv4Packet.protocol == IP_UDP) {

            UDPDatagram datagram = new UDPDatagram(ipv4Packet.data);

            // Convert from pcap time to java time.
            //
            long timestamp =
                ((long)timestampSeconds * 1000) +
                ((long)timestampMicroseconds / 1000);

            DataInputStream pduData = getStream(datagram.data);

            pdu = PDU.createFromPCAP(
                ipv4Packet.sourceAddress,
                datagram.sourcePort,
                timestamp,
                pduData);
        }

        return pdu;
    }

    private static PDU getIPv6PDU(
            byte[] bytes,
            int timestampSeconds,
            int timestampMicroseconds) throws IOException {

        IPv6Packet ipv6Packet = new IPv6Packet(bytes);
        PDU pdu = null;

        if (ipv6Packet.nextHeader == IP_UDP) {

            UDPDatagram datagram = new UDPDatagram(ipv6Packet.data);

            // Convert from pcap time to java time.
            //
            long timestamp =
                ((long)timestampSeconds * 1000) +
                ((long)timestampMicroseconds / 1000);

            DataInputStream pduData = getStream(datagram.data);

            pdu = PDU.createFromPCAP(
                ipv6Packet.sourceAddress,
                datagram.sourcePort,
                timestamp,
                pduData);
        }

        return pdu;
    }

    private static DataInputStream getStream(byte array[]) {

        return new DataInputStream(new ByteArrayInputStream(array));
    }

    static class Header {

        public final int magicNumber;
        public final short majorVersion;
        public final short minorVersion;
        public final int timeZoneOffset;
        public final int timestampAccuracy;
        public final int snapshotLength;
        public final int linkLayerHeaderType;

        public Header(DataInputStream stream, boolean swapBytes)
                throws IOException {

            if (swapBytes) {

                magicNumber = Integer.reverseBytes(stream.readInt());
                majorVersion = Short.reverseBytes(stream.readShort());
                minorVersion = Short.reverseBytes(stream.readShort());
                timeZoneOffset = Integer.reverseBytes(stream.readInt());
                timestampAccuracy = Integer.reverseBytes(stream.readInt());
                snapshotLength = Integer.reverseBytes(stream.readInt());
                linkLayerHeaderType = Integer.reverseBytes(stream.readInt());
            }
            else {

                magicNumber = stream.readInt();
                majorVersion = stream.readShort();
                minorVersion = stream.readShort();
                timeZoneOffset = stream.readInt();
                timestampAccuracy = stream.readInt();
                snapshotLength = stream.readInt();
                linkLayerHeaderType = stream.readInt();
            }
        }
    }

    static class Record {

        public final int timestampSeconds;
        public final int timestampMicroseconds;
        public final int length;
        public final int untruncatedLength;
        public final byte[] payload;

        public Record(DataInputStream stream, boolean swapBytes)
                throws IOException {

            if (swapBytes) {

                timestampSeconds = Integer.reverseBytes(stream.readInt());
                timestampMicroseconds = Integer.reverseBytes(stream.readInt());
                length = Integer.reverseBytes(stream.readInt());
                untruncatedLength = Integer.reverseBytes(stream.readInt());
            }
            else {

                timestampSeconds = stream.readInt();
                timestampMicroseconds = stream.readInt();
                length = stream.readInt();
                untruncatedLength = stream.readInt();
            }

            payload = new byte[length];

            stream.read(payload, 0, length);
        }
    }

    static class EthernetPacket {

        public final byte[] destinationMAC = new byte[6];
        public final byte[] sourceMAC = new byte[6];
        public final int etherType;
        public final byte[] userData;

        public EthernetPacket(byte[] bytes) throws IOException {

            DataInputStream stream = getStream(bytes);

            stream.read(destinationMAC);
            stream.read(sourceMAC);
            etherType = stream.readUnsignedShort();

            userData= new byte[stream.available()];

            stream.read(userData, 0, stream.available());
            stream.close();
        }
    }

    static class IPv4Packet {

        public final int version;
        public final int headerLength;
        public final byte serviceType;
        public final int totalLength;
        public final short identification;
        public final short flagsAndFragmentOffset;
        public final byte ttl;
        public final byte protocol;
        public final short headerChecksum;
        public final String sourceAddress;
        public final String destAddress;
        public final byte[] data;

        public IPv4Packet(byte[] bytes) throws IOException {

            DataInputStream stream = getStream(bytes);
            byte versionAndIHL = stream.readByte();

            version = (versionAndIHL & 0xf0) >> 4;
            headerLength = versionAndIHL & 0x0f;

            serviceType = stream.readByte();
            totalLength = stream.readUnsignedShort();
            identification = stream.readShort();
            flagsAndFragmentOffset = stream.readShort();
            ttl = stream.readByte();
            protocol = stream.readByte();
            headerChecksum = stream.readShort();
            sourceAddress = createIPAddress(stream);
            destAddress = createIPAddress(stream);

            data = new byte[stream.available()];

            stream.close();
            stream.read(data, 0, stream.available());
        }

        private String createIPAddress(DataInputStream stream) throws IOException {

            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < 4; i++) {

                builder.append(stream.readUnsignedByte());

                if (i < 3) {

                    builder.append(".");
                }
            }

            return builder.toString();
        }
    }

    static class IPv6Packet {

        public final int version;
        public final byte trafficClass;
        public final int flowLabel;
        public final int payloadLength;
        public final byte nextHeader;
        public final int hopLimit;
        public final String sourceAddress;
        public final String destAddress;
        public final byte[] data;

        public IPv6Packet(byte[] bytes) throws IOException {

            DataInputStream stream = getStream(bytes);
            int first32bits = stream.readInt();

            version = (first32bits & 0xf0000000) >> 28;
            trafficClass = (byte)((first32bits & 0x0ff00000) >> 20);
            flowLabel = (first32bits & 0x000fffff);

            payloadLength = stream.readUnsignedShort();
            nextHeader = stream.readByte();
            hopLimit = stream.readByte();
            sourceAddress = createIPAddress(stream);
            destAddress = createIPAddress(stream);

            data = new byte[stream.available()];

            stream.read(data, 0, stream.available());
            stream.close();
        }

        private String createIPAddress(DataInputStream stream) throws IOException {

            boolean zeroField = false;

            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < 8; i++) {

                int field = stream.readUnsignedShort();

                if (field != 0) {

                    builder.append(Integer.toHexString(field));
                }
                else if ((field == 0) && !zeroField) {

                    builder.append(":");
                    zeroField = true;
                }

                if ((i < 7) && ((field != 0) || !zeroField)) {

                    builder.append(":");
                }
            }

            return builder.toString();
        }
    }

    static class UDPDatagram {

        public final int sourcePort;
        public final int destPort;
        public final int length;
        public final short checksum;
        public final byte[] data;

        public UDPDatagram(byte[] bytes) throws IOException {

            DataInputStream stream = getStream(bytes);

            sourcePort = stream.readUnsignedShort();
            destPort = stream.readUnsignedShort();
            length = stream.readUnsignedShort();
            checksum = stream.readShort();

            data = new byte[stream.available()];

            stream.read(data, 0, stream.available());
            stream.close();
        }
    }
}
