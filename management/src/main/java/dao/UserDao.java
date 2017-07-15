package dao;

import java.util.Map;

import model.user.User;
import utils.ResultData;

public interface UserDao {
    ResultData query(Map<String, Object> condition);


    ResultData insert(User user);
}
