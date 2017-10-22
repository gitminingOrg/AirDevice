package dao;


public interface MachineDao {

    public int deleteDevice(String deviceId);

    public int releaseQrCode(String QrCode);
}
