package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import utils.ResultData;

@RestController
@RequestMapping("/servicerequest")
public class ServiceRequestController {
	private Logger logger = LoggerFactory.getLogger(ServiceRequestController.class);

	@RequestMapping(method = RequestMethod.GET, value = "/all")
	public ResultData list() {
		ResultData result = new ResultData();

		return result;
	}
}
