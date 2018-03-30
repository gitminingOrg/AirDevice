package finley.monitor.dao;

import utils.ResultData;

import java.util.Map;

public interface LogoPathDao {
    ResultData queryLogoPath(Map<String, Object> condition);
}
