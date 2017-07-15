package controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import service.GoodsService;
import utils.ResponseCode;
import utils.ResultData;

@RestController
@RequestMapping("/goods")
public class GoodsController {
	private Logger logger = LoggerFactory.getLogger(GoodsController.class);

	@Autowired
	private GoodsService goodsService;

	@RequestMapping(method = RequestMethod.GET, value = "/overview")
	public ModelAndView overview() {
		ModelAndView view = new ModelAndView();
		view.setViewName("/backend/goods/overview");
		return view;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/list/all")
	public ResultData listAll() {
		ResultData result = new ResultData();
		Map<String, Object> condition = new HashMap<>();
		condition.put("blockFlag", "false");
		ResultData response = goodsService.fetch(condition);
		if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
			result.setResponseCode(ResponseCode.RESPONSE_NULL);
			result.setDescription("No goods data found.");
			return result;
		}
		result.setData(response.getData());
		return result;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/model/all")
	public ResultData modelAvailable(String goodsId) {
		ResultData result = new ResultData();
		Map<String, Object> condition = new HashMap<>();
		if(!StringUtils.isEmpty(goodsId)) {
			condition.put("goodsId", goodsId);
		}
		ResultData response = goodsService.fetchModel(condition);
		if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
			result.setResponseCode(ResponseCode.RESPONSE_NULL);
			result.setDescription("No model available.");
			return result;
		}
		result.setData(response.getData());
		return result;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/{goodsId}/model")
	public ResultData goodsModel(@PathVariable("goodsId") String goodsId) {
		ResultData result = new ResultData();
		if(StringUtils.isEmpty(goodsId)) {
			logger.info("Query goods model with goods id: " + goodsId + " failure.");
			result.setResponseCode(ResponseCode.RESPONSE_NULL);
			result.setDescription("Sorry, the request is meaningless.");
			return result;
		}
		Map<String, Object> condition = new HashMap<>();
		condition.put("goodsId", goodsId);
		ResultData response = goodsService.fetchModel(condition);
		if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
			result.setResponseCode(ResponseCode.RESPONSE_NULL);
			result.setDescription("No model available.");
			return result;
		}
		result.setData(response.getData());
		return result;
	}
}
