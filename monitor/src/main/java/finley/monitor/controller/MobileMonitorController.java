package finley.monitor.controller;


import finley.monitor.service.CityPM25Service;
import finley.monitor.service.MachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import utils.ResponseCode;
import utils.ResultData;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/mobileMonitor")
public class MobileMonitorController {

    @Autowired
    private MachineService machineService;

    @RequestMapping(method = RequestMethod.GET, value = "/{machine}")
    public ModelAndView mobileMonitor(@PathVariable("machine") String machine) {
        ModelAndView view = new ModelAndView();
        Map<String, Object> condition = new HashMap<>();
        condition.put("machineId", machine);
        ResultData response = machineService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            view.addObject("machineId", machine);
        }
        view.setViewName("/mobile/item");
        return view;
    }
}
