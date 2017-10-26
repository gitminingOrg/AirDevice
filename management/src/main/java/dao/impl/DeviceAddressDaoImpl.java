package dao.impl;

import dao.DeviceAddressDao;
import dao.BaseDao;
import model.address.DeviceAddress;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class DeviceAddressDaoImpl extends BaseDao implements DeviceAddressDao {
    @Override
    public List<DeviceAddress> getDeviceAddress() {
        return sqlSession.selectList("management.deviceAddress.selectDeviceAddress");
    }
}
