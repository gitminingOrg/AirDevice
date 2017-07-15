package controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import service.ConsumerService;
import utils.ResponseCode;
import utils.ResultData;
@RestController
@RequestMapping("/consumer")
public class ConsumerController {
	@Autowired
	private ConsumerService consumerService;
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/list/all")
	public ResultData listAll() {
		ResultData result = new ResultData();
		Map<String, Object> condition = new HashMap<>();
		ResultData response = consumerService.fetchConsumerGoods(condition);
		if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
			result.setResponseCode(ResponseCode.RESPONSE_NULL);
			result.setDescription("No goods data found.");
			return result;
		}
		result.setData(response.getData());
		return result;
	}
}
