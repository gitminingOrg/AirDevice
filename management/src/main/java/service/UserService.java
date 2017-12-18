package service;

import model.user.User;
import utils.ResultData;

import java.util.Map;

public interface UserService {
	ResultData login(User user);

	ResultData create(User user);

	ResultData fetch(Map<String, Object> condition);
}
