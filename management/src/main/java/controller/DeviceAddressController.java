package controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import service.DeviceAddressService;
import service.MachineService;
import utils.ResponseCode;
import utils.ResultData;
import vo.MachineStataVo;
import vo.address.DeviceAddressVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/deviceAddress")
public class DeviceAddressController {

    @Autowired
    private DeviceAddressService deviceAddressService;

    @Autowired
    private MachineService machineService;

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
            List<DeviceAddressVO> list = (List<DeviceAddressVO>) response.getData();
            Map<String, Object> condition = new HashMap<>();
            JSONObject json = new JSONObject();
            JSONArray devices = new JSONArray();
            for (DeviceAddressVO da : list) {
                json = JSON.parseObject(da.toString());
                String machineId = da.getDevice_id();
                condition.put("machineId", machineId);
                response = machineService.queryMachineStata(condition);
                if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                    MachineStataVo vo = ((List<MachineStataVo>) response.getData()).get(0);
                    json.put("pm25", vo.getPm25());
                    json.put("is_power_on", vo.isPowerOn());
                    json.put("update_time", vo.getCreateAt());
                }
                devices.add(json);
            }
            result.setData(devices);
            return result;
        }else {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        }
        return result;
    }
}
