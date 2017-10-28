package controller;

import form.FeedBackForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import service.ServiceRequestService;
import utils.ResponseCode;
import utils.ResultData;

@RestController
@RequestMapping("/servicerequest")
public class ServiceRequestController {
	private Logger logger = LoggerFactory.getLogger(ServiceRequestController.class);

	@Autowired
	private ServiceRequestService serviceRequestService;

	@RequestMapping(method = RequestMethod.GET, value = "/all")
	public ResultData list() {
		ResultData result = serviceRequestService.getFeedback();
		return result;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/feedback")
	public ResultData list(@RequestParam(name = "status") int status) {
		ResultData result = serviceRequestService.getFeedBack(status);
		return result;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/feedback")
	public ResultData update(@Validated FeedBackForm feedBackForm, BindingResult br)
	{
		if (br.hasErrors()) {
			ResultData resultData = new ResultData();
			resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
			resultData.setDescription(br.getAllErrors().toString());
			return resultData;
		}

		int id = feedBackForm.getId();
		int status = feedBackForm.getStatus();
		if (status == 0 || status > 2) {
			ResultData resultData = new ResultData();
			resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
			resultData.setDescription("status code can only be 1 or 2");
			return resultData;
		}

		logger.info("更改反馈信息id:" + id + "，设置状态为：" + status);
		ResultData resultData = serviceRequestService.updateFeedbackStatus(id, status);
		return resultData;
	}

}
