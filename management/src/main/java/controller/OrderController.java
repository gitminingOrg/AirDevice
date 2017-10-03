package controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.ws.rs.Path;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.csvreader.CsvReader;

import form.OrderCreateForm;
import form.OrderMissionForm;
import model.order.CustomizeOrder;
import model.order.OrderMission;
import model.order.OrderStatus;
import model.order.TaobaoOrder;
import model.user.User;
import pagination.DataTablePage;
import pagination.DataTableParam;
import service.OrderService;
import utils.ResponseCode;
import utils.ResultData;
import vo.consumer.ConsumerShareCodeVo;
import vo.order.OrderVo;
import vo.user.UserVo;

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
		if (StringUtils.isEmpty(orderId)) {
			view.setViewName("redirect:/order/overview");
			return view;
		}
		Map<String, Object> condition = new HashMap<>();
		condition.put("orderId", orderId);
		ResultData response = orderService.fetch(condition);
		if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
			view.setViewName("redirect:/order/overview");
			return view;
		}
		OrderVo vo = ((List<OrderVo>) response.getData()).get(0);
		view.addObject("order", vo);
		view.setViewName("/backend/order/detail");
		;
		return view;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/{orderId}/deliver")
	public ResultData deliver(@PathVariable("orderId") String orderId, String productSerial, String shipNo) {
		ResultData result = new ResultData();
		if (StringUtils.isEmpty(orderId) || StringUtils.isEmpty(productSerial)) {
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription("订单编号和产品编号不正确");
			return result;
		}
		TaobaoOrder order = new TaobaoOrder();
		order.setOrderId(orderId);
		order.setProductSerial(productSerial);
		order.setShipNo(shipNo);
		order.setStatus(OrderStatus.SHIPPED);
		ResultData response = orderService.assign(order);
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setResponseCode(ResponseCode.RESPONSE_OK);
		} else {
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription("发货失败");
		}
		return result;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/create")
	public ModelAndView create() {
		ModelAndView view = new ModelAndView();
		view.setViewName("/backend/order/create");
		return view;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/create")
	public ResultData create(@Valid OrderCreateForm form, BindingResult br) {
		ResultData result = new ResultData();
		if (br.hasErrors()) {
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription(JSONObject.toJSONString(br.getAllErrors()));
			return result;
		}
		CustomizeOrder order = new CustomizeOrder(form.getOrderNo(), form.getOrderBuyer(),
				Double.parseDouble(form.getOrderPrice()), form.getReceiverName(), form.getReceiverPhone(),
				form.getReceiverAddress(), form.getOrderCoupon(), form.getGoodsName(), form.getPayTime(),
				form.getOrderChannel(), form.getDescription());
		ResultData response = orderService.create(order);
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setResponseCode(ResponseCode.RESPONSE_OK);
		} else {
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription(response.getDescription());
		}
		return result;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/{orderId}/mission")
	public ResultData mission(@PathVariable("orderId") String orderId) {
		ResultData result = new ResultData();
		Map<String, Object> condition = new HashMap<>();
		condition.put("orderId", orderId);
		condition.put("blockFlag", false);
		ResultData response = orderService.fetchMission4Order(condition);
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(response.getData());
		} else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
			response.setResponseCode(ResponseCode.RESPONSE_NULL);
			result.setDescription("当前此订单暂无任何事件记录");
		} else {
			response.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription(response.getDescription());
		}
		return result;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/{orderId}/mission/create")
	public ResultData missionCreate(@Valid OrderMissionForm form, BindingResult br,
			@PathVariable("orderId") String orderId) {
		ResultData result = new ResultData();
		if (br.hasErrors()) {
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription(JSONObject.toJSONString(br.getAllErrors()));
			return result;
		}
		Subject subject = SecurityUtils.getSubject();
		if (!subject.isAuthenticated() || subject.getPrincipal() == null) {
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription("请先登录再进行操作");
			return result;
		}
		Map<String, Object> condition = new HashMap<>();
		condition.put("orderId", orderId);
		condition.put("blockFlag", false);
		ResultData response = orderService.fetch(condition);
		if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription("当前订单无法进行该操作");
			logger.error(result.getDescription());
			return result;
		}
		OrderVo vo = ((List<OrderVo>) response.getData()).get(0);
		TaobaoOrder order = new TaobaoOrder();
		order.setOrderId(orderId);
		order.setProductSerial(vo.getProductSerial());
		order.setShipNo(vo.getShipNo());
		order.setStatus(OrderStatus.INSTALLING);
		orderService.assign(order);
		UserVo user = (UserVo) subject.getPrincipal();
		OrderMission mission = new OrderMission(orderId, form.getMissionTitle(), form.getMissionContent(),
				user.getUsername());
		response = orderService.create(mission);
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setResponseCode(ResponseCode.RESPONSE_OK);
			result.setData(response.getData());
		} else {
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription(response.getDescription());
		}
		return result;
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/{orderId}/complete")
	public ResultData complete(@PathVariable("orderId") String orderId) {
		ResultData result = new ResultData();
		Map<String, Object> condition = new HashMap<>();
		condition.put("orderId", orderId);
		condition.put("blockFlag", false);
		ResultData response = orderService.fetch(condition);
		if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription("当前订单无法进行该操作");
			logger.error(result.getDescription());
			return result;
		}
		OrderVo vo = ((List<OrderVo>) response.getData()).get(0);
		TaobaoOrder order = new TaobaoOrder();
		order.setOrderId(orderId);
		order.setProductSerial(vo.getProductSerial());
		order.setShipNo(vo.getShipNo());
		order.setStatus(OrderStatus.SUCCEED);
		orderService.assign(order);
		return result;
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/{orderId}/cancel")
	public ResultData cancel(@PathVariable("orderId") String orderId) {
		ResultData result = new ResultData();
		Map<String, Object> condition = new HashMap<>();
		condition.put("orderId", orderId);
		condition.put("blockFlag", false);
		ResultData response = orderService.fetch(condition);
		if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription("当前订单无法进行该操作");
			logger.error(result.getDescription());
			return result;
		}
		OrderVo vo = ((List<OrderVo>) response.getData()).get(0);
		TaobaoOrder order = new TaobaoOrder();
		order.setOrderId(orderId);
		order.setProductSerial(vo.getProductSerial());
		order.setShipNo(vo.getShipNo());
		order.setStatus(OrderStatus.REFUNDED);
		orderService.assign(order);
		return result;
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/status/list")
	public ResultData status() {
		ResultData result = new ResultData();
		ResultData response = orderService.fetchStatus();
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(response.getData());
		} else {
			result.setResponseCode(ResponseCode.RESPONSE_NULL);
			result.setDescription(response.getDescription());
		}
		return result;
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/channel/list")
	public ResultData channel() {
		ResultData result = new ResultData();
		ResultData response = orderService.fetchChannel();
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(response.getData());
		} else {
			result.setResponseCode(ResponseCode.RESPONSE_NULL);
			result.setDescription(response.getDescription());
		}
		return result;
	}
}
