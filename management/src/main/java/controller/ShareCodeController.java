package controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import pagination.DataTablePage;
import pagination.DataTableParam;
import service.ConsumerService;
import utils.ResponseCode;
import utils.ResultData;
import vo.consumer.ConsumerShareCodeVo;

@RestController
@RequestMapping("/sharecode")
public class ShareCodeController {
	private Logger logger = LoggerFactory.getLogger(ShareCodeController.class);

	@Autowired
	private ConsumerService consumerService;
	
	@RequestMapping(method = RequestMethod.GET, value = "/overview")
	public ModelAndView overview() {
		ModelAndView view = new ModelAndView();
		view.setViewName("/backend/sharecode/overview");
		return view;
	}
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/list")
	public DataTablePage<ConsumerShareCodeVo> list(DataTableParam param) {
		DataTablePage<ConsumerShareCodeVo> result = new DataTablePage<>(param);
		if (StringUtils.isEmpty(param)) {
			return result;
		}
		Map<String, Object> condition = new HashMap<>();
		ResultData response = consumerService.fetchShareCode(condition, param);
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result = (DataTablePage<ConsumerShareCodeVo>) response.getData();
		}
		return result;
	}
}
