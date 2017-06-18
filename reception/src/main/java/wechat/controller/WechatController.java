package wechat.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ejb.Init;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import config.ReceptionConfig;
import config.WechatConfig;
import model.ResultMap;
import util.Configuration;
import util.WechatUtil;
import utils.Encryption;

@RestController
public class WechatController {
	private Logger logger = LoggerFactory.getLogger(WechatController.class);

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/wechat")
	public String check(HttpServletRequest request) {
		String signature = request.getParameter("signature");// 微信加密签名
		String timestamp = request.getParameter("timestamp");// 时间戳
		String nonce = request.getParameter("nonce");// 随机数
		String echostr = request.getParameter("echostr");//
		List<String> params = new ArrayList<>();
		params.add(ReceptionConfig.getValue("wechat_token"));
		params.add(timestamp);
		params.add(nonce);
		Collections.sort(params);
		String temp = params.get(0) + params.get(1) + params.get(2);
		if (Encryption.SHA1(temp).equals(signature)) {
			return echostr;
		}
		return "";
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/wechat/init")
	public ResultMap init(String url) {
		ResultMap result = new ResultMap();
		if(StringUtils.isEmpty(url)) {
			result.setStatus(ResultMap.STATUS_FAILURE);
			result.setInfo("请传入当前页面的完整URL");
			return result;
		}
		Configuration configuration = WechatConfig.config(url);
		result.addContent("configuration", configuration);
		return result;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/token")
	public String token() {
		return ReceptionConfig.getAccessToken();
	}
}
