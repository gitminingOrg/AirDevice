package dao.impl;

import dao.BaseDao;
import dao.MachineDao;
import org.springframework.stereotype.Repository;

@Repository
public class MachineDaoImpl extends BaseDao implements MachineDao{

    @Override
    public int deleteDevice(String deviceId) {
        return sqlSession.delete("management.machine.deleteDevice", deviceId);
    }

    @Override
    public int releaseQrCode(String QrCode) {
        return sqlSession.update("management.machine.releaseQrcode", QrCode);
    }
}
