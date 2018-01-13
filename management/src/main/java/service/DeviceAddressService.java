package service;

import utils.ResultData;

import java.util.Map;

public interface DeviceAddressService {

    ResultData getDeviceAddress(Map<String, Object> condition);
}
