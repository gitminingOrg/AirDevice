package controller;

import com.alibaba.fastjson.JSON;
import form.CommodityForm;
import model.guomai.Commodity;
import model.order.CommodityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import service.CommodityService;
import utils.ResponseCode;
import utils.ResultData;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hushe on 2018/1/8.
 */
@RestController
@RequestMapping("/commodity")
public class CommodityController {
    private Logger logger = LoggerFactory.getLogger(CommodityController.class);

    @Autowired
    private CommodityService commodityService;

    @RequestMapping(method = RequestMethod.GET, value = "/list")
    public ResultData list() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        ResultData response = commodityService.fetch(condition);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else {
            result.setDescription(response.getDescription());
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/create")
    public ResultData create(@Valid CommodityForm form, BindingResult br) {
        ResultData result = new ResultData();
        if (br.hasErrors()) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("表单中含有非法参数");
            logger.error(JSON.toJSONString(br.getAllErrors()));
            return result;
        }
        Commodity commodity = new Commodity(CommodityType.convertToCommodityType(form.getType()), form.getName(), form.getPrice(), form.getBonus());
        ResultData response = commodityService.create(commodity);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setDescription(response.getDescription());
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/update")
    public ResultData update(Commodity commodity) {
        ResultData result = new ResultData();
        ResultData response = commodityService.modify(commodity);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setDescription(response.getDescription());
        }
        return result;
    }
}
