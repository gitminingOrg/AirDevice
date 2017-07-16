package controller;

import java.util.HashMap;
import java.util.Map;

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
import vo.consumer.ConsumerGoods;
import vo.goods.ConsumerGoodsVo;
import vo.qrcode.QRCodeVo;
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
	
	@RequestMapping(method = RequestMethod.GET, value = "/overview")
	public ModelAndView overview() {
		ModelAndView view = new ModelAndView();
		view.setViewName("/backend/consumer/overview");
		return view;
	}
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value="/list")
	public DataTablePage<ConsumerGoodsVo> list(DataTableParam param) {
		DataTablePage<ConsumerGoodsVo> result = new DataTablePage<>(param);
		if (StringUtils.isEmpty(param)) {
			return result;
		}
		Map<String, Object> condition = new HashMap<>();
		condition.put("delivered", false);
		ResultData response = consumerService.fetchConsumerGoods(condition, param);
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result = (DataTablePage<ConsumerGoodsVo>) response.getData();
		}
		return result;
	}
}
