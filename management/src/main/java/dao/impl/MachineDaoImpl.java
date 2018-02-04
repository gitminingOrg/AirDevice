package dao.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import dao.BaseDao;
import dao.MachineDao;
import model.machine.IdleMachine;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import pagination.DataTablePage;
import pagination.DataTableParam;
import utils.IDGenerator;
import utils.ResponseCode;
import utils.ResultData;
import vo.MachineStataVo;
import vo.machine.EverydayRecordVo;
import vo.machine.IdleMachineVo;
import vo.machine.MachineStatusVo;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Repository
public class MachineDaoImpl extends BaseDao implements MachineDao {
    private Logger logger = LoggerFactory.getLogger(MachineDaoImpl.class);

    @Override
    public ResultData deleteDevice(String deviceId) {
        ResultData result = new ResultData();
        try {
            sqlSession.delete("management.machine.deleteDevice", deviceId);
            sqlSession.update("management.machine.releaseQrcode", deviceId);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return result;
        }
        return result;
    }

    @Override
    public ResultData insertIdleMachine(IdleMachine machine) {
        ResultData result = new ResultData();
        machine.setImId(IDGenerator.generate("IME"));
        try {
            sqlSession.insert("management.machine.idle.insert", machine);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData queryIdleMachine(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<IdleMachineVo> list = sqlSession.selectList("management.machine.idle.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData updateIdleMachine(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("management.machine.idle.update", condition);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData queryMachineStatus(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<MachineStatusVo> list = sqlSession.selectList("management.machine.queryMachineStatus", condition);
            if (list.size() == 0) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                return result;
            }
            result.setData(list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData queryMachineStatus(Map<String, Object> condition, DataTableParam param) {
        ResultData result = new ResultData();
        JSONObject params = JSON.parseObject(param.getParams());
        DataTablePage<MachineStatusVo> pages = new DataTablePage<>(param);
        if (!StringUtils.isEmpty(param.getParams())) {
            if (!StringUtils.isEmpty(params.getString("startDate"))) {
                condition.put("startTime", params.getString("startDate"));
            }
            if (!StringUtils.isEmpty(params.getString("endDate"))) {
                condition.put("endTime", params.getString("endDate"));
            }
            if (!StringUtils.isEmpty(params.getString("modelCode"))) {
                condition.put("modelCode", params.getString("modelCode"));
            }
        }
        if (!StringUtils.isEmpty(param.getsSearch())) {
            condition.put("search", new StringBuilder("%").append(param.getsSearch()).append("%").toString());
        }

        ResultData total = queryMachineStatus(condition);
        if (total.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(total.getDescription());
            return result;
        }
        pages.setiTotalRecords(((List) total.getData()).size());
        pages.setiTotalDisplayRecords(((List) total.getData()).size());

        List<MachineStatusVo> current =
                queryMachineStatusByPage(condition, param.getiDisplayStart(), param.getiDisplayLength());
        pages.setData(current);
        result.setData(pages);

        return result;
    }

    private List<MachineStatusVo> queryMachineStatusByPage(Map<String, Object> condition, int start, int length) {
        List<MachineStatusVo> list = new LinkedList<>();
        try {
            list = sqlSession.selectList("management.machine.queryMachineStatus", condition, new RowBounds(start, length));

        }catch (Exception e) {
                logger.error(e.getMessage());
        }
        return list;
    }

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<MachineStataVo> list = sqlSession.selectList("management.machine.queryMachineStata", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData updateidle(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("management.machine.idle.updateidle", condition);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData queryEverydayRecord(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<EverydayRecordVo> list = sqlSession.selectList("management.everydayrecord.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData queryMachineStatusRange(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<MachineStataVo> list = sqlSession.selectList("management.machine.queryDeviceStatus", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
