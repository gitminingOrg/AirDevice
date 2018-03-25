package finley.monitor.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import finley.monitor.service.CityPM25Service;
import finley.monitor.service.MachineService;
import finley.monitor.vo.CityPM25Vo;
import finley.monitor.vo.MachineVo;
import utils.HttpDeal;
import utils.ResponseCode;
import utils.ResultData;

@RestController
@RequestMapping("/monitor")
public class MonitorController {
	@Autowired
	private MachineService machineService;

	@Autowired
	private CityPM25Service cityPM25Service;

	@RequestMapping(method = RequestMethod.GET, value = "/{machine}")
	public ModelAndView monitor(@PathVariable("machine") String machine) {
		ModelAndView view = new ModelAndView();
		Map<String, Object> condition = new HashMap<>();
		condition.put("machineId", machine);
		ResultData response = machineService.fetch(condition);
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			view.addObject("machineId", machine);
		}
		view.setViewName("/backend/item");
		return view;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{machine}/vertical")
    public ModelAndView verticalMonitor(@PathVariable("machine") String machine) {
	    ModelAndView view = new ModelAndView();
	    Map<String, Object> condition = new HashMap<>();
	    condition.put("machineId", machine);
	    ResultData response = machineService.fetch(condition);
	    if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
	        view.addObject("machineId", machine);
        }
        view.setViewName("/backend/monitor_vertical");
	    return view;
    }

	@RequestMapping(method = RequestMethod.GET, value = "/overview")
	public ModelAndView Monitor() {
		ModelAndView view = new ModelAndView();

		return view;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{machine}/inner")
	public ResultData inner(@PathVariable("machine") String machine) {
		ResultData result = new ResultData();
		Map<String, Object> condition = new HashMap<>();
		condition.put("machineId", machine);
		ResultData response = machineService.fetch(condition);
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			MachineVo vo = ((List<MachineVo>) response.getData()).get(0);
			String chipId = vo.getChipId();
			StringBuffer url = new StringBuffer(
					"http://commander.gmair.net/AirCleanerOperation/device/status/device?token=").append(chipId);
			String json = HttpDeal.getResponse(url.toString());
			JSONObject data = JSON.parseObject(json);
			if (StringUtils.isEmpty(data) || StringUtils.isEmpty(data.getJSONObject("contents")) || StringUtils.isEmpty(data.getJSONObject("contents").getJSONObject("status"))) {
				result.setResponseCode(ResponseCode.RESPONSE_NULL);
			} else {
				result.setData(data.getJSONObject("contents").getJSONObject("status"));
			}
		}
		return result;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{machine}/outer")
	public ResultData outer(@PathVariable("machine") String machine) {
		ResultData result = new ResultData();
		Map<String, Object> condition = new HashMap<>();
		condition.put("machineId", machine);
		ResultData response = cityPM25Service.fetch(condition);
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			CityPM25Vo vo = ((List<CityPM25Vo>) response.getData()).get(0);
			result.setData(vo);
		}
		return result;
	}
}
