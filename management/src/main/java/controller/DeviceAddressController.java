package controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import service.DeviceAddressService;
import utils.ResultData;

@RestController
@RequestMapping("/deviceAddress")
public class DeviceAddressController {

    @Autowired
    private DeviceAddressService deviceAddressService;

    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public ResultData getDeviceAddress() {
        ResultData resultData = deviceAddressService.getDeviceAddress();
        return resultData;
    }
}
