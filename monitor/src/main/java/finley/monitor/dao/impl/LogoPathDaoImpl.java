package finley.monitor.dao.impl;

import finley.monitor.dao.BaseDao;
import finley.monitor.dao.LogoPathDao;
import finley.monitor.vo.LogoPathVo;
import org.springframework.stereotype.Repository;
import utils.ResponseCode;
import utils.ResultData;

import java.util.List;
import java.util.Map;

@Repository
public class LogoPathDaoImpl extends BaseDao implements LogoPathDao{
    @Override
    public ResultData queryLogoPath(Map<String, Object> condition) {
        ResultData resultData = new ResultData();
        try {
            List<LogoPathVo> logoPathVos = sqlSession.selectList("monitor.logopath.queryLogoPath", condition);
            if (logoPathVos.isEmpty()) {
                resultData.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            else {
                resultData.setData(logoPathVos);
            }
        }catch (Exception e) {
            resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
            resultData.setDescription(e.getMessage());
        }
        return resultData;
    }
}
