package service.impl;

import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import dao.UserDao;
import model.user.User;
import service.UserService;
import utils.Encryption;
import utils.ResponseCode;
import utils.ResultData;
import vo.user.UserVo;

@Service
public class UserServiceImpl implements UserService {
	private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserDao userDao;

	@Override
	public ResultData login(User user) {
		ResultData result = new ResultData();
		Map<String, Object> condition = new HashMap<>();
		user.setPassword(Encryption.md5(user.getPassword()));
		condition.put("username", user.getUsername());
		condition.put("blockFlag", false);
		ResultData response = userDao.query(condition);
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			UserVo target = ((List<UserVo>) response.getData()).get(0);
			if (!StringUtils.isEmpty(user.getPassword()) && user.getPassword().equals(target.getPassword())) {
				result.setData(target);
				return result;
			}
		}
		result.setResponseCode(ResponseCode.RESPONSE_NULL);
		result.setDescription("User " + user.getUsername() + "login failed");
		return result;
	}

	@Override
	public ResultData create(User user) {
		ResultData result = new ResultData();
		ResultData response = userDao.insert(user);
		result.setResponseCode(response.getResponseCode());
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(response.getData());
		} else {
			result.setDescription(response.getDescription());
		}
		return result;
	}
}
