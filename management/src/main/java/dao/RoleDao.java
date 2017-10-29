package dao;

import utils.ResultData;

import java.util.Map;

public interface RoleDao {

    ResultData query(Map<String, Object> condition);
}
