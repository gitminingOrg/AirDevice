package service;

import model.user.User;
import utils.ResultData;

public interface UserService {
	ResultData login(User user);

	ResultData create(User user);
}
