package dao.impl;

import dao.BaseDao;
import dao.CommodityDao;
import model.guomai.Commodity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import utils.IDGenerator;
import utils.ResponseCode;
import utils.ResultData;
import vo.guomai.CommodityVo;

import java.util.List;
import java.util.Map;

/**
 * Created by hushe on 2018/1/8.
 */
@Repository
public class CommodityDaoImpl extends BaseDao implements CommodityDao {
    private Logger logger = LoggerFactory.getLogger(CommodityDaoImpl.class);

    private Object lock = new Object();

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<CommodityVo> list = sqlSession.selectList("management.commodity.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            } else {
                result.setData(list);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData insert(Commodity commodity) {
        ResultData result = new ResultData();
        commodity.setCommodityId(IDGenerator.generate("COM"));
        synchronized (lock) {
            try {
                sqlSession.insert("management.commodity.insert", commodity);
                result.setData(commodity);
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
            }
        }
        return result;
    }

    @Override
    public ResultData update(Commodity commodity) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                sqlSession.update("management.commodity.update", commodity);
                result.setData(commodity);
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
            }
        }
        return result;
    }
}
