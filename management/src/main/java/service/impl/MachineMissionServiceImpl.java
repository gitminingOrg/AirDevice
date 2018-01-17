package service.impl;

import dao.MachineMissionDao;
import model.order.MachineMission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.MachineMissionService;
import utils.ResponseCode;
import utils.ResultData;
import vo.order.MachineMissionVo;

import java.util.List;
import java.util.Map;

/**
 * Created by hushe on 2018/1/17.
 */
@Service
public class MachineMissionServiceImpl implements MachineMissionService {
    private Logger logger = LoggerFactory.getLogger(MachineMissionServiceImpl.class);

    @Autowired
    private MachineMissionDao machineMissionDao;

    @Override
    public ResultData fetch(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = machineMissionDao.query(condition);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            return response;
        } else {
            List<MachineMissionVo> list = (List<MachineMissionVo>) response.getData();
            result.setData(list);
            return result;
        }
    }

    @Override
    public ResultData create(MachineMission machineMission) {
        ResultData result = new ResultData();
        ResultData response = machineMissionDao.insert(machineMission);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else {
            result.setDescription(response.getDescription());
        }
        return result;
    }
}
