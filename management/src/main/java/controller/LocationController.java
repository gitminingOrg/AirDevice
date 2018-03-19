package controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import config.ManagementConfig;
import model.GmairOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import service.GMorderService;
import utils.HttpDeal;
import utils.ResponseCode;
import utils.ResultData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by hushe on 2017/10/28.
 */
@RestController
@RequestMapping("/location")
public class LocationController {
    private Logger logger = LoggerFactory.getLogger(LocationController.class);

    @Autowired
    GMorderService gMorderService;

    @RequestMapping(method = RequestMethod.GET, value = "/ip")
    public ResultData ip2city(String ip) {
        ResultData result = new ResultData();

        String url = new StringBuffer
                ("http://apis.map.qq.com/ws/location/v1/ip").append("?ip=").append(ip)
                .append("&key=").append(ManagementConfig.getValue("tencent_map_key"))
                .toString();

        String response = HttpDeal.getResponse(url);
        JSONObject json = JSON.parseObject(response);
        try {
            if (!StringUtils.isEmpty(json) && !StringUtils.isEmpty(json.getJSONObject("result").getJSONObject("ad_info"))) {
                JSONObject jo = new JSONObject();
                jo.put("nation", json.getJSONObject("result").getJSONObject("ad_info").get("nation"));
                jo.put("province", json.getJSONObject("result").getJSONObject("ad_info").get("province"));
                jo.put("city", json.getJSONObject("result").getJSONObject("ad_info").get("city"));
                result.setData(jo);
            } else {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                result.setDescription("IP未解析成功！");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/locationToCoord")
    public ResultData locationToCoord() {
        ResultData result = new ResultData();
        System.out.println("Processing method locationtocoord");
        Map<String, Object> condition = new HashMap<>();
        condition.put("lnglat", true);
        condition.put("blockFlag", 0);
        ResultData gmorderResponse = gMorderService.fetchOrder(condition);
        System.out.println("response: " + JSON.toJSONString(gmorderResponse));
        if (gmorderResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<GmairOrder> orderList = (List<GmairOrder>) gmorderResponse.getData();
            for (GmairOrder gmairOrder : orderList) {
                String address = gmairOrder.getAddress().replace(" ", "");
                String url = new StringBuilder("http://apis.map.qq.com/ws/geocoder/v1/")
                        .append("?address=")
                        .append(address)
                        .append("&key=")
                        .append(ManagementConfig.getValue("tencent_map_key"))
                        .toString();

                String response = HttpDeal.getResponse(url);
                JSONObject json = JSON.parseObject(response);
                try {
                    if (!StringUtils.isEmpty(json) && json.getInteger("status") == 0
                            && !StringUtils.isEmpty(json.getJSONObject("result"))) {
                        double lng = json.getJSONObject("result").getJSONObject("location").getDouble("lng");
                        double lat = json.getJSONObject("result").getJSONObject("location").getDouble("lat");
                        gmairOrder.setLongtitude(lng);
                        gmairOrder.setLatitude(lat);
                        logger.debug("order lng lat: " + JSON.toJSONString(gmairOrder));
                        gMorderService.modifyOrder(gmairOrder);
                    } else {
                        result.setResponseCode(ResponseCode.RESPONSE_NULL);
                        result.setDescription("未解析成功！");
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                    result.setDescription(e.getMessage());
                }
            }
        }

        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/order")
    public ResultData mapUrl() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", 0);
        ResultData gmorderResponse = gMorderService.fetchOrder(condition);
        if (gmorderResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<GmairOrder> orderList = (List<GmairOrder>) gmorderResponse.getData();
            result.setData(orderList);
        } else {
            result.setResponseCode(gmorderResponse.getResponseCode());
            result.setDescription(gmorderResponse.getDescription());
        }
        return result;
    }
}