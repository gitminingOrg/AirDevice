package controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import service.MachineService;
import utils.ResponseCode;
import utils.ResultData;


@RestController
@RequestMapping("/machine")
public class MachineController {

    private Logger logger = LoggerFactory.getLogger(MachineController.class);

    @Autowired
    private MachineService machineService;

    @RequestMapping(method = RequestMethod.POST, value = "/device/delete/{deviceId}")
    public ResultData delete(@PathVariable String deviceId) {
        int result = machineService.deleteDevice(deviceId);
        ResultData resultData = new ResultData();
        resultData.setResponseCode(ResponseCode.RESPONSE_OK);
        resultData.setData(result);
        resultData.setDescription("删除设备：" + deviceId);
        logger.info("delete device using deviceId: " + deviceId);
        return resultData;
    }
}
