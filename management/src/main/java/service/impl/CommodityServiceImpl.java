package service.impl;

import dao.CommodityDao;
import model.guomai.Commodity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.CommodityService;
import utils.ResponseCode;
import utils.ResultData;
import vo.guomai.CommodityVo;

import java.util.List;
import java.util.Map;

/**
 * Created by hushe on 2018/1/8.
 */
@Service
public class CommodityServiceImpl implements CommodityService {
    private Logger logger = LoggerFactory.getLogger(CommodityServiceImpl.class);

    @Autowired
    private CommodityDao commodityDao;

    @Override
    public ResultData fetch(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = commodityDao.query(condition);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            return response;
        } else {
            List<CommodityVo> list = (List<CommodityVo>) response.getData();
            result.setData(list);
            return result;
        }
    }

    @Override
    public ResultData create(Commodity commodity) {
        ResultData result = new ResultData();
        ResultData response = commodityDao.insert(commodity);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else {
            result.setDescription(response.getDescription());
        }
        return result;
    }

    @Override
    public ResultData modify(Commodity commodity) {
        ResultData result = new ResultData();
        ResultData response = commodityDao.update(commodity);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else {
            result.setDescription(response.getDescription());
        }
        return result;
    }
}
