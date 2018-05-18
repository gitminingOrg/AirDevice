package finley.monitor.dao.impl;

import finley.monitor.dao.AdvertisementDao;
import finley.monitor.dao.BaseDao;
import finley.monitor.vo.AdvertisementVo;
import org.springframework.stereotype.Repository;
import utils.ResponseCode;
import utils.ResultData;

import java.util.List;
import java.util.Map;


@Repository
public class AdvertisementDaoImpl extends BaseDao implements AdvertisementDao{

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<AdvertisementVo> list = sqlSession.selectList("monitor.advertisement.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            } else {
                result.setData(list);
            }
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
}
