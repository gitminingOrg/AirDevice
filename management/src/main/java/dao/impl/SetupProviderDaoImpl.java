package dao.impl;

import dao.BaseDao;
import dao.SetupProviderDao;
import model.order.SetupProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import utils.IDGenerator;
import utils.ResponseCode;
import utils.ResultData;
import vo.order.SetupProviderVo;

import java.util.List;
import java.util.Map;

@Repository
public class SetupProviderDaoImpl extends BaseDao implements SetupProviderDao {
    private Logger logger = LoggerFactory.getLogger(SetupProviderDaoImpl.class);

    private Object lock = new Object();

    @Override
    public ResultData insert(SetupProvider provider) {
        ResultData result = new ResultData();
        provider.setProviderId(IDGenerator.generate("SPR"));
        synchronized (lock) {
            try {
                sqlSession.insert("management.setup.provider.insert", provider);
                result.setData(new SetupProviderVo(provider));
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
            }
        }
        return result;
    }

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<SetupProviderVo> list = sqlSession.selectList("management.setup.provider.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData update(SetupProvider provider) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                sqlSession.update("management.setup.provider.update", provider);
                result.setData(new SetupProviderVo(provider));
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
            }
        }
        return result;
    }

    @Override
    public ResultData delete(String providerId) {
        ResultData result = new ResultData();
        try {
            sqlSession.delete("management.setup.provider.delete", providerId);
            result.setDescription("已删除相关信息");
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}