package dao.impl;

import dao.BaseDao;
import dao.RoleDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import utils.ResponseCode;
import utils.ResultData;
import vo.role.RoleVO;

import java.util.List;
import java.util.Map;


@Repository
public class RoleDaoImpl extends BaseDao implements RoleDao{

    private Logger logger = LoggerFactory.getLogger(RoleDaoImpl.class);

    @Override
    public ResultData query(Map<String, Object> condition) {
         ResultData result = new ResultData();

         try {
             List<RoleVO> list = sqlSession.selectList("management.role.query", condition);
             result.setData(list);
             if (list.size() == 0) {
                 result.setResponseCode(ResponseCode.RESPONSE_NULL);
             }
         } catch (Exception e) {
             logger.error(e.getMessage());
             result.setResponseCode(ResponseCode.RESPONSE_ERROR);
             result.setDescription(e.getMessage());
         }
         return result;
    }
}
