package controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.csvreader.CsvReader;

import model.order.TaobaoOrder;
import service.OrderService;
import utils.ResponseCode;
import utils.ResultData;

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
}
