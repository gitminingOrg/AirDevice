package wechat.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.XStream;

import bean.WechatUser;
import config.ReceptionConfig;
import config.WechatConfig;
import model.ResultMap;
import util.Configuration;
import util.WechatUtil;
import utils.Encryption;
import utils.HttpDeal;

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

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "wechat", produces="text/xml;charset=utf-8")
	public String handle(HttpServletRequest request, HttpServletRequest response) {
		try {
			ServletInputStream stream = request.getInputStream();
			String input = WechatUtil.inputStream2String(stream);
			
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
		return "";
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/wechat/init")
	public ResultMap init(String url) {
		url = url.split("#")[0];
		ResultMap result = new ResultMap();
		if (StringUtils.isEmpty(url)) {
			result.setStatus(ResultMap.STATUS_FAILURE);
			result.setInfo("请传入当前页面的完整URL");
			return result;
		}
		Configuration configuration = WechatConfig.config(url);
		logger.info(JSONObject.toJSONString(configuration));
		result.setStatus(ResultMap.STATUS_SUCCESS);
		result.addContent("configuration", configuration);
		return result;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/wechat/token")
	public String token() {
		return ReceptionConfig.getAccessToken();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/wechat/user")
	public ResultMap user(String serial, String code, HttpServletRequest request, HttpServletResponse response) {
		ResultMap result = new ResultMap();
		// identify the request origin
		if (WechatUtil.isWechat(request)) {
			logger.info("request from wechat");
			if (StringUtils.isEmpty(code)) {
				logger.info("redirect 1st");
				try {
					logger.info("serial: " + serial);
					String link = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
							+ ReceptionConfig.getValue("wechat_appid") + "&redirect_uri="
							+ URLEncoder.encode("http://" + "commander.qingair.net/reception/www/index.html#/device/bind/" + serial, "utf-8")
							+ "&response_type=code&scope=snsapi_base&state=view#wechat_redirect";
					result.setStatus(ResultMap.STATUS_SUCCESS);
					result.addContent("redirect_url", link);
					return result;
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} else {
				logger.info("request 2nd, code: " + code);
				String openId = WechatUtil.queryOauthOpenId(ReceptionConfig.getValue("wechat_appid"), ReceptionConfig.getValue("wechat_secret"), code);
				result.setStatus(ResultMap.STATUS_SUCCESS);
				result.addContent("openId", openId);
				return result;
			}
		}
		result.setStatus(ResultMap.STATUS_SUCCESS);
		result.addContent("openId", "123");
		return result;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/wechat/auth")
	public ResultMap auth(String token, String code, HttpServletRequest request, HttpServletResponse response) {
		ResultMap result = new ResultMap();
		// identify the request origin
		if (WechatUtil.isWechat(request)) {
			logger.info("request from wechat");
			if (StringUtils.isEmpty(code)) {
				logger.info("redirect 1st");
				try {
					logger.info("token: " + token);
					String link = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
							+ ReceptionConfig.getValue("wechat_appid") + "&redirect_uri="
							+ URLEncoder.encode("http://" + "commander.qingair.net/reception/www/index.html#/device/auth/" + token, "utf-8")
							+ "&response_type=code&scope=snsapi_base&state=view#wechat_redirect";
					result.setStatus(ResultMap.STATUS_SUCCESS);
					result.addContent("redirect_url", link);
					return result;
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} else {
				logger.info("request 2nd, code: " + code);
				String openId = WechatUtil.queryOauthOpenId(ReceptionConfig.getValue("wechat_appid"), ReceptionConfig.getValue("wechat_secret"), code);
				result.setStatus(ResultMap.STATUS_SUCCESS);
				result.addContent("openId", openId);
				return result;
			}
		}
		return result;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/wechat/info/{openId}")
	public ResultMap getWechatUserInfo(@PathVariable("openId")String openId){
		ResultMap resultMap = new ResultMap();
		try{
			String accessToken = ReceptionConfig.getAccessToken();
			String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token="+accessToken+"&openid="+openId+"&lang=zh_CN";
			String result = HttpDeal.getResponse(url);
			Gson gson = new GsonBuilder().disableHtmlEscaping().create();
			WechatUser wechatUser = gson.fromJson(result, WechatUser.class);
			resultMap.setStatus(ResultMap.STATUS_SUCCESS);
			resultMap.addContent("wechatUser", wechatUser);
		}catch(Exception e){
			resultMap.setStatus(ResultMap.STATUS_FAILURE);
			logger.error("get wechat user info failed",e);
		}
		
		return resultMap;
		
	}
}
