package model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ByteUtil;
import utils.CRC16;

import java.io.Serializable;


public class MCPPacket implements Serializable {

    private static final long serialVersionUID = 6172522038548028403L;
    public transient static Logger LOG = LoggerFactory.getLogger(MCPPacket.class);

    private byte[] FRH;
    private byte[] CTF;
    private byte[] CID;
    private byte[] UID;
    private byte[] LEN;
    private byte[] DATA;
    private byte[] CRC;
    private byte[] FRT;


    public MCPPacket(byte[] cTF, byte[] cID, byte[] uID,
                     byte[] lEN, byte[] dATA, byte[] cRC) {
        FRH = new byte[]{-0x11};
        CTF = cTF;
        CID = cID;
        UID = uID;
        LEN = lEN;
        DATA = dATA;
        CRC = cRC;
        FRT = new byte[]{-0x12};
    }

    public MCPPacket(byte[] fRH, byte[] cTF, byte[] cID, byte[] uID,
                     byte[] lEN, byte[] dATA, byte[] cRC, byte[] fRT) {
        super();
        FRH = fRH;
        CTF = cTF;
        CID = cID;
        UID = uID;
        LEN = lEN;
        DATA = dATA;
        CRC = cRC;
        FRT = fRT;
    }


    public byte[] getFRH() {
        return FRH;
    }

    public byte[] getCTF() {
        return CTF;
    }

    public byte[] getCID() {
        return CID;
    }

    public byte[] getUID() {
        return UID;
    }

    public byte[] getLEN() {
        return LEN;
    }

    public byte[] getDATA() {
        return DATA;
    }

    public byte[] getCRC() {
        return CRC;
    }

    public byte[] getFRT() {
        return FRT;
    }

    public void setLEN(byte[] lEN) {
        LEN = lEN;
    }

    public void setDATA(byte[] dATA) {
        DATA = dATA;
    }

    public static boolean validateMCPPacket(MCPPacket packet) {
        if (packet.FRH[0] != -0x11) {
            return false;
        }
        if (packet.FRT[0] != -0x12) {
            return false;
        }

        int expectCRC = CRC16.CRC_XModem(ByteUtil.concatAll(packet.CTF, packet.CID, packet.UID, packet.LEN, packet.DATA));
        int actualCRC = ByteUtil.byteArrayToInt(packet.CRC);
        if (expectCRC != actualCRC) {
            LOG.error("CRC check failed, expect :" + expectCRC + ", actual : " + actualCRC);
            return false;
        }
        return true;
    }

    public void calCRC() {
        int expectCRC = CRC16.CRC_XModem(ByteUtil.concatAll(CTF, CID, UID, LEN, DATA));
        byte[] CRC = ByteUtil.intToByteArray(expectCRC, 2);
        this.CRC = CRC;
    }

    public byte[] toByte() {
        byte[] result = ByteUtil.concatAll(FRH, CTF, CID, UID, LEN, DATA, CRC, FRT);
        return result;
    }

    @Override
    public String toString() {
        byte[] result = toByte();
        StringBuffer sb = new StringBuffer();
        for (byte b : result) {
            sb.append(String.format("%02x", b).toUpperCase());
        }
        return sb.toString();
    }


}
