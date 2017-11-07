package controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import config.ManagementConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import utils.HttpDeal;
import utils.ResponseCode;
import utils.ResultData;

/**
 * Created by hushe on 2017/10/28.
 */
@RestController
@RequestMapping("/location")
public class LocationController {
    private Logger logger = LoggerFactory.getLogger(LocationController.class);

    @RequestMapping(method =  RequestMethod.GET, value = "/ip")
    public ResultData ip2city(String ip){
        ResultData result = new ResultData();

        String url = new StringBuffer
                ("http://apis.map.qq.com/ws/location/v1/ip").append("?ip=").append(ip)
                .append("&key=").append(ManagementConfig.getValue("tencent_map_key"))
                .toString();

        String response = HttpDeal.getResponse(url);
        JSONObject json = JSON.parseObject(response);
        try {
            JSONObject jo = new JSONObject();
            jo.put("nation", json.getJSONObject("result").getJSONObject("ad_info").getJSONObject("nation"));
            jo.put("province", json.getJSONObject("result").getJSONObject("ad_info").getJSONObject("province"));
            jo.put("city", json.getJSONObject("result").getJSONObject("ad_info").getJSONObject("city"));
            result.setData(jo);
        }catch (Exception e){
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}