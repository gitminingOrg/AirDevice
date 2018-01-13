package dao;

import utils.ResultData;

import java.util.Map;

public interface DeviceAddressDao {

    ResultData getDeviceAddress(Map<String, Object> condition);
}
