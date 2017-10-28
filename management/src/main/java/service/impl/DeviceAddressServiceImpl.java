package service.impl;

import dao.DeviceAddressDao;
import model.address.DeviceAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.DeviceAddressService;
import utils.ResultData;

import java.util.List;


@Service
public class DeviceAddressServiceImpl implements DeviceAddressService {

    @Autowired
    private DeviceAddressDao addressDao;

    @Override
    public ResultData getDeviceAddress() {
        return addressDao.getDeviceAddress();
    }
}
