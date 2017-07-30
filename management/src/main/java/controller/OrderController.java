package controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.csvreader.CsvReader;

import model.order.TaobaoOrder;
import pagination.DataTablePage;
import pagination.DataTableParam;
import service.OrderService;
import utils.ResponseCode;
import utils.ResultData;
import vo.consumer.ConsumerShareCodeVo;
import vo.order.OrderVo;

@RestController
@RequestMapping("/order")
public class OrderController {
	private Logger logger = LoggerFactory.getLogger(OrderController.class);

	@Autowired
	private OrderService orderService;

	@RequestMapping(method = RequestMethod.GET, value = "/overview")
	public ModelAndView overview() {
		ModelAndView view = new ModelAndView();
		view.setViewName("/backend/order/overview");
		return view;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/upload")
	public ModelAndView upload(MultipartHttpServletRequest request) throws IOException {
		ModelAndView view = new ModelAndView();
		MultipartFile file = request.getFile("orderFile");
		if (file.isEmpty()) {
			logger.info("上传的订单文件为空");
			view.setViewName("redirect:/order/overview");
			return view;
		}
		InputStream stream = file.getInputStream();
		CsvReader reader = new CsvReader(stream, Charset.forName("gbk"));
		List<String[]> list = new ArrayList<>();
		// 读取表头
		reader.readHeaders();
		while (reader.readRecord()) {
			list.add(reader.getValues());
		}
		reader.close();
		List<TaobaoOrder> order = new LinkedList<>();
		for (int i = 0; i < list.size(); i++) {
			TaobaoOrder item = new TaobaoOrder(list.get(i));
			order.add(item);
		}
		ResultData response = orderService.upload(order);
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			logger.info("上传订单记录成功");
		} else {
			logger.info("上传订单记录失败");
		}
		view.setViewName("redirect:/order/overview");
		return view;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/list")
	public DataTablePage<OrderVo> list(DataTableParam param) {
		DataTablePage<OrderVo> result = new DataTablePage<>(param);
		if (StringUtils.isEmpty(param)) {
			return result;
		}
		Map<String, Object> condition = new HashMap<>();
		ResultData response = orderService.fetch(condition, param);
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result = (DataTablePage<OrderVo>) response.getData();
		}
		return result;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{orderId}")
	public ModelAndView detail(@PathVariable("orderId") String orderId) {
		ModelAndView view = new ModelAndView();
		if(StringUtils.isEmpty(orderId)) {
			view.setViewName("redirect:/order/overview");
			return view;
		}
		Map<String, Object> condition = new HashMap<>();
		condition.put("orderId", orderId);
		ResultData response = orderService.fetch(condition);
		if(response.getResponseCode() != ResponseCode.RESPONSE_OK) {
			view.setViewName("redirect:/order/overview");
			return view;
		}
		OrderVo vo = ((List<OrderVo>)response.getData()).get(0);
		view.addObject("order", vo);
		view.setViewName("/backend/order/detail");;
		return view;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/{orderId}/deliver")
	public ResultData deliver(@PathVariable("orderId") String orderId, String productSerial) {
		ResultData result = new ResultData();
		if(StringUtils.isEmpty(orderId) || StringUtils.isEmpty(productSerial)) {
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription("订单编号和产品编号");
			return result;
		}
		TaobaoOrder order = new TaobaoOrder();
		order.setOrderId(orderId);
		order.setProductSerial(productSerial);
		ResultData response = orderService.assignSerial(order);
		if(response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setResponseCode(ResponseCode.RESPONSE_OK);
		}else {
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription("发货失败");
		}
		return result;
	}
}
