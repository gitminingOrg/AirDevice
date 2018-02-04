package dao.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import dao.BaseDao;
import dao.MachineItemDao;
import model.order.MachineItem;
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
import vo.order.MachineItemVo;
import vo.order.MachineMissionVo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by XXH on 2018/1/20.
 */
@Repository
public class MachineItemDaoImpl extends BaseDao implements MachineItemDao{
    private Logger logger = LoggerFactory.getLogger(MachineItemDao.class);
    private final Object lock = new Object();
    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<MachineItemVo> list = sqlSession.selectList("management.order.machineItem.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            } else {
                result.setData(list);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    public ResultData query(DataTableParam param) {
        ResultData result = new ResultData();
        DataTablePage<MachineItemVo> pages = new DataTablePage<>(param);
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        if (!StringUtils.isEmpty(param) && !StringUtils.isEmpty(param.getParams())) {
            JSONObject params = JSON.parseObject(param.getParams());
            if (!StringUtils.isEmpty(params.get("startDate"))) {
                condition.put("startTime", params.getString("startDate"));
            }
            if (!StringUtils.isEmpty(params.get("endDate"))) {
                condition.put("endTime", params.getString("endDate"));
            }
            if (!StringUtils.isEmpty(params.get("machineStatus"))) {
                condition.put("machineStatus", params.getString("machineStatus"));
            }
            if (!StringUtils.isEmpty(params.get("providerName"))) {
                condition.put("providerName", params.getString("providerName"));
            }
            if (!StringUtils.isEmpty(params.get("installType"))) {
                condition.put("installType", params.getString("installType"));
            }
        }
        if (!StringUtils.isEmpty(param) && !StringUtils.isEmpty(param.getsSearch())) {
            condition.put("search", new StringBuilder("%").append(param.getsSearch()).append("%").toString());
        }
        ResultData response = query(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(response.getResponseCode());
            result.setDescription(response.getDescription());
            return result;
        } else {
            List<MachineItemVo> totalRecords = (List<MachineItemVo>) response.getData();
            pages.setiTotalDisplayRecords(totalRecords.size());
            pages.setiTotalRecords(totalRecords.size());
            List<MachineItemVo> currentRecords = query(condition, param.getiDisplayStart(), param.getiDisplayLength());
            if (currentRecords.size() == 0) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            pages.setData(currentRecords);
            result.setData(pages);
            return result;
        }
    }

    private List<MachineItemVo> query(Map<String, Object> condition, int start, int length){
        return sqlSession.selectList("management.order.machineItem.query", condition, new RowBounds(start, length));
    }

    @Override
    public ResultData insert(MachineItem machineItem) {
        ResultData result = new ResultData();
        machineItem.setMachineId(IDGenerator.generate("MI"));
        synchronized (lock) {
            try {
                sqlSession.insert("management.order.machineItem.insert", machineItem);
                result.setData(machineItem);
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
            }
        }
        return result;
    }

    @Override
    public ResultData insertBatch(List<MachineItem> machineItemList){
        ResultData result = new ResultData();
        for (MachineItem machineItem : machineItemList) {
            machineItem.setMachineId(IDGenerator.generate("MI"));
        }
        synchronized (lock) {
            sqlSession.insert("management.order.machineItem.insertBatch", machineItemList);
            result.setData(machineItemList);
        }
        return result;
    }

    @Override
    public ResultData update(MachineItem machineItem) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                sqlSession.insert("management.order.machineItem.update", machineItem);
                result.setData(machineItem);
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
            }
        }
        return result;
    }

    @Override
    public ResultData updateBatch(List<MachineItem> machineItems) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                sqlSession.insert("management.order.machineItem.updateBatch", machineItems);
                result.setData(machineItems);
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
            }
        }
        return result;
    }
}
