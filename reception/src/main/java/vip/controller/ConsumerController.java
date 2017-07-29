package vip.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import form.LoginForm;
import model.ResultMap;
import model.consumer.ConsumerShareCode;
import utils.ResponseCode;
import utils.ResultData;
import vip.service.ConsumerSerivce;
import vip.service.ShareCodeService;
import vip.service.UploadService;
import vo.consumer.ConsumerShareCodeVo;
import vo.vip.ConsumerVo;

@RestController
@RequestMapping("/consumer")
public class ConsumerController {
	private Logger logger = LoggerFactory.getLogger(ConsumerController.class);

	@Autowired
	private ConsumerSerivce consumerService;

	@Autowired
	private UploadService uploadService;

	@Autowired
	private ShareCodeService shareCodeService;

	@RequiresGuest
	@RequestMapping(method = RequestMethod.POST, value = "/login")
	public ResultMap login(LoginForm form) {
		ResultMap result = new ResultMap();
		try {
			Subject subject = SecurityUtils.getSubject();
			subject.login(new UsernamePasswordToken(form.getPhone(), ""));
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.setStatus(ResultMap.STATUS_FAILURE);
			result.setInfo(e.getMessage());
			return result;
		}
		result.setStatus(ResultMap.STATUS_SUCCESS);
		return result;
	}

	@RequiresAuthentication
	@RequestMapping(method = RequestMethod.GET, value = "/info")
	public ResultMap info() {
		ResultMap result = new ResultMap();
		Subject subject = SecurityUtils.getSubject();
		ConsumerVo current = (ConsumerVo) subject.getPrincipal();
		if (StringUtils.isEmpty(current)) {
			result.setStatus(ResultMap.STATUS_FORBIDDEN);
			result.setInfo("No user authenticated, please login first.");
			return result;
		}
		result.setStatus(ResultMap.STATUS_SUCCESS);
		result.addContent("consumer", current);
		return result;
	}

	@RequiresAuthentication
	@RequestMapping(method = RequestMethod.GET, value = "/share/reference")
	public ResultMap shareCode() {
		ResultMap result = new ResultMap();
		Subject subject = SecurityUtils.getSubject();
		ConsumerVo current = (ConsumerVo) subject.getPrincipal();
		if (StringUtils.isEmpty(current)) {
			result.setStatus(ResultMap.STATUS_FORBIDDEN);
			result.setInfo("No user authenticated, please login first.");
			return result;
		}
		String customerId = current.getCustomerId();
		Map<String, Object> condition = new HashMap<>();
		condition.put("userId", customerId);
		condition.put("status", 1);
		boolean status = consumerService.canShare(condition);
		if (!status) {
			result.setStatus(ResultMap.STATUS_FORBIDDEN);
			result.setInfo("Do not have permission to share.");
			return result;
		}
		condition.clear();
		condition.put("consumerId", customerId);
		condition.put("blockFlag", false);
		ResultData response = consumerService.fetchShareCode(condition);
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			ConsumerShareCodeVo vo = ((List<ConsumerShareCodeVo>) response.getData()).get(0);
			result.setStatus(ResultMap.STATUS_SUCCESS);
			result.addContent("sharecode", vo);
			return result;
		}
		ConsumerShareCode code = new ConsumerShareCode(customerId);
		response = consumerService.createShareCode(code);
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setStatus(ResultMap.STATUS_SUCCESS);
			result.addContent("sharecode", response.getData());
			return result;
		}
		result.setStatus(ResultMap.STATUS_FAILURE);
		return result;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/share/upload")
	public ResultMap upload(MultipartHttpServletRequest request) {
		ResultMap result = new ResultMap();
		Subject subject = SecurityUtils.getSubject();
		ConsumerVo current = (ConsumerVo) subject.getPrincipal();
		if (StringUtils.isEmpty(current)) {
			result.setStatus(ResultMap.STATUS_FORBIDDEN);
			result.setInfo("No user authenticated, please login first.");
			return result;
		}
		Map<String, Object> condition = new HashMap<>();
		String customerId = current.getCustomerId();
		condition.put("consumerId", customerId);
		condition.put("blockFlag", false);
		ResultData response = consumerService.fetchShareCode(condition);
		if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
			result.setStatus(ResultMap.STATUS_FORBIDDEN);
			result.setInfo("The user does not have a share code.");
			return result;
		}
		ConsumerShareCodeVo vo = ((List<ConsumerShareCodeVo>) response.getData()).get(0);
		String context = request.getSession().getServletContext().getRealPath("/");
		try {
			MultipartFile file = request.getFile("credit");
			response = uploadService.upload(file, context);
			if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
				response = shareCodeService.customizeShareCode(response.getData().toString(), vo.getShareCode());
				if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
					result.setStatus(ResultMap.STATUS_SUCCESS);
					result.addContent("sharepath", response.getData().toString());
					return result;
				}
				result.setInfo("generate customize picture failed.");
				return result;
			} else {
				result.setStatus(ResultMap.STATUS_FAILURE);
				result.setInfo("image upload failure");
				return result;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return result;
	}
}
