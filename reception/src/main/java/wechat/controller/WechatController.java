package wechat.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.enterprise.event.Reception;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.ResultMap;
import model.wechat.Article;
import model.wechat.Articles;
import model.wechat.InMessage;

import model.wechat.TextOutMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import util.Configuration;
import util.WechatUtil;
import utils.Encryption;
import utils.HttpDeal;
import utils.ResultData;
import utils.XStreamFactory;
import vip.service.ConsumerSerivce;
import vo.vip.ConsumerVo;
import bean.DeviceStatus;
import bean.WechatUser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.XStream;

import config.ReceptionConfig;
import config.WechatConfig;
import device.service.DeviceVipService;

@RestController
public class WechatController {
	private Logger logger = LoggerFactory.getLogger(WechatController.class);
	@Autowired
	private DeviceVipService deviceVipService;
	@Autowired
	private ConsumerSerivce consumerSerivce;

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
	@RequestMapping(method = RequestMethod.POST, value = "/wechat", produces="text/xml;charset=utf-8")
	public String handle(HttpServletRequest request, HttpServletRequest response) {
		try {
			ServletInputStream stream = request.getInputStream();
			String input = WechatUtil.inputStream2String(stream);
			XStream content = XStreamFactory.init(false);
            content.alias("xml", InMessage.class);
            logger.info("input:" + input);
            final InMessage message = (InMessage) content.fromXML(input);
            HttpSession session = request.getSession();
            session.setAttribute("openId", message.getFromUserName());
            logger.info("message: " + JSONObject.toJSONString(message));
            switch (message.getMsgType()) {
            	case "event":
            		if (message.getEvent().equals("subscribe")) {
						content.alias("xml", TextOutMessage.class);
						TextOutMessage result = new TextOutMessage();
						result.setFromUserName(message.getToUserName());
						result.setToUserName(message.getFromUserName());
						result.setContent("Hey,您终于来了！\n恭喜您找到新鲜至净空气提供者:\n果麦新风\n这里只跟您分享室内好空气的秘密。\n您可以点击下方菜单自助查阅所需要的内容,若想获取更多帮助,请发送文字、语音、视频告诉我们，或拨打客服热线400-994-6898(客服在线时间周一到周五9:00-18:00),感谢您的支持，祝您生活愉快!");
						String xml = content.toXML(result);
						return xml;
					}
            		if(message.getEventKey().equals("gmair")){
            			String openId = message.getFromUserName();
                        ConsumerVo consumerVo = consumerSerivce.login(openId);
                        if(consumerVo != null && consumerVo.getCustomerId() != null){
                        	List<DeviceStatus> deviceStatus = deviceVipService.getUserCleaner(consumerVo.getCustomerId());
							if(deviceStatus.size() > 0) {
								content.alias("xml", Articles.class);
								content.alias("item", Article.class);
								Articles result = new Articles();
								result.setFromUserName(message.getToUserName());
								result.setToUserName(message.getFromUserName());
								result.setCreateTime(new Date().getTime());
								List<Article> list = new ArrayList<>();
								Article welcome = new Article();
								welcome.setTitle("果麦新风");
								SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
								Calendar c = Calendar.getInstance();
								String time = sdf.format(c.getTime());
								welcome.setPicUrl("http://commander.gmair.net/reception/www/img/logo_blue.png?"+time);
								welcome.setUrl("http://commander.gmair.net/reception/www/index.html#/home/device");
								StringBuffer sb = new StringBuffer();
								welcome.setDescription("");
								for (DeviceStatus status : deviceStatus) {
									sb.append("设备名称:"+status.getDeviceName()+"\n");
									if(status.getCleanerStatus() == null){
										sb.append("设备状态:离线\n");
									}else{
										sb.append("PM2.5 :"+status.getCleanerStatus().getPm25()+"µg/m³\n");
										sb.append("室内温度:"+status.getCleanerStatus().getTemperature()+"℃\n");
										sb.append("室内湿度:"+status.getCleanerStatus().getHumidity()+"%\n");
										if(status.getCleanerStatus().getCo2() > 0 && status.getCleanerStatus().getCo2() != 2000){
											sb.append("二氧化碳:"+status.getCleanerStatus().getCo2()+"ppm\n");
										}
										sb.append("风机风量:"+status.getCleanerStatus().getVelocity()+"m³/h\n");
										if(status.getCleanerStatus().getHeat() > 0){
											sb.append("设备辅热:开\n");
										}else{
											sb.append("设备辅热:关\n");
										}

									}
									sb.append("\n\n");
								}
								welcome.setDescription(sb.toString());
								list.add(welcome);
								result.setArticles(list);
								result.setArticleCount(list.size());
								content.processAnnotations(Article.class);
								String xml = content.toXML(result);
								return xml;
							}
                        }
						content.alias("xml", TextOutMessage.class);
						TextOutMessage result = new TextOutMessage();
						result.setFromUserName(message.getToUserName());
						result.setToUserName(message.getFromUserName());
						result.setContent("您尚未绑定果麦新风设备,暂不提供空气数据支持");
						String xml = content.toXML(result);
                        return xml;
            		}
            		break;
            }
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
							+ URLEncoder.encode("http://" + ReceptionConfig.getValue("domain_url") + "/reception/www/index.html#/device/bind/" + serial, "utf-8")
							+ "&response_type=code&scope=snsapi_base&state=view#wechat_redirect";
					result.setStatus(ResultMap.STATUS_SUCCESS);
					result.addContent("redirect_url", link);
					return result;
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} else {
				return oauth(result, code);
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
							+ URLEncoder.encode("http://" + "commander.gmair.net/reception/www/index.html#/device/auth/" + token, "utf-8")
							+ "&response_type=code&scope=snsapi_base&state=view#wechat_redirect";
					result.setStatus(ResultMap.STATUS_SUCCESS);
					result.addContent("redirect_url", link);
					return result;
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} else {
				return oauth(result, code);
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

	private ResultMap oauth(ResultMap result, String code) {
		String openId = WechatUtil.queryOauthOpenId(ReceptionConfig.getValue("wechat_appid"), ReceptionConfig.getValue("wechat_secret"), code);
		result.setStatus(ResultMap.STATUS_SUCCESS);
		result.addContent("openId", openId);
		return result;
	}
}