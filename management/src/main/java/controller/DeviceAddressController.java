package controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import service.DeviceAddressService;
import utils.ResponseCode;
import utils.ResultData;

@RestController
@RequestMapping("/deviceAddress")
public class DeviceAddressController {

    @Autowired
    private DeviceAddressService deviceAddressService;

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public ResultData getDeviceAddress() {
        ResultData result = new ResultData();
        ResultData response = deviceAddressService.getDeviceAddress();
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("服务器异常，请稍后尝试");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            return result;
        }
        return result;
    }
}
