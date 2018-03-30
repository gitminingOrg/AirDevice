package finley.monitor.service;

import utils.ResultData;

import java.util.Map;

public interface LogoPathService {
    ResultData fetchLogoPath(Map<String, Object> condition);
}
