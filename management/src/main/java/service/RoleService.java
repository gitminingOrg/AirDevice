package service;


import utils.ResultData;

import java.util.Map;

public interface RoleService {

    ResultData query(Map<String, Object> condition);
}
