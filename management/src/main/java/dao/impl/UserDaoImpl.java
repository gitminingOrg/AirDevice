package dao.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import dao.BaseDao;
import dao.UserDao;
import model.user.User;
import utils.ResponseCode;
import utils.ResultData;
import vo.user.UserVo;

@Repository
public class UserDaoImpl extends BaseDao implements UserDao{
	private Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

	@Override
	public ResultData query(Map<String, Object> condition) {
		ResultData result = new ResultData();
		try {
			List<UserVo> list = sqlSession.selectList("management.user.query", condition);
			if(list.isEmpty()) {
				result.setResponseCode(ResponseCode.RESPONSE_NULL);
			}
			result.setData(list);
		}catch (Exception e) {
			logger.error(e.getMessage());
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription(e.getMessage());
		}
		return result;
	}

	@Override
	public ResultData insert(User user) {
		// TODO Auto-generated method stub
		return null;
	}
}
