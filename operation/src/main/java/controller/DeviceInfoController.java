package controller;

import model.DeviceInfo;
import model.ResultMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.DeviceControlService;
import service.DeviceReceiveService;
import utils.Constant;

import javax.servlet.http.HttpServletRequest;

@RequestMapping(value = "/info")
@RestController
public class DeviceInfoController {
    @Autowired
    private DeviceControlService deviceControlService;


    public void setDeviceControlService(DeviceControlService deviceControlService) {
        
        this.deviceControlService = deviceControlService;
    }

    @Autowired
    private DeviceReceiveService deviceReceiveService;

    public void setDeviceReceiveService(DeviceReceiveService deviceReceiveService) {
        this.deviceReceiveService = deviceReceiveService;
    }

    @RequestMapping(value = "/device/{deviceID}")
    public ResultMap getDeviceInfo(@PathVariable("deviceID") long deviceID) {
        ResultMap resultMap = new ResultMap();
        DeviceInfo deviceInfo = deviceReceiveService.getDeviceInfo(deviceID);
        if (deviceInfo == null) {
            resultMap.setStatus(ResultMap.STATUS_FAILURE);
            resultMap.setInfo("没有找到相应的设备");
        } else {
            resultMap.setStatus(ResultMap.STATUS_SUCCESS);
            resultMap.addContent("device", deviceInfo);
        }
        return resultMap;
    }

    @RequestMapping(value = "/server/{deviceID}")
    public ResultMap serverIPControl(HttpServletRequest request, @PathVariable("deviceID") long device) {
        ResultMap resultMap = new ResultMap();
        String server = request.getParameter("server");
        boolean result = deviceControlService.infoControl(Constant.SERVER_IP, server, device);
        if (!result) {
            resultMap.setStatus(ResultMap.STATUS_FAILURE);
            resultMap.setInfo("更新设备连接服务器失败");
        } else {
            resultMap.setStatus(ResultMap.STATUS_SUCCESS);
            resultMap.setInfo("更新设备连接服务器成功");
        }
        return resultMap;
    }

    @RequestMapping(value = "/port/{deviceID}/{port}")
    public ResultMap serverPortControl(@PathVariable("deviceID") long device, @PathVariable("port") String port) {
        ResultMap resultMap = new ResultMap();
        boolean result = deviceControlService.infoControl(Constant.SERVER_PORT, port, device);
        if (!result) {
            resultMap.setStatus(ResultMap.STATUS_FAILURE);
            resultMap.setInfo("更新服务器端口失败");
        } else {
            resultMap.setStatus(ResultMap.STATUS_SUCCESS);
            resultMap.setInfo("更新服务器端口成功");
        }
        return resultMap;
    }

    @RequestMapping(value = "/heartbeat/{deviceID}/{gap}")
    public ResultMap heartbeatControl(@PathVariable("deviceID") long device, @PathVariable("gap") int gap) {
        ResultMap resultMap = new ResultMap();
        boolean result = deviceControlService.infoControl(Constant.HEARTBEAT_GAP, gap, device);
        if (!result) {
            resultMap.setStatus(ResultMap.STATUS_FAILURE);
            resultMap.setInfo("更新服务器端口失败");
        } else {
            resultMap.setStatus(ResultMap.STATUS_SUCCESS);
            resultMap.setInfo("更新服务器端口成功");
        }
        return resultMap;
    }

    @RequestMapping(value = "/command/{deviceID}/{CTF}/{command}/{data}")
    public ResultMap generalCommand(@PathVariable("deviceID") long device, @PathVariable("CTF") int CTF, @PathVariable("command") String command, @PathVariable("data") int data) {
        ResultMap resultMap = new ResultMap();

        boolean result = deviceControlService.commandHandler(CTF, command, data, device, DeviceInfo.class);
        if (result) {
            resultMap.setStatus(ResultMap.STATUS_SUCCESS);
            resultMap.setInfo("获取成功");
        } else {
            resultMap.setStatus(ResultMap.STATUS_FAILURE);
            resultMap.setInfo("获取失败");
        }
        return resultMap;
    }
}