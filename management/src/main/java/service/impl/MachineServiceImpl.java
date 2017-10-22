package service.impl;

import dao.MachineDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.MachineService;

@Service
public class MachineServiceImpl implements MachineService{
    @Autowired
    private MachineDao machineDao;

    @Override
    public int deleteDevice(String deviceId) {
        // 先删除相关的设备操作与绑定记录，在更新二维码的占有状态
        int delete = machineDao.deleteDevice(deviceId);
        int update = machineDao.releaseQrCode(deviceId);

//        System.out.println("delete: " + delete);
//        System.out.println("update: " + update);

        return update;
    }
}
