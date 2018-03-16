package service.impl;

import com.mysql.jdbc.util.ResultSetUtil;
import dao.GmairDao;
import model.GmairOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.GMorderService;
import utils.ResponseCode;
import utils.ResultData;

import java.util.Map;

@Service
public class GMOrderServiceImpl implements GMorderService {
    @Autowired
    private GmairDao gmairDao;

    @Override
    public ResultData fetchOrder(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = gmairDao.query(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return result;
        }
        result.setData(response.getData());
        return result;
    }

    @Override
    public ResultData modifyOrder(GmairOrder order) {
        ResultData result = new ResultData();
        ResultData response = gmairDao.update(order);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return result;
        }
        result.setData(response.getData());
        return result;
    }
}
